package com.meet.meet.mappers;

import java.util.stream.Collectors;

import com.meet.meet.dtos.EventDto;
import com.meet.meet.models.Event;

public class EventMapper {
    
    public static EventDto mapToDto(
        Event event
    ) {
        return mapToDto(event, false, false);
    }

    public static EventDto mapToDto(
        Event event, boolean includeGroup, boolean includeParticipaters
    ) {
        var builder = EventDto.builder()
                       .id(event.getId())
                       .title(event.getTitle())
                       .description(event.getDescription())
                       .photo(event.getPhoto())
                       .starTime(event.getStartTime())
                       .endTime(event.getEndTime())
                       .createdAt(event.getCreatedAt())
                       .updatedAt(event.getUpdatedAt());
        if(includeGroup) {
            builder = builder.group(GroupMapper.mapToDto(event.getGroup()));
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
}
