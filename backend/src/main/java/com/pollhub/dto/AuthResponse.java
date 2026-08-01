package com.pollhub.dto;

import java.util.Set;

public record AuthResponse(String token, String tokenType, Long userId, String firstName, String lastName,
                           String email, Set<String> roles) {
}
