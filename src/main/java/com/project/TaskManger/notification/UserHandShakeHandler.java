package com.project.TaskManger.notification;

import com.project.TaskManger.exception.NotFoundException;
import com.project.TaskManger.security.config.JwtService;
import com.project.TaskManger.security.user.UserRepository;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
@RequiredArgsConstructor

public class UserHandShakeHandler extends DefaultHandshakeHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(token!=null)
        token = token.substring(7);
        String email = jwtService.extractUsername(token);
        Integer id = userRepository.
                findByEmail(email).orElseThrow(() ->
                new NotFoundException("user with email [%s] not found".formatted(email))).
                getId();
        attributes.put("id",id);
        return new UserPrincipal(id.toString());
    }
}
