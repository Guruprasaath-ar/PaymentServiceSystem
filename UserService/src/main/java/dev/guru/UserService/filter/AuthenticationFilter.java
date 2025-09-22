package dev.guru.UserService.filter;

import dev.guru.UserService.service.UserEntityDetailsService;
import dev.guru.UserService.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private UserEntityDetailsService userEntityDetailsService;

    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    public void setUserEntityDetailsService(UserEntityDetailsService userEntityDetailsService) {
        this.userEntityDetailsService = userEntityDetailsService;
    }

    List<String> validPaths = new ArrayList<>(List.of("/api/auth/register","/api/auth/login","/api/auth/logout"));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(validPaths.contains(request.getRequestURI())){
            filterChain.doFilter(request,response);
            return;
        }

        String jwt = null;
        String email = null;

        String authorization = request.getHeader("Authorization");
        if(authorization != null && authorization.startsWith("Bearer ")){
            jwt = authorization.substring(7);
        }

        if(jwt == null){
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for(Cookie cookie : cookies){
                    if(!cookie.getName().equals("JWT_Token"))
                        continue;

                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        if(jwt != null){
            email = jwtUtil.getSubjectFromToken(jwt);
            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                if(jwtUtil.validateToken(jwt,email)){
                    UserDetails userDetails = userEntityDetailsService.loadUserByUsername(email);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request,response);
    }

}
