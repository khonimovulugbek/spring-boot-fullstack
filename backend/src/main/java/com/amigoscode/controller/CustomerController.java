package com.amigoscode.controller;

import com.amigoscode.model.Customer;
import com.amigoscode.model.dto.CustomerRegistrationRequest;
import com.amigoscode.model.dto.CustomerUpdateRequest;
import com.amigoscode.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("{id}")
    public Customer getCustomerById(@PathVariable Long id){
        return customerService.getCustomer(id);
    }

    @PostMapping
    public void saveCustomer(@RequestBody CustomerRegistrationRequest request){
        customerService.save(request);
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomerById(id);
    }

    @PutMapping("{id}")
    public void updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerUpdateRequest request){
        customerService.updateCustomer(id,request);
    }
}
