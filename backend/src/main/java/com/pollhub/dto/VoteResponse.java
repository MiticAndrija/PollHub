package com.pollhub.dto;
import java.time.LocalDateTime;
public record VoteResponse(Long id,Long pollId,Long optionId,LocalDateTime createdAt) {}
