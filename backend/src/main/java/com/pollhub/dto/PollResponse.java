package com.pollhub.dto;
import java.time.LocalDateTime;
import java.util.List;
public record PollResponse(Long id,String title,String description,boolean active,LocalDateTime createdAt,
 LocalDateTime expiresAt,Long creatorId,String creatorName,Long categoryId,String categoryName,
 List<PollOptionResponse> options,long totalVotes) {}
