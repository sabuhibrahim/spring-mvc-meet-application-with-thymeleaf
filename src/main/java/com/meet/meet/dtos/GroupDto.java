package com.meet.meet.dtos;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupDto {
    private long id;
    @NotNull
    private String title;

    private String description;
    private String photo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserDto owner;

    private List<UserDto> subscribers;

    private List<EventDto> events;
}
