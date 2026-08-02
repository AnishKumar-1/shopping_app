package com.authentication.services;

import com.authentication.model.UserModel;
import com.authentication.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
     Optional<UserModel> user=userRepo.findByEmail(email);
     UserModel userModel=user.get();
     List<SimpleGrantedAuthority> authorities=
             userModel.getRoles().stream().map(role->new SimpleGrantedAuthority(role.getRoleName()))
                     .toList();
        return new User(userModel.getEmail(),userModel.getPassword(),authorities);
    }
}
