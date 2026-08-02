package com.pollhub.repository;

import com.pollhub.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByUserIdAndPollId(Long userId, Long pollId);

    long countByPollOptionId(Long pollOptionId);
    long countByPollId(Long pollId);
}
