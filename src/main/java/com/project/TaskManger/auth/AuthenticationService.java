package com.project.TaskManger.auth;

import com.project.TaskManger.config.JwtService;
import com.project.TaskManger.user.Role;
import com.project.TaskManger.user.User;
import com.project.TaskManger.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request){
        var user= User.builder().
                firstName(request.getFirstName()).
                lastName(request.getLastName()).
                email(request.getEmail()).
                password(passwordEncoder.encode(request.getPassword())).role(Role.USER).build();
        userRepository.save(user);
        var jwtToken=jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                        request.getPassword()));
        var user= userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken=jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}
