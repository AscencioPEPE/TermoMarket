package com.ascencio.thermomarket.order.service;

import com.ascencio.thermomarket.catalog.entity.Product;
import com.ascencio.thermomarket.catalog.service.ProductService;
import com.ascencio.thermomarket.customer.entity.Address;
import com.ascencio.thermomarket.customer.entity.Customer;
import com.ascencio.thermomarket.customer.repository.AddressRepository;
import com.ascencio.thermomarket.customer.service.CustomerService;
import com.ascencio.thermomarket.order.dto.OrderItemRequest;
import com.ascencio.thermomarket.order.dto.OrderItemResponse;
import com.ascencio.thermomarket.order.dto.OrderRequest;
import com.ascencio.thermomarket.order.dto.OrderResponse;
import com.ascencio.thermomarket.order.entity.OrderItem;
import com.ascencio.thermomarket.order.entity.OrderStatus;
import com.ascencio.thermomarket.order.entity.PurchaseOrder;
import com.ascencio.thermomarket.order.repository.PurchaseOrderRepository;
import com.ascencio.thermomarket.shared.exception.ResourceNotFoundException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductService productService;
    private final CustomerService customerService;
    private final AddressRepository addressRepository;

    public OrderService(
            PurchaseOrderRepository purchaseOrderRepository,
            ProductService productService,
            CustomerService customerService,
            AddressRepository addressRepository
    ) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.productService = productService;
        this.customerService = customerService;
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return purchaseOrderRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        return toResponse(getOrder(id));
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        Customer customer = customerService.getCustomer(request.customerId());
        Address address = addressRepository.findById(request.shippingAddressId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address %d was not found".formatted(request.shippingAddressId())));

        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new IllegalArgumentException("The address does not belong to the selected customer");
        }

        PurchaseOrder order = new PurchaseOrder();
        order.setCustomer(customer);
        order.setShippingAddress(address);
        order.setStatus(OrderStatus.PENDING);
        order.setNotes(request.notes());
        order.setOrderDate(OffsetDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productService.getEntity(itemRequest.productId());
            validateStock(product, itemRequest.quantity());

            product.setStock(product.getStock() - itemRequest.quantity());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setUnitPrice(product.getPrice());
            item.setQuantity(itemRequest.quantity());
            item.setLineTotal(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
            order.getItems().add(item);

            total = total.add(item.getLineTotal());
        }

        order.setTotal(total);
        return toResponse(purchaseOrderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public PurchaseOrder getOrder(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order %d was not found".formatted(id)));
    }

    private void validateStock(Product product, Integer quantity) {
        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new IllegalArgumentException("Product '%s' is inactive".formatted(product.getName()));
        }

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException(
                    "Insufficient stock for product '%s'. Available: %d".formatted(product.getName(), product.getStock()));
        }
    }

    private OrderResponse toResponse(PurchaseOrder order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomer().getId(),
                order.getShippingAddress().getId(),
                order.getStatus(),
                order.getTotal(),
                order.getNotes(),
                order.getOrderDate(),
                order.getItems().stream().map(this::toResponse).toList(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    private OrderItemResponse toResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getProductId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getLineTotal()
        );
    }
}
