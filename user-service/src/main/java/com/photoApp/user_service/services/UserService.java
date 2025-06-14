package com.photoApp.user_service.services;

import com.photoApp.user_service.dtos.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByEmail(String email);
}
