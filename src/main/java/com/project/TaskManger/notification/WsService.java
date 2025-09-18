package com.project.TaskManger.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WsService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void notifyGlobal(final String message) {
        ResponseMessage response = new ResponseMessage(message);

        simpMessagingTemplate.convertAndSend("/topic/messages", response);
    }
    public void notifyUser(Integer id,final String message) {
        ResponseMessage response = new ResponseMessage(message);

        simpMessagingTemplate.convertAndSendToUser(id.toString(),"/topic/private-messages", response);
    }
}
