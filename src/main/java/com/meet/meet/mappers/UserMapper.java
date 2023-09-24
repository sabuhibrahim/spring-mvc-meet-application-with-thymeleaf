package com.meet.meet.mappers;

import com.meet.meet.dtos.UserDto;
import com.meet.meet.models.UserEntity;

public class UserMapper {
    public static UserEntity mapToUser(UserDto userDto) {
        return UserEntity.builder()
                         .id(userDto.getId())
                         .email(userDto.getEmail())
                         .fullName(userDto.getFullName())
                         .username(userDto.getUsername())
                         .joinedAt(userDto.getJoinedAt())
                         .updatedAt(userDto.getUpdatedAt())
                         .build();

    }

    public static UserDto mapToUserDto(UserEntity user) {
        return UserDto.builder()
                         .id(user.getId())
                         .email(user.getEmail())
                         .fullName(user.getFullName())
                         .username(user.getUsername())
                         .joinedAt(user.getJoinedAt())
                         .updatedAt(user.getUpdatedAt())
                         .build();
                         
    }
}
