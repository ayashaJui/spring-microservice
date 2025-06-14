package com.photoApp.user_service.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserLoginRequestDto {
    @NotNull(message = "Email Address can not be null")
    @Email
    private String email;

    @NotNull(message = "Password can not be null")
    private String password;
}
