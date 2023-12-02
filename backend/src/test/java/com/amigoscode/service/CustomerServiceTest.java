package com.amigoscode.service;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.ResourceNotFound;
import com.amigoscode.model.Customer;
import com.amigoscode.model.dto.CustomerRegistrationRequest;
import com.amigoscode.model.dto.CustomerUpdateRequest;
import com.amigoscode.repository.CustomerDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao, new ObjectMapper());
    }

    @Test
    void getAllCustomers() {
        // Given
        underTest.getAllCustomers();
        // When
        verify(customerDao).selectAllCustomers();
        //Then
    }

    @Test
    void canGetCustomer() {
        // Given
        Long id = 1L;
        Customer customer = new Customer(
                id, "alex", "alex@gmail.com", 19
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // When
        Customer actual = underTest.getCustomer(id);

        assertThat(actual).isEqualTo(customer);
        //Then
        verify(customerDao).selectCustomerById(id);
    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {
        // Given
        Long id = 111L;

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage(
                        "Customer with id [%s] not found.".formatted(id)
                );
    }

    @Test
    void save() {
        // Given
        String email = "ali@gmail.com";

        when(customerDao.existsCustomerWithEmail(email)).thenReturn(false);


        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "ali",
                email,
                2
        );
        underTest.save(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        // When
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        //Then
        Customer captorValue = customerArgumentCaptor.getValue();
        assertThat(captorValue.getId()).isNull();
        assertThat(captorValue.getName()).isEqualTo(request.name());
        assertThat(captorValue.getEmail()).isEqualTo(request.email());
        assertThat(captorValue.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingACustomer() {
        // Given
        String email = "ali@gmail.com";

        when(customerDao.existsCustomerWithEmail(email)).thenReturn(true);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "ali",
                email,
                2
        );
        assertThatThrownBy(() -> underTest.save(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // When
        verify(customerDao, never()).insertCustomer(any());
        //Then

    }


    @Test
    void deleteCustomerById() {
        // Given
        Long id = 1L;

        when(customerDao.existsCustomerWithId(id)).thenReturn(true);

        // "customer with id [%s] not found".formatted(id)
        underTest.deleteCustomerById(id);
        //Then
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void willThrowWhenEmailExistsWhileDeletingCustomerById() {
        // Given
        Long id = 1L;

        when(customerDao.existsCustomerWithId(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

        verify(customerDao,never()).deleteCustomerById(any());
    }


    @Test
    void updateCustomer() {
        // Given
        Long id = 1L;
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "alijon",
                "alijon@mail.ru",
                22
        );
//        Customer customer = new Customer(
//                id, "alex", "alex@gmail.com", 19
//        );
//        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existsCustomerWithId(id)).thenReturn(true);
        when(customerDao.existsCustomerWithEmail(request.email())).thenReturn(false);

        underTest.updateCustomer(id, request);

        verify(customerDao).updateCustomer(id, request);
    }
    @Test
    void willThrowWhenIdIsNotExistsWhileUpdateCustomer() {
        // Given
        Long id = 1L;
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "alijon",
                "alijon@mail.ru",
                22
        );
        when(customerDao.existsCustomerWithId(id)).thenReturn(false);
        when(customerDao.existsCustomerWithEmail(request.email())).thenReturn(true);


        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

        when(customerDao.existsCustomerWithId(id)).thenReturn(true);
        when(customerDao.existsCustomerWithEmail(request.email())).thenReturn(true);
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");
        verify(customerDao, never()).updateCustomer(any(), any());

        //customer with id [1] not found
        verify(customerDao, never()).updateCustomer(any(), any());
    }

}