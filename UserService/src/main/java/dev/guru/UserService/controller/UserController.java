package dev.guru.UserService.controller;

import dev.guru.UserService.domain.UserEntity;
import dev.guru.UserService.dto.AuthRequest;
import dev.guru.UserService.dto.AuthResponse;
import dev.guru.UserService.dto.UserRequest;
import dev.guru.UserService.dto.UserResponse;
import dev.guru.UserService.service.UserService;
import dev.guru.UserService.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;
    private JwtUtil jwtUtil;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest user){
        return userService.registerUser(user);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody AuthRequest auth, HttpServletResponse httpResponse) throws Exception {
        Authentication response = userService.AuthenticateUser(auth);

        if(!response.isAuthenticated()){
            AuthResponse authResponse = new AuthResponse
                    .Builder()
                    .withAuthenticated(false)
                    .withMessage("Authentication Failed")
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }

        String token = jwtUtil.generateJwtToken(auth.getEmail());

        ResponseCookie cookie = ResponseCookie.from("JWT_Token",token)
                .httpOnly(true)
                .sameSite("strict")
                .maxAge(jwtUtil.getAccessTokenExpireInSeconds())
                .path("/")
                .secure(true)
                .build();

        AuthResponse authResponse = new AuthResponse
                .Builder()
                .withAuthenticated(true)
                .withMessage("Successfully logged in")
                .withToken(token)
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);
    }

    @PutMapping("/users/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username,@Valid @RequestBody UserRequest user,Authentication authentication) throws Exception {

        String loggedInEmail  = authentication.getName();
        UserEntity userEntity = userService.findUserByEmail(loggedInEmail);

        if(userEntity.getUserName().equals(username))
            return userService.updateUser(user, userEntity);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Unauthorized"));
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username, @Valid @RequestBody UserRequest user, Authentication authentication){

        String loggedInEmail  = authentication.getName();
        UserEntity userEntity = userService.findUserByEmail(loggedInEmail);

        if(userEntity.getUserName().equals(username))
            return userService.deleteUser(user, userEntity);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Unauthorized"));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("JWT_Token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("strict")
                .path("/")
                .maxAge(0)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }
}
