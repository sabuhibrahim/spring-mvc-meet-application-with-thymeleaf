package com.meet.meet.services.internal;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.meet.meet.dtos.EventDto;
import com.meet.meet.exceptions.NotFoundException;
import com.meet.meet.mappers.EventMapper;
import com.meet.meet.models.Event;
import com.meet.meet.repositories.EventRepository;
import com.meet.meet.services.EventService;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Page<EventDto> findAll(int pageNo, int pageSize) {
        return findAll(pageNo, pageSize, null);
    }

    @Override
    public Page<EventDto> findAll(int pageNo, int pageSize, Long groupId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Event> events;
        if(groupId == null){
            events = eventRepository.findAll(pageable);
        } else {
            events = eventRepository.findByGroupId(groupId, pageable);
        }

        return events.map(
            event -> EventMapper.mapToDto(
                event, true, 
                false, false
            )
        );
    }

    @Override
    public EventDto create(EventDto eventDto) {
        Event event = EventMapper.mapToModel(eventDto, true, false, false);
        event = eventRepository.save(event);

        return EventMapper.mapToDto(event, true, false, false);
    }

    @Override
    public EventDto findById(long id) throws NotFoundException {
        Event event;
        try {
            event = eventRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            logger.debug("Event not found! Id: " + id);
            throw new NotFoundException("Event not found!"); 
        }

        return EventMapper.mapToDto(event, true, false, false);
    }

    @Override
    public EventDto findByIdAndGroupId(long id, long groupId) {
        Event event;
        try {
            event = eventRepository.findByIdAndGroupId(id, groupId).get();
        } catch (NoSuchElementException ex) {
            logger.debug("Event not found! Id: " + id);
            throw new NotFoundException("Event not found!"); 
        }

        return EventMapper.mapToDto(event, true, false, false);
    }

    @Override
    public Event findByIdModel(long id) throws NotFoundException {
        Event event;
        try {
            event = eventRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            logger.debug("Event not found! Id: " + id);
            throw new NotFoundException("Event not found!"); 
        }

        return event;
    }

    @Override
    public EventDto findByIdJoinParticipaters(long id) throws NotFoundException {
        Event event;
        try {
            event = eventRepository.findByIdJoinParticipaters(id).get();
        }catch(NoSuchElementException ex) {
            logger.debug("Event not found! Id: " + id);
            throw new NotFoundException("Event not found!");
        }
        return EventMapper.mapToDto(event, true, true, true);
    }

    @Override
    public void update(EventDto eventDto) throws NotFoundException {
        Event event = EventMapper.mapToModel(eventDto, true, false, false);
        eventRepository.save(event);
    }

    @Override
    public void delete(EventDto event) throws NotFoundException {
        eventRepository.delete(
            EventMapper.mapToModel(event, true, false, false)
        );
    }
    
}
