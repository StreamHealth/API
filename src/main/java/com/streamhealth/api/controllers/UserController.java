package com.streamhealth.api.controllers;

import com.streamhealth.api.dtos.UserDataDto;
import com.streamhealth.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/v1/user/get_user")
    public ResponseEntity <UserDataDto> getUser() {
        UserDataDto userData = userService.getUserData();
        return ResponseEntity.ok(userData);
    }
}
