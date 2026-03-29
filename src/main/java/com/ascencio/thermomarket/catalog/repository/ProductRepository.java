package com.ascencio.thermomarket.catalog.repository;

import com.ascencio.thermomarket.catalog.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findBySlug(String slug);
    boolean existsBySlug(String slug);

    @Query("select distinct p.brand from Product p where p.active = true order by p.brand asc")
    List<String> findDistinctActiveBrands();

    @Query("select distinct p.category from Product p where p.active = true order by p.category asc")
    List<String> findDistinctActiveCategories();

    @Query("select distinct p.color from Product p where p.active = true order by p.color asc")
    List<String> findDistinctActiveColors();

    @Query("select distinct p.capacityOz from Product p where p.active = true order by p.capacityOz asc")
    List<Integer> findDistinctActiveCapacitiesOz();
}
