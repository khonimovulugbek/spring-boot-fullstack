package com.amigoscode.model.dto;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
