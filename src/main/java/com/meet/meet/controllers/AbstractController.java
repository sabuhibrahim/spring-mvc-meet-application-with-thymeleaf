package com.meet.meet.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import com.meet.meet.models.UserEntity;
import com.meet.meet.security.SecurityUtil;
import com.meet.meet.services.UserService;

public abstract class AbstractController {
    
    public UserEntity getCurrentUser(UserService userService) {
        UserEntity user;
        String username = SecurityUtil.getSessionUser();

        if (username != null) {
            user = userService.findByUsername(username);
        } else {
            user = new UserEntity();
        }

        return user;
    }
}
