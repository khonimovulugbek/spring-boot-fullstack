package com.amigoscode.repository;

import com.amigoscode.model.Customer;
import com.amigoscode.model.dto.CustomerUpdateRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerJpaDataAccessServiceTest {
    private CustomerJpaDataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJpaDataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {

        // When
        underTest.selectAllCustomers();
        verify(customerRepository)
                .findAll();

        //Then
    }

    @Test
    void selectCustomerById() {
        // Given
        Long id = 1L;

        underTest.selectCustomerById(id);

        verify(customerRepository).findById(id);
        // When

        //Then
    }

    @Test
    void insertCustomer() {
        Customer customer = new Customer(
                1L, "Ali","ali@gmail.com",1
        );
        underTest.insertCustomer(customer);
        verify(customerRepository).save(customer);
    }

    @Test
    void existsCustomerWithEmail() {
        // Given
        String email = "foo@gmail.com";

        // When
        underTest.existsCustomerWithEmail(email);
        //Then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existsCustomerWithId() {
        // Given
        Long id = 1L;

        // When
        underTest.existsCustomerWithId(id);
        //Then
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        // Given
        Long id = 2L;

        when(customerRepository.existsById(id)).thenReturn(true);
        // When
        underTest.deleteCustomerById(id);

        //Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        Long id = 1L;
        CustomerUpdateRequest request = new CustomerUpdateRequest(
          "ali",
          "ali@mail.ru",
          23
        );
        Customer customer = new Customer(
                id,
                request.name(),
                request.email(),
                request.age()
        );
        underTest.updateCustomer(customer);

        verify(customerRepository).save(customer);

        // When

        //Then
    }
}