package com.meet.meet.mappers;

import java.util.stream.Collectors;

import com.meet.meet.dtos.EventDto;
import com.meet.meet.models.Event;

public class EventMapper {
    
    public static EventDto mapToDto(
        Event event
    ) {
        return mapToDto(event, false, false, false);
    }

    public static EventDto mapToDto(
        Event event, boolean includeGroup, boolean includeParticipaters, boolean includeOwner
    ) {
        var builder = EventDto.builder()
                       .id(event.getId())
                       .title(event.getTitle())
                       .description(event.getDescription())
                       .photo(event.getPhoto())
                       .startTime(event.getStartTime())
                       .endTime(event.getEndTime())
                       .createdAt(event.getCreatedAt())
                       .updatedAt(event.getUpdatedAt());
        if(includeGroup) {
            builder = builder.group(
                includeOwner ?
                GroupMapper.mapToDto(event.getGroup(), true, false, false) :
                GroupMapper.mapToDto(event.getGroup())
            );
        }
        if(includeParticipaters) {
            builder = builder.participaters(
                event.getParticipaters()
                     .stream()
                     .map(user -> UserMapper.mapToUserDto(user))
                     .collect(Collectors.toList())
            );
        }
        return builder.build();
    }


    public static Event mapToModel(
        EventDto event
    ) {
        return mapToModel(event, false, false, false);
    }

    public static Event mapToModel(
        EventDto eventDto, boolean includeGroup, boolean includeParticipaters, boolean includeOwner
    ) {
        var builder = Event.builder()
                       .id(eventDto.getId())
                       .title(eventDto.getTitle())
                       .description(eventDto.getDescription())
                       .photo(eventDto.getPhoto())
                       .startTime(eventDto.getStartTime())
                       .endTime(eventDto.getEndTime())
                       .createdAt(eventDto.getCreatedAt())
                       .updatedAt(eventDto.getUpdatedAt());
        if(includeGroup) {
            builder = builder.group(
                includeOwner ?
                GroupMapper.mapToModel(eventDto.getGroup(), true, false, false) :
                GroupMapper.mapToModel(eventDto.getGroup())
            );
        }
        if(includeParticipaters) {
            builder = builder.participaters(
                eventDto.getParticipaters()
                     .stream()
                     .map(user -> UserMapper.mapToUser(user))
                     .collect(Collectors.toList())
            );
        }
        return builder.build();
    }
}
