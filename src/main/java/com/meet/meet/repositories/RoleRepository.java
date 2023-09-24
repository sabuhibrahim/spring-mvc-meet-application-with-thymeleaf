package com.meet.meet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meet.meet.models.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
