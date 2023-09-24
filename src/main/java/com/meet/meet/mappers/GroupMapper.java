package com.meet.meet.mappers;

import java.util.stream.Collectors;

import com.meet.meet.dtos.GroupDto;
import com.meet.meet.models.Group;

public class GroupMapper {
    public static GroupDto mapToDto(
        Group group
    ) {
        return mapToDto(group, false, false, false);
    }
    public static GroupDto mapToDto(
        Group group, 
        boolean includeOwner, 
        boolean includeSubscribers,
        boolean includeEvents
    ) {
        var builder = GroupDto.builder()
                              .id(group.getId())
                              .title(group.getTitle())
                              .description(group.getTitle())
                              .photo(group.getPhoto())
                              .createdAt(group.getCreatedAt());
        if(includeOwner) {
            builder = builder.owner(
                UserMapper.mapToUserDto(group.getOwner())
            );
        }

        if(includeSubscribers) {
            builder = builder.subscribers(
                group.getSubscribers()
                     .stream()
                     .map(user -> UserMapper.mapToUserDto(user))
                     .collect(Collectors.toList())
            );
        }
        if(includeEvents) {
            builder = builder.events(
                group.getEvents()
                     .stream()
                     .map(event -> EventMapper.mapToDto(event))
                     .collect(Collectors.toList())
            );
        }
        return builder.build();
    }
}
