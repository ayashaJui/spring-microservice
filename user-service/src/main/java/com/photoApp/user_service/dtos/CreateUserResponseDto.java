package com.photoApp.user_service.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateUserResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String userId;
}
