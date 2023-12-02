package com.amigoscode.repository;

import com.amigoscode.model.Customer;
import com.amigoscode.model.dto.CustomerUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Long id);
    void insertCustomer(Customer customer);
    boolean existsCustomerWithEmail(String email);
    boolean existsCustomerWithId(Long email);
    void deleteCustomerById(Long id);
    void updateCustomer(Long id, CustomerUpdateRequest request);
}
