package com.amigoscode.repository;

import com.amigoscode.AbstractTestcontainers;
import com.amigoscode.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }


    @Test
    void existsCustomerByEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        Customer actual = underTest.save(customer);
        assertThat(actual.getEmail()).isEqualTo(customer.getEmail());
        assertThat(actual.getAge()).isEqualTo(customer.getAge());
        assertThat(actual.getName()).isEqualTo(customer.getName());
        boolean existsCustomerByEmail = underTest.existsCustomerByEmail(customer.getEmail());
        assertThat(existsCustomerByEmail).isTrue();
    }
    @Test
    void existsCustomerNotByEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        boolean existsCustomerByEmail = underTest.existsCustomerByEmail(email);
        assertThat(existsCustomerByEmail).isFalse();
    }

    @Test
    void existsCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        Customer actual = underTest.save(customer);
        boolean existsCustomerById = underTest.existsCustomerById(actual.getId());
        assertThat(existsCustomerById).isTrue();
    }
    @Test
    void existsCustomerNotById() {

        boolean existsCustomerById = underTest.existsCustomerById(-1L);
        assertThat(existsCustomerById).isFalse();
    }
}