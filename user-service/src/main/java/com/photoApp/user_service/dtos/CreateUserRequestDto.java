package com.photoApp.user_service.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateUserRequestDto {
    @NotNull(message = "firstName can not be null")
    @Size(min = 2, message = "First Name must be greater than 2 characters")
    private String firstName;
    @NotNull(message = "lastName can not be null")
    @Size(min = 2, message = "Last Name must be greater than 2 characters")
    private String lastName;
    @NotNull(message = "email can not be null")
    @Email
    private String email;
    @NotNull(message = "password can not be null")
    @Size(min = 2, max = 8, message = "Password must be between 2 & 8 characters")
    private String password;
}
