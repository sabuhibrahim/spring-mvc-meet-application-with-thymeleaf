package com.meet.meet.services;

import org.springframework.data.domain.Page;

import com.meet.meet.dtos.GroupDto;
import com.meet.meet.exceptions.NotFoundException;

public interface GroupService {
    Page<GroupDto> findAll(int pageNo, int pageSize);

    Page<GroupDto> search(String query, int pageNo, int pageSize);

    GroupDto create(GroupDto group);
    
    GroupDto findById(long id) throws NotFoundException;

    void update(GroupDto group) throws NotFoundException;

    void delete(long id) throws NotFoundException;
}
