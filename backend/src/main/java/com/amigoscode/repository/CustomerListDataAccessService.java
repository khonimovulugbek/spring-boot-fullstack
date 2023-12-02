package com.amigoscode.repository;

import com.amigoscode.model.Customer;
import com.amigoscode.model.dto.CustomerUpdateRequest;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{
    @Override
    public List<Customer> selectAllCustomers() {
        return Collections.emptyList();
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return Optional.empty();
    }

    @Override
    public void insertCustomer(Customer customer) {

    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return false;
    }

    @Override
    public boolean existsCustomerWithId(Long email) {
        return true;
    }

    @Override
    public void deleteCustomerById(Long id) {

    }

    @Override
    public void updateCustomer(Long id, CustomerUpdateRequest request) {
        
    }
}
