package com.meet.meet.services.internal;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.meet.meet.dtos.GroupDto;
import com.meet.meet.exceptions.NotFoundException;
import com.meet.meet.mappers.GroupMapper;
import com.meet.meet.models.Group;
import com.meet.meet.models.UserEntity;
import com.meet.meet.repositories.GroupRepository;
import com.meet.meet.services.GroupService;

@Service
public class GroupServiceImpl implements GroupService {
    
    @Autowired
    private GroupRepository groupRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Page<GroupDto> findAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Group> groups = groupRepository.findAll(pageable);
        return groups.map(group -> GroupMapper.mapToDto(group, true, false, false));
    }

    @Override
    public Page<GroupDto> search(String query, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Group> groups = groupRepository.search(query, pageable);
        return groups.map(group -> GroupMapper.mapToDto(
            group, true, false, false
        ));
    }

    @Override
    public GroupDto create(GroupDto groupDto) {
        Group group = GroupMapper.mapToModel(groupDto, true, false, false);

        group = groupRepository.save(group);
        return GroupMapper.mapToDto(group, true, false, false);
    }

    @Override
    public GroupDto findById(long id) throws NotFoundException {
        Group group;
        try {
            group = groupRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            logger.debug("Group not found! Id: " + id);
            throw new NotFoundException("Group not found!");
        }

        return GroupMapper.mapToDto(group, true, false, false);
    }

    @Override
    public Group findByIdModel(long id) throws NotFoundException {
        Group group;
        try {
            group = groupRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            logger.debug("Group not found! Id: " + id);
            throw new NotFoundException("Group not found!");
        }

        return group;
    }

    @Override
    public GroupDto findByIdJoinEvents(long id) throws NotFoundException {
        Group group;
        try {
            group = groupRepository.findByIdJoinEvents(id).get();
        } catch (NoSuchElementException ex) {
            logger.debug("Group not found! Id: " + id);
            throw new NotFoundException("Group not found!");
        }

        return GroupMapper.mapToDto(group, true, false, true);
    }

    @Override
    public GroupDto findByIdJoinSubscribers(long id) throws NotFoundException {
        Group group;
        try {
            group = groupRepository.findByIdJoinSubscribers(id).get();
        } catch (NoSuchElementException ex) {
            logger.debug("Group not found! Id: " + id);
            throw new NotFoundException("Group not found!");
        }

        return GroupMapper.mapToDto(group, true, true, false);
    }

    @Override
    public void update(GroupDto groupDto) throws NotFoundException {
        Group group = GroupMapper.mapToModel(groupDto, true, false, false);
        groupRepository.save(group);
    }

    @Override
    public void delete(GroupDto group) throws NotFoundException {
        groupRepository.delete(GroupMapper.mapToModel(group, true, false, false));
    }

}
