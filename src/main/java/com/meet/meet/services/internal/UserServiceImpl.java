package com.meet.meet.services.internal;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.meet.meet.dtos.RegistrationDto;
import com.meet.meet.dtos.UserDto;
import com.meet.meet.mappers.UserMapper;
import com.meet.meet.models.Role;
import com.meet.meet.models.UserEntity;
import com.meet.meet.repositories.RoleRepository;
import com.meet.meet.repositories.UserRepository;
import com.meet.meet.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerUser(RegistrationDto registerDto) {
        UserEntity user = new UserEntity();

        user.setEmail(registerDto.getEmail());
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role role = roleRepository.findByName("USER");

        user.setRoles(Arrays.asList(role));

        userRepository.saveAndFlush(user);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
}
