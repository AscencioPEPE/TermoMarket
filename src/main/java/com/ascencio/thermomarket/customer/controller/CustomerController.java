package com.ascencio.thermomarket.customer.controller;

import com.ascencio.thermomarket.customer.dto.AddressRequest;
import com.ascencio.thermomarket.customer.dto.AddressResponse;
import com.ascencio.thermomarket.customer.dto.CustomerRequest;
import com.ascencio.thermomarket.customer.dto.CustomerResponse;
import com.ascencio.thermomarket.customer.service.CustomerService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerResponse> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public CustomerResponse findById(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(@Valid @RequestBody CustomerRequest request) {
        return customerService.create(request);
    }

    @GetMapping("/{customerId}/addresses")
    public List<AddressResponse> findAddresses(@PathVariable Long customerId) {
        return customerService.findAddresses(customerId);
    }

    @PostMapping("/{customerId}/addresses")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponse addAddress(@PathVariable Long customerId, @Valid @RequestBody AddressRequest request) {
        return customerService.addAddress(customerId, request);
    }
}
