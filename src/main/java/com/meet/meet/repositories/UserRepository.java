package com.meet.meet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meet.meet.models.UserEntity;



public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    UserEntity findFirstByUsername(String username);
}
