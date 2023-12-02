package com.amigoscode.repository;

import com.amigoscode.exception.ResourceNotFound;
import com.amigoscode.model.Customer;
import com.amigoscode.model.dto.CustomerUpdateRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJpaDataAccessService implements CustomerDao{
    private final CustomerRepository customerRepository;

    public CustomerJpaDataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customerRepository.findById(id);

    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public boolean existsCustomerWithId(Long id) {
        return customerRepository.existsCustomerById(id);
    }

    @Override
    public void deleteCustomerById(Long id) {
        if (!customerRepository.existsById(id))
            throw new ResourceNotFound("customer with id [%s] not found!".formatted(id));
        customerRepository.deleteById(id);
    }

    @Override
    public void updateCustomer(Long id, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("customer with id [%s] not found!".formatted(id)));
        customer.setId(id);
        customer.setAge(request.age());
        customer.setEmail(request.email());
        customer.setName(request.email());
        customerRepository.save(customer);
    }
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
