package com.pollhub.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(Long userId, String firstName, String lastName, String email, Set<String> roles,
                           LocalDateTime createdAt) {
}
