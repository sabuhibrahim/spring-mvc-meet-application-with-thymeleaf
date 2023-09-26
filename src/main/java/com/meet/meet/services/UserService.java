package com.meet.meet.services;

import com.meet.meet.dtos.RegistrationDto;
import com.meet.meet.dtos.UserDto;
import com.meet.meet.models.Event;
import com.meet.meet.models.Group;
import com.meet.meet.models.UserEntity;

public interface UserService {
    UserDto registerUser(RegistrationDto registerDto);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);

    void subscribe(UserEntity user, Group group);

    void unsubscribe(UserEntity user, Group group);

    void participate(UserEntity user, Event event);

    void unparticipate(UserEntity user, Event event);
}
