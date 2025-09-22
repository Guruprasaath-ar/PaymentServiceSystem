package dev.guru.UserService.service;
import dev.guru.UserService.domain.UserEntity;
import dev.guru.UserService.dto.AuthRequest;
import dev.guru.UserService.dto.UserRequest;
import dev.guru.UserService.dto.UserResponse;
import dev.guru.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
    }

    private UserEntity convertUserRequestToUserEntity(UserRequest userRequest) {
        String hashedPassword = bCryptPasswordEncoder.encode(userRequest.getPassword());
        return new UserEntity
                .Builder(userRequest.getUsername(), userRequest.getEmail(), hashedPassword)
                .withVerified(false)
                .build();
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public ResponseEntity<UserResponse> registerUser(UserRequest userRequest) {
        UserResponse userResponse;
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            userResponse = new UserResponse
                    .Builder()
                    .withResult(false)
                    .withMessage("Account already exists with this email")
                    .build();

            return ResponseEntity.ok(userResponse);
        } else if (userRepository.findByUserName(userRequest.getUsername()).isPresent()) {
            userResponse = new UserResponse
                    .Builder()
                    .withResult(false)
                    .withMessage("Username already exists with this email")
                    .build();

            return ResponseEntity.ok(userResponse);
        }

        return saveUser(userRequest);
    }

    public Authentication AuthenticateUser(AuthRequest authRequest) {
        return authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken
                        (authRequest.getEmail(), authRequest.getPassword()));

    }

    public ResponseEntity<UserResponse> saveUser(UserRequest userRequest) {
        UserEntity userEntity = convertUserRequestToUserEntity(userRequest);
        userRepository.save(userEntity);
        UserResponse userResponse = new UserResponse
                .Builder().withResult(true)
                .withMessage("User registered successfully!")
                .build();
        return ResponseEntity.ok(userResponse);
    }

    public ResponseEntity<UserResponse> updateUser(UserRequest userRequest,UserEntity userEntity) {
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
        userEntity.setUserName(userRequest.getUsername());
        userRepository.save(userEntity);
        UserResponse userResponse = new UserResponse
                .Builder().withResult(true)
                .withMessage("User updated successfully!")
                .build();
        return ResponseEntity.ok(userResponse);
    }

    public ResponseEntity<UserResponse> deleteUser(UserRequest userRequest,UserEntity userEntity) {
        userRepository.delete(userEntity);
        UserResponse userResponse = new UserResponse.Builder().withResult(true)
                .withMessage("User deleted successfully!")
                .build();
        return ResponseEntity.ok(userResponse);
    }
}
