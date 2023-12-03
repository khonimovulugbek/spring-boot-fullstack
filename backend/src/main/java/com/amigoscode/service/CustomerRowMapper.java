package com.amigoscode.service;

import com.amigoscode.model.Customer;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CustomerRowMapper implements RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
       return new Customer(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("name"),
                rs.getInt("age")
        );
    }
}
