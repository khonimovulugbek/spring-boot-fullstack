package com.amigoscode.service;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.ResourceNotFound;
import com.amigoscode.model.Customer;
import com.amigoscode.model.dto.CustomerRegistrationRequest;
import com.amigoscode.model.dto.CustomerUpdateRequest;
import com.amigoscode.repository.CustomerDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;
    private final ObjectMapper objectMapper;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao, ObjectMapper objectMapper) {
        this.customerDao = customerDao;
        this.objectMapper = objectMapper;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Long id){
        return customerDao.selectCustomerById(id)
                .orElseThrow(()-> new ResourceNotFound("Customer with id [%s] not found.".formatted(id)));
    }
    public void save(CustomerRegistrationRequest request){
        boolean existsPersonWithEmail = customerDao.existsCustomerWithEmail(request.email());
        if (existsPersonWithEmail) throw new DuplicateResourceException(
              "email already taken"
        );
        Customer customer = objectMapper.convertValue(request, Customer.class);

        customerDao.insertCustomer(customer);
    }

    public void deleteCustomerById(Long id) {
        if (!customerDao.existsCustomerWithId(id)) throw new ResourceNotFound("customer with id [%s] not found".formatted(id));
        customerDao.deleteCustomerById(id);
    }

    public void updateCustomer(Long id, CustomerUpdateRequest request) {
        if (!customerDao.existsCustomerWithId(id)) throw new ResourceNotFound("customer with id [%s] not found".formatted(id));
        if (customerDao.existsCustomerWithEmail(request.email())) throw new DuplicateResourceException(
                "email already taken"
        );
        customerDao.updateCustomer(id,request);
    }
}
