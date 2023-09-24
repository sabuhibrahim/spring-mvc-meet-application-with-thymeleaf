package com.meet.meet.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String fullName;
    private LocalDateTime joinedAt;
    private LocalDateTime updatedAt;
}
