package com.meet.meet.controllers;

import com.meet.meet.dtos.UserDto;
import com.meet.meet.mappers.UserMapper;
import com.meet.meet.models.UserEntity;
import com.meet.meet.security.SecurityUtil;
import com.meet.meet.services.UserService;

public abstract class AbstractController {
    
    public UserEntity getCurrentUser(UserService userService) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user;
        if (username != null) {
            user = userService.findByUsername(username);
        } else {
            user = new UserEntity();
        }

        return user;
    }

    public UserDto getCurrentDtoUser(UserService userService) {
        return UserMapper.mapToUserDto(getCurrentUser(userService));
    }
}
