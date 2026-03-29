package com.ascencio.thermomarket.catalog.service;

import com.ascencio.thermomarket.catalog.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public final class ProductSpecifications {

    private ProductSpecifications() {
    }

    public static Specification<Product> withFilters(
            String search,
            Boolean active,
            String brand,
            String category,
            String color,
            Integer capacityOz,
            Long cursorId
    ) {
        return Specification.allOf(
                hasSearch(search),
                hasActive(active),
                hasBrand(brand),
                hasCategory(category),
                hasColor(color),
                hasCapacityOz(capacityOz),
                idBefore(cursorId)
        );
    }

    private static Specification<Product> hasSearch(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String pattern = "%" + search.trim().toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("slug")), pattern)
            );
        };
    }

    private static Specification<Product> hasActive(Boolean active) {
        return (root, query, criteriaBuilder) ->
                active == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("active"), active);
    }

    private static Specification<Product> hasBrand(String brand) {
        return (root, query, criteriaBuilder) -> {
            if (brand == null || brand.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("brand")), brand.trim().toLowerCase());
        };
    }

    private static Specification<Product> hasCategory(String category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null || category.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("category")), category.trim().toLowerCase());
        };
    }

    private static Specification<Product> hasColor(String color) {
        return (root, query, criteriaBuilder) -> {
            if (color == null || color.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("color")), color.trim().toLowerCase());
        };
    }

    private static Specification<Product> hasCapacityOz(Integer capacityOz) {
        return (root, query, criteriaBuilder) ->
                capacityOz == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("capacityOz"), capacityOz);
    }

    private static Specification<Product> idBefore(Long cursorId) {
        return (root, query, criteriaBuilder) ->
                cursorId == null ? criteriaBuilder.conjunction() : criteriaBuilder.lessThan(root.get("id"), cursorId);
    }
}
