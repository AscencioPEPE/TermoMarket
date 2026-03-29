package com.ascencio.thermomarket.customer.service;

import com.ascencio.thermomarket.customer.dto.AddressRequest;
import com.ascencio.thermomarket.customer.dto.AddressResponse;
import com.ascencio.thermomarket.customer.dto.CustomerRequest;
import com.ascencio.thermomarket.customer.dto.CustomerResponse;
import com.ascencio.thermomarket.customer.entity.Address;
import com.ascencio.thermomarket.customer.entity.Customer;
import com.ascencio.thermomarket.customer.repository.AddressRepository;
import com.ascencio.thermomarket.customer.repository.CustomerRepository;
import com.ascencio.thermomarket.shared.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    public CustomerService(CustomerRepository customerRepository, AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id) {
        return toResponse(getCustomer(id));
    }

    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("A customer with email '%s' already exists".formatted(request.email()));
        }

        Customer customer = new Customer();
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());

        return toResponse(customerRepository.save(customer));
    }

    @Transactional
    public AddressResponse addAddress(Long customerId, AddressRequest request) {
        Customer customer = getCustomer(customerId);
        Address address = new Address();
        address.setCustomer(customer);
        address.setStreet(request.street());
        address.setExteriorNumber(request.exteriorNumber());
        address.setInteriorNumber(request.interiorNumber());
        address.setNeighborhood(request.neighborhood());
        address.setCity(request.city());
        address.setState(request.state());
        address.setPostalCode(request.postalCode());
        address.setReferences(request.references());
        return toAddressResponse(addressRepository.save(address));
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> findAddresses(Long customerId) {
        getCustomer(customerId);
        return addressRepository.findByCustomerId(customerId).stream().map(this::toAddressResponse).toList();
    }

    @Transactional(readOnly = true)
    public Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer %d was not found".formatted(customerId)));
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }

    private AddressResponse toAddressResponse(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getCustomer().getId(),
                address.getStreet(),
                address.getExteriorNumber(),
                address.getInteriorNumber(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getReferences(),
                address.getCreatedAt(),
                address.getUpdatedAt()
        );
    }
}
