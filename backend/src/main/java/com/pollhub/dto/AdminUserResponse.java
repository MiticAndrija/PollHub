package com.pollhub.dto;
import java.time.LocalDateTime;
import java.util.Set;
public record AdminUserResponse(Long id,String firstName,String lastName,String email,boolean enabled,
 Set<String> roles,LocalDateTime createdAt) {}
