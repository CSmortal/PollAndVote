package com.joven.poller.service;

import com.joven.poller.Role;
import com.joven.poller.authObject.AuthenticationRequest;
import com.joven.poller.authObject.AuthenticationResponse;
import com.joven.poller.authObject.RegisterRequest;
import com.joven.poller.authObject.RegisterResponse;
import com.joven.poller.entity.User;
import com.joven.poller.jwt.JwtService;
import com.joven.poller.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public RegisterResponse register(RegisterRequest request) {
        User user = User.builder()
                .userName(request.getName())
                .email(request.getEmail()) // altho this column should be unique, we dont proactively check the database if there is a duplicate value, because we might have race conditions (there could be another register request of the same email that has not yet been saved)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        try {
            userRepository.save(user);
            String jwtToken = jwtService.generateToken(user);

            return RegisterResponse.builder()
                    .token(jwtToken)
                    .success(true)
                    .build();

        } catch (Exception e) {
            String errorMessage;
            if (e.getClass().equals(DataIntegrityViolationException.class)) {
                errorMessage = "Email has already been registered";
            } else {
                errorMessage = e.getMessage();
            }
            System.out.println(e.getMessage());

            return RegisterResponse.builder()
                    .success(false)
                    .errorMessage(errorMessage)
                    .build();
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            Authentication result = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(result);
            UserDetails userDetails = userRepository.findByEmail(request.getEmail()).orElseThrow();
            String token = jwtService.generateToken(userDetails);

            return AuthenticationResponse.builder()
                    .token(token)
                    .success(true)
                    .build();
        } catch (AuthenticationException e) {
            return AuthenticationResponse.builder()
                    .success(false)
                    .build();
        }

    }
}
