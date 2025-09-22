package dev.guru.UserService.service;

import dev.guru.UserService.domain.UserEntity;

import dev.guru.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserEntityDetailsService implements UserDetailsService {

    private UserRepository userRepository;


    public UserEntityDetailsService() {
    }

    @Autowired
    public void setUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new User(entity.getEmail(),entity.getPassword(),new ArrayList<GrantedAuthority>());
    }
}
