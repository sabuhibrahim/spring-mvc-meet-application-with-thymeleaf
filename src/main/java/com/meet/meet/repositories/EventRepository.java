package com.meet.meet.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.meet.meet.models.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e JOIN e.group")
    Page<Event> findAll(Pageable pageable);

    @Query("SELECT e FROM Event e JOIN e.group WHERE e.group.id = :groupId")
    Page<Event> findByGroupId(Long groupId, Pageable pageable);

    @Query("SELECT e FROM Event e JOIN e.group WHERE e.id = :id")
    Optional<Event> findById(long id);

    @Query("SELECT e FROM Event e JOIN e.group WHERE e.id = :id and e.group.id = :groupId")
    Optional<Event> findByIdAndGroupId(long id, long groupId);

    @Query("SELECT e FROM Event e LEFT JOIN e.participaters JOIN e.group JOIN e.group.owner WHERE e.id = :id")
    Optional<Event> findByIdJoinParticipaters(Long id);
}
