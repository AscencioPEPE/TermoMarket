package com.ascencio.thermomarket.catalog.service;

import com.ascencio.thermomarket.catalog.dto.ProductCatalogResponse;
import com.ascencio.thermomarket.catalog.dto.ProductFilterOptionsResponse;
import com.ascencio.thermomarket.catalog.dto.ProductRequest;
import com.ascencio.thermomarket.catalog.dto.ProductResponse;
import com.ascencio.thermomarket.catalog.entity.Product;
import com.ascencio.thermomarket.catalog.repository.ProductRepository;
import com.ascencio.thermomarket.shared.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCursorCodec productCursorCodec;
    private final ProductImageService productImageService;

    public ProductService(
            ProductRepository productRepository,
            ProductCursorCodec productCursorCodec,
            ProductImageService productImageService
    ) {
        this.productRepository = productRepository;
        this.productCursorCodec = productCursorCodec;
        this.productImageService = productImageService;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductFilterOptionsResponse findFilterOptions() {
        return new ProductFilterOptionsResponse(
                productRepository.findDistinctActiveBrands(),
                productRepository.findDistinctActiveCategories(),
                productRepository.findDistinctActiveColors(),
                productRepository.findDistinctActiveCapacitiesOz()
        );
    }

    @Transactional(readOnly = true)
    public ProductCatalogResponse findCatalog(
            String cursor,
            Integer limit,
            String search,
            Boolean active,
            String brand,
            String category,
            String color,
            Integer capacityOz
    ) {
        int resolvedLimit = normalizeLimit(limit);
        Long cursorId = cursor == null || cursor.isBlank() ? null : productCursorCodec.decode(cursor);

        List<Product> products = productRepository.findAll(
                        ProductSpecifications.withFilters(search, active, brand, category, color, capacityOz, cursorId),
                        PageRequest.of(0, resolvedLimit + 1, Sort.by(Sort.Direction.DESC, "id")))
                .getContent();

        boolean hasNext = products.size() > resolvedLimit;
        List<Product> currentItems = hasNext ? products.subList(0, resolvedLimit) : products;
        String nextCursor = hasNext
                ? productCursorCodec.encode(currentItems.get(currentItems.size() - 1).getId())
                : null;

        return new ProductCatalogResponse(
                currentItems.stream().map(this::toResponse).toList(),
                nextCursor,
                hasNext,
                resolvedLimit
        );
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        if (productRepository.existsBySlug(request.slug())) {
            throw new IllegalArgumentException("A product with slug '%s' already exists".formatted(request.slug()));
        }

        Product product = new Product();
        apply(product, request);
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = getEntity(id);
        productRepository.findBySlug(request.slug())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("A product with slug '%s' already exists".formatted(request.slug()));
                });

        apply(product, request);
        return toResponse(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public Product getEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product %d was not found".formatted(id)));
    }

    private int normalizeLimit(Integer limit) {
        if (limit == null) {
            return 20;
        }

        if (limit < 1 || limit > 100) {
            throw new IllegalArgumentException("Limit must be between 1 and 100");
        }

        return limit;
    }

    private void apply(Product product, ProductRequest request) {
        product.setName(request.name());
        product.setSlug(request.slug());
        product.setDescription(request.description());
        product.setBrand(request.brand());
        product.setCategory(request.category());
        product.setColor(request.color());
        product.setMaterial(request.material());
        product.setCapacityOz(request.capacityOz());
        product.setImageAlt(request.imageAlt());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setActive(request.active());
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getDescription(),
                product.getBrand(),
                product.getCategory(),
                product.getColor(),
                product.getMaterial(),
                product.getCapacityOz(),
                product.getImageAlt(),
                product.getPrice(),
                product.getStock(),
                product.getActive(),
                productImageService.findPrimaryImageUrl(product.getId()),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
