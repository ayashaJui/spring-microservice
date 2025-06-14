package com.photoApp.user_service.controllers;

import com.photoApp.user_service.dtos.CreateUserRequestDto;
import com.photoApp.user_service.dtos.CreateUserResponseDto;
import com.photoApp.user_service.dtos.UserDto;
import com.photoApp.user_service.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;

    @GetMapping("/status")
    public String status() {
        return "Working";
    }

    @PostMapping
    public ResponseEntity<CreateUserResponseDto> createUser(@Valid @RequestBody CreateUserRequestDto userRequestModel){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(userRequestModel, UserDto.class);
        UserDto user = userService.createUser(userDto);

        CreateUserResponseDto responseDto = modelMapper.map(user, CreateUserResponseDto.class);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
