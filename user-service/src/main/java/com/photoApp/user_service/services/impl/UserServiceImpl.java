package com.photoApp.user_service.services.impl;

import com.photoApp.user_service.dtos.UserDto;
import com.photoApp.user_service.entity.AuthUser;
import com.photoApp.user_service.repository.UserRepository;
import com.photoApp.user_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        AuthUser user = modelMapper.map(userDto, AuthUser.class);

        userRepository.save(user);

        UserDto dto = modelMapper.map(user, UserDto.class);

        return dto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        AuthUser user = userRepository.findByEmail(email);

        if(user == null) {
            throw new UsernameNotFoundException(email);
        }

        return new ModelMapper().map(user, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(user.getEmail(), user.getEncryptedPassword(), new ArrayList<>());
    }
}
