package com.project.TaskManager.config;

import com.project.TaskManager.notification.UserHandShakeHandler;
import com.project.TaskManager.security.config.JwtService;
import com.project.TaskManager.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

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
