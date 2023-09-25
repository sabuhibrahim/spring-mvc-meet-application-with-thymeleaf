package com.meet.meet.dtos;

import com.meet.meet.validators.PasswordMatch;
import com.meet.meet.validators.ValidPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PasswordMatch
public class RegistrationDto {
    
    @NotEmpty
    private String username;
    
    @Email
    @NotEmpty
    private String email;

    private String fullName;

    @NotEmpty
    // @ValidPassword
    private String password;

    @NotEmpty
    private String confirmPassword;
}
