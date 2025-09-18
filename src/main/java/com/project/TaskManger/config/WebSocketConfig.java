package com.project.TaskManger.config;

import com.project.TaskManger.notification.MyHandler;
import com.project.TaskManger.notification.UserHandShakeHandler;
import com.project.TaskManger.security.config.JwtService;
import com.project.TaskManger.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/ws");
        config.enableSimpleBroker("/topic");
    }
    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/our-websocket").
                setHandshakeHandler(new UserHandShakeHandler(jwtService,userRepository)).
                withSockJS();
    }
}
