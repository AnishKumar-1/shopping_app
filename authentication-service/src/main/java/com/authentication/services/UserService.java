package com.authentication.services;

import com.authentication.Jwt.JwtUtils;
import com.authentication.dto.login.UserLoginRequest;
import com.authentication.dto.register.UserRegisterRequest;
import com.authentication.exception.UserNotFound;
import com.authentication.model.UserModel;
import com.authentication.model.UserRoles;
import com.authentication.records.login.LoginResponse;
import com.authentication.records.register.RegisterResponse;
import com.authentication.repository.RoleRepo;
import com.authentication.repository.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RoleRepo roleRepo;


    public UserService(PasswordEncoder passwordEncoder,
                       UserRepo userRepo,AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils,
                       RoleRepo roleRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.authenticationManager=authenticationManager;
        this.jwtUtils=jwtUtils;
        this.roleRepo=roleRepo;
    }

    //register user
    // register user
    public RegisterResponse registerUser(UserRegisterRequest userRegisterRequest) {

        if (userRepo.existsByEmail(userRegisterRequest.getEmail())) {

            throw new IllegalArgumentException("You are already registered with this account: " + userRegisterRequest.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(userRegisterRequest.getCreatePassword());

        // 1. Fetch seeded default role
        UserRoles userRole = roleRepo.findByRoleName("ROLE_USER");

        // 2. Build user with default role
        UserModel register = UserModel.builder()
                .fullName(userRegisterRequest.getFullName())
                .email(userRegisterRequest.getEmail())
                .password(encodedPassword)
                .roles(Set.of(userRole)) // Set default role
                .build();

        userRepo.save(register);
        return new RegisterResponse("Registered Successfully.");
    }

    //login user

    public LoginResponse loginUser(UserLoginRequest request){
        Optional<UserModel> user=userRepo.findByEmail(request.getEmail());
        if(!user.isPresent()){
            throw new UserNotFound("User Not Found with this email: "+ request.getEmail());
        }
// 1. Delegate authentication & password check to AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserModel userModel=user.get();


        // 2. If valid, generate JWT Token
        String token = jwtUtils.generateToken(authentication);
        // 5. Return complete LoginResponse
        return new LoginResponse(
                userModel.getId(),
                userModel.getFullName(),
                "Login successful",
                token
        );
    }

}
