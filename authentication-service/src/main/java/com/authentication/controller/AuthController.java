package com.authentication.controller;

import com.authentication.dto.login.UserLoginRequest;
import com.authentication.dto.register.UserRegisterRequest;
import com.authentication.records.login.LoginResponse;
import com.authentication.records.register.RegisterResponse;
import com.authentication.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);
    //register user
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody UserRegisterRequest request){
        logger.info("Register request received");
        return ResponseEntity.status(HttpStatus.OK).body(userService.registerUser(request));
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginRequest request){
        logger.info("Login request received");
        return ResponseEntity.status(HttpStatus.OK).body(userService.loginUser(request));
    }
}
