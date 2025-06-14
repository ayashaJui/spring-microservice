package com.photoApp.user_service.dtos;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 4009718385243635939L;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userId;
    private String encryptedPassword;
}
