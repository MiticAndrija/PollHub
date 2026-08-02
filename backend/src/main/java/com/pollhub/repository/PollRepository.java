package com.pollhub.repository;

import com.pollhub.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findByActiveTrue();

    List<Poll> findByCreatorId(Long creatorId);

    List<Poll> findByCategoryId(Long categoryId);

    @Query("select distinct p from Poll p left join p.options o left join o.votes v where " +
            "(:search is null or lower(p.title) like lower(concat('%', :search, '%'))) and " +
            "(:categoryId is null or p.category.id = :categoryId) and (:active is null or " +
            "(:active = true and p.active = true and (p.expiresAt is null or p.expiresAt > current_timestamp)) or " +
            "(:active = false and (p.active = false or p.expiresAt <= current_timestamp))) " +
            "group by p.id order by case when :sort = 'popular' then count(v) end desc, p.createdAt desc")
    List<Poll> search(@Param("search") String search, @Param("categoryId") Long categoryId,
                      @Param("active") Boolean active, @Param("sort") String sort);

    @Query("select count(p) from Poll p where p.active = true and (p.expiresAt is null or p.expiresAt > current_timestamp)")
    long countCurrentlyActive();
}
