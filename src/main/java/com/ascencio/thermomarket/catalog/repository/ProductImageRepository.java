package com.ascencio.thermomarket.catalog.repository;

import com.ascencio.thermomarket.catalog.entity.ProductImage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductIdOrderByPrimaryImageDescSortOrderAscIdAsc(Long productId);
    Optional<ProductImage> findByProductIdAndStorageKey(Long productId, String storageKey);
    Optional<ProductImage> findFirstByProductIdOrderByPrimaryImageDescSortOrderAscIdAsc(Long productId);
    Optional<ProductImage> findByIdAndProductId(Long id, Long productId);
}
