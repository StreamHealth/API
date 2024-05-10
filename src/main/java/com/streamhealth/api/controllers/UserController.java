package com.streamhealth.api.controllers;

import com.streamhealth.api.dtos.UserDataDto;
import com.streamhealth.api.dtos.UserDto;
import com.streamhealth.api.entities.User;
import com.streamhealth.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity <UserDataDto> getUser() {
        UserDataDto userData = userService.getUserData();
        return ResponseEntity.ok(userData);
    }
}
