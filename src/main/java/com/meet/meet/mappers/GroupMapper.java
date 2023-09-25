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
                              .description(group.getDescription())
                              .photo(group.getPhoto())
                              .createdAt(group.getCreatedAt())
                              .updatedAt(group.getUpdatedAt());
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


    public static Group mapToModel(
        GroupDto group
    ) {
        return mapToModel(group, false, false, false);
    }
    public static Group mapToModel(
        GroupDto groupDto, 
        boolean includeOwner, 
        boolean includeSubscribers,
        boolean includeEvents
    ) {
        var builder = Group.builder()
                              .id(groupDto.getId())
                              .title(groupDto.getTitle())
                              .description(groupDto.getDescription())
                              .photo(groupDto.getPhoto())
                              .createdAt(groupDto.getCreatedAt())
                              .updatedAt(groupDto.getUpdatedAt());
        if(includeOwner) {
            builder = builder.owner(
                UserMapper.mapToUser(groupDto.getOwner())
            );
        }

        if(includeSubscribers) {
            builder = builder.subscribers(
                groupDto.getSubscribers()
                     .stream()
                     .map(user -> UserMapper.mapToUser(user))
                     .collect(Collectors.toList())
            );
        }
        if(includeEvents) {
            builder = builder.events(
                groupDto.getEvents()
                     .stream()
                     .map(event -> EventMapper.mapToModel(event))
                     .collect(Collectors.toList())
            );
        }
        return builder.build();
    }
}
