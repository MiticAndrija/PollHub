package com.pollhub.dto;
import jakarta.validation.constraints.NotNull;
public record VoteRequest(@NotNull Long optionId) {}
