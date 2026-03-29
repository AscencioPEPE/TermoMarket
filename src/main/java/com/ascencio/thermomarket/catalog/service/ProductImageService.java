package com.ascencio.thermomarket.catalog.service;

import com.ascencio.thermomarket.catalog.dto.ProductImageCreateRequest;
import com.ascencio.thermomarket.catalog.dto.ProductImagePresignRequest;
import com.ascencio.thermomarket.catalog.dto.ProductImagePresignResponse;
import com.ascencio.thermomarket.catalog.dto.ProductImageResponse;
import com.ascencio.thermomarket.catalog.dto.ProductImageUpdateRequest;
import com.ascencio.thermomarket.catalog.entity.Product;
import com.ascencio.thermomarket.catalog.entity.ProductImage;
import com.ascencio.thermomarket.catalog.repository.ProductImageRepository;
import com.ascencio.thermomarket.shared.exception.ResourceNotFoundException;
import com.ascencio.thermomarket.storage.config.S3Properties;
import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class ProductImageService {

    private static final Duration PRESIGNED_UPLOAD_DURATION = Duration.ofMinutes(10);

    private final ProductService productService;
    private final ProductImageRepository productImageRepository;
    private final ProductImageKeyFactory productImageKeyFactory;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3Properties s3Properties;

    public ProductImageService(
            ProductService productService,
            ProductImageRepository productImageRepository,
            ProductImageKeyFactory productImageKeyFactory,
            S3Client s3Client,
            S3Presigner s3Presigner,
            S3Properties s3Properties
    ) {
        this.productService = productService;
        this.productImageRepository = productImageRepository;
        this.productImageKeyFactory = productImageKeyFactory;
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.s3Properties = s3Properties;
    }

    @Transactional(readOnly = true)
    public List<ProductImageResponse> findByProductId(Long productId) {
        productService.getEntity(productId);
        return productImageRepository.findByProductIdOrderByPrimaryImageDescSortOrderAscIdAsc(productId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public String findPrimaryImageUrl(Long productId) {
        return productImageRepository.findFirstByProductIdOrderByPrimaryImageDescSortOrderAscIdAsc(productId)
                .map(ProductImage::getStorageKey)
                .map(this::buildPublicUrl)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public ProductImagePresignResponse createPresignedUpload(Long productId, ProductImagePresignRequest request) {
        productService.getEntity(productId);
        validateS3Configuration();

        String storageKey = productImageKeyFactory.createProductOriginalKey(productId, request.contentType());
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Properties.bucketName())
                .key(storageKey)
                .contentType(request.contentType())
                .contentLength(request.sizeBytes())
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(
                PutObjectPresignRequest.builder()
                        .signatureDuration(PRESIGNED_UPLOAD_DURATION)
                        .putObjectRequest(putObjectRequest)
                        .build());

        return new ProductImagePresignResponse(
                storageKey,
                presignedRequest.url().toString(),
                buildPublicUrl(storageKey),
                OffsetDateTime.now().plus(PRESIGNED_UPLOAD_DURATION),
                request.contentType()
        );
    }

    @Transactional
    public ProductImageResponse registerImage(Long productId, ProductImageCreateRequest request) {
        Product product = productService.getEntity(productId);
        validateS3Configuration();
        validateStorageKey(productId, request.storageKey());

        productImageRepository.findByProductIdAndStorageKey(productId, request.storageKey())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("This storage key is already registered for the product");
                });

        if (Boolean.TRUE.equals(request.primaryImage())) {
            clearPrimaryImage(productId);
        }

        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setStorageKey(request.storageKey());
        image.setOriginalFilename(request.originalFilename());
        image.setContentType(request.contentType());
        image.setSizeBytes(request.sizeBytes());
        image.setWidth(request.width());
        image.setHeight(request.height());
        image.setPrimaryImage(request.primaryImage());
        image.setSortOrder(request.sortOrder());

        return toResponse(productImageRepository.save(image));
    }

    @Transactional
    public ProductImageResponse updateImage(Long productId, Long imageId, ProductImageUpdateRequest request) {
        productService.getEntity(productId);
        ProductImage image = getProductImage(productId, imageId);

        if (Boolean.TRUE.equals(request.primaryImage())) {
            clearPrimaryImage(productId);
        }

        image.setPrimaryImage(request.primaryImage());
        image.setSortOrder(request.sortOrder());
        return toResponse(productImageRepository.save(image));
    }

    @Transactional
    public void deleteImage(Long productId, Long imageId) {
        productService.getEntity(productId);
        ProductImage image = getProductImage(productId, imageId);
        validateS3Configuration();

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(s3Properties.bucketName())
                .key(image.getStorageKey())
                .build());

        productImageRepository.delete(image);
    }

    private void clearPrimaryImage(Long productId) {
        List<ProductImage> images = productImageRepository.findByProductIdOrderByPrimaryImageDescSortOrderAscIdAsc(productId);
        for (ProductImage image : images) {
            if (Boolean.TRUE.equals(image.getPrimaryImage())) {
                image.setPrimaryImage(false);
            }
        }
    }

    private ProductImage getProductImage(Long productId, Long imageId) {
        return productImageRepository.findByIdAndProductId(imageId, productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Image %d was not found for product %d".formatted(imageId, productId)));
    }

    private void validateStorageKey(Long productId, String storageKey) {
        String expectedPrefix = "products/%d/".formatted(productId);
        if (!storageKey.startsWith(expectedPrefix)) {
            throw new IllegalArgumentException("The storage key does not belong to the selected product");
        }
    }

    private void validateS3Configuration() {
        if (s3Properties.bucketName() == null || s3Properties.bucketName().isBlank()) {
            throw new IllegalStateException("S3 bucket name is not configured");
        }
    }

    private ProductImageResponse toResponse(ProductImage image) {
        return new ProductImageResponse(
                image.getId(),
                image.getStorageKey(),
                image.getOriginalFilename(),
                image.getContentType(),
                image.getSizeBytes(),
                image.getWidth(),
                image.getHeight(),
                image.getPrimaryImage(),
                image.getSortOrder(),
                buildPublicUrl(image.getStorageKey()),
                image.getCreatedAt(),
                image.getUpdatedAt()
        );
    }

    private String buildPublicUrl(String storageKey) {
        if (s3Properties.publicBaseUrl() != null && !s3Properties.publicBaseUrl().isBlank()) {
            return s3Properties.publicBaseUrl().replaceAll("/+$", "") + "/" + storageKey;
        }

        URL url = s3Presigner.presignGetObject(builder -> builder
                        .signatureDuration(Duration.ofMinutes(10))
                        .getObjectRequest(get -> get.bucket(s3Properties.bucketName()).key(storageKey)))
                .url();
        return url.toString();
    }
}
