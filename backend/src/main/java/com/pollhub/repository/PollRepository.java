package com.pollhub.repository;

import com.pollhub.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findByActiveTrue();

    List<Poll> findByCreatorId(Long creatorId);

    List<Poll> findByCategoryId(Long categoryId);
}
