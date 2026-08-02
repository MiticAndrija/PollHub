package com.pollhub.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
public record PollRequest(@NotBlank @Size(max=200) String title, @Size(max=2000) String description,
 @NotNull Long categoryId, @NotNull @Size(min=2,max=20) List<@NotBlank @Size(max=200) String> options,
 @Future LocalDateTime expiresAt) {}
