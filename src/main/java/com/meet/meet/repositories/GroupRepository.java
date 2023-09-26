package com.meet.meet.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.meet.meet.models.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query(
        "SELECT g FROM Group g JOIN g.owner WHERE g.title ILIKE CONCAT('%', :query, '%')"
        + " OR g.description ILIKE CONCAT('%', :query, '%')"
    )
    Page<Group> search(String query, Pageable pageable);

    @Query("SELECT g FROM Group g JOIN g.owner")
    Page<Group> findAll(Pageable pageable);

    @Query("SELECT g FROM Group g JOIN g.owner WHERE g.id = :id")
    Optional<Group> findById(long id);

    @Query("SELECT g FROM Group g LEFT JOIN g.events JOIN g.owner WHERE g.id = :id")
    Optional<Group> findByIdJoinEvents(Long id);

    @Query("SELECT g FROM Group g LEFT JOIN g.subscribers JOIN g.owner WHERE g.id = :id")
    Optional<Group> findByIdJoinSubscribers(Long id);
}
