package com.amigoscode.model.dto;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
