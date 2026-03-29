package com.ascencio.thermomarket.catalog.controller;

import com.ascencio.thermomarket.catalog.dto.ProductCatalogResponse;
import com.ascencio.thermomarket.catalog.dto.ProductFilterOptionsResponse;
import com.ascencio.thermomarket.catalog.dto.ProductImageCreateRequest;
import com.ascencio.thermomarket.catalog.dto.ProductImagePresignRequest;
import com.ascencio.thermomarket.catalog.dto.ProductImagePresignResponse;
import com.ascencio.thermomarket.catalog.dto.ProductImageResponse;
import com.ascencio.thermomarket.catalog.dto.ProductImageUpdateRequest;
import com.ascencio.thermomarket.catalog.dto.ProductRequest;
import com.ascencio.thermomarket.catalog.dto.ProductResponse;
import com.ascencio.thermomarket.catalog.service.ProductImageService;
import com.ascencio.thermomarket.catalog.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductImageService productImageService;

    public ProductController(ProductService productService, ProductImageService productImageService) {
        this.productService = productService;
        this.productImageService = productImageService;
    }

    @GetMapping
    public ProductCatalogResponse findCatalog(
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer capacityOz
    ) {
        return productService.findCatalog(cursor, limit, search, active, brand, category, color, capacityOz);
    }

    @GetMapping("/all")
    public List<ProductResponse> findAll() {
        return productService.findAll();
    }

    @GetMapping("/filters")
    public ProductFilterOptionsResponse findFilterOptions() {
        return productService.findFilterOptions();
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @GetMapping("/{productId}/images")
    public List<ProductImageResponse> findImages(@PathVariable Long productId) {
        return productImageService.findByProductId(productId);
    }

    @PostMapping("/{productId}/images/presign")
    public ProductImagePresignResponse createImagePresign(
            @PathVariable Long productId,
            @Valid @RequestBody ProductImagePresignRequest request
    ) {
        return productImageService.createPresignedUpload(productId, request);
    }

    @PostMapping("/{productId}/images")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductImageResponse registerImage(
            @PathVariable Long productId,
            @Valid @RequestBody ProductImageCreateRequest request
    ) {
        return productImageService.registerImage(productId, request);
    }

    @PutMapping("/{productId}/images/{imageId}")
    public ProductImageResponse updateImage(
            @PathVariable Long productId,
            @PathVariable Long imageId,
            @Valid @RequestBody ProductImageUpdateRequest request
    ) {
        return productImageService.updateImage(productId, imageId, request);
    }

    @DeleteMapping("/{productId}/images/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable Long productId, @PathVariable Long imageId) {
        productImageService.deleteImage(productId, imageId);
    }
}
