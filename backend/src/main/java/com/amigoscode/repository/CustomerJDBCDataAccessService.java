package com.amigoscode.repository;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.ResourceNotFound;
import com.amigoscode.model.Customer;
import com.amigoscode.model.dto.CustomerUpdateRequest;
import com.amigoscode.service.CustomerRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                """;

        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                WHERE id = ?
                """;
        return jdbcTemplate.query(
                        sql,
                        customerRowMapper,
                        id
                )
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, age)
                VALUES (?, ?, ?)
                """;
        int update = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
        System.out.println("jdbctemplate.update = " + update);

    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsCustomerWithId(Long id) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomerById(Long id) {
        if (!existsPersonWithId(id)) throw new ResourceNotFound("Customer with id [%s] not found!".formatted(id));
        var sql = """
                DELETE FROM customer
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateCustomer(Long id, CustomerUpdateRequest request) {
        if (!existsPersonWithId(id)) throw new ResourceNotFound("Customer with id [%s] not found!".formatted(id));
        if (existsCustomerWithEmail(request.email()))
            throw new DuplicateResourceException("This email is already taken!");
        var sql = """
                UPDATE customer
                SET
                name = ?,
                email = ?,
                age = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(
                sql,
                request.name(),
                request.email(),
                request.age(),
                id);
    }

    private boolean existsPersonWithId(Long id) {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                WHERE id = ?
                """;
        return !jdbcTemplate.query(sql, customerRowMapper, id).isEmpty();
    }
}
