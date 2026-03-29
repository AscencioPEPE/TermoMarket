package com.ascencio.thermomarket.order.repository;

import com.ascencio.thermomarket.order.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
}
