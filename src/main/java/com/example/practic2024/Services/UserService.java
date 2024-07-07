package com.example.practic2024.Services;

import com.example.practic2024.Models.UserEntity;
import com.example.practic2024.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByLogin(username);

        if(user == null){
            throw  new UsernameNotFoundException("User not found");
        }

        return User.builder().username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();

    }
}
