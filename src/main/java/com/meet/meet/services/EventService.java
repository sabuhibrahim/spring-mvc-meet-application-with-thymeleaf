package com.meet.meet.services;

import org.springframework.data.domain.Page;

import com.meet.meet.dtos.EventDto;
import com.meet.meet.exceptions.NotFoundException;
import com.meet.meet.models.Event;

public interface EventService {
    
    Page<EventDto> findAll(int pageNo, int pageSize);

    Page<EventDto> findAll(int pageNo, int pageSize, Long groupId);

    EventDto create(EventDto event);
    
    EventDto findById(long id) throws NotFoundException;

    EventDto findByIdAndGroupId(long id, long groupId);

    Event findByIdModel(long id) throws NotFoundException;

    EventDto findByIdJoinParticipaters(long id) throws NotFoundException;

    void update(EventDto event) throws NotFoundException;

    void delete(EventDto event) throws NotFoundException;
}
