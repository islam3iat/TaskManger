package com.project.TaskManger.notification;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Tag(name = "WebSocket")
public class WsController {
    private  WsService wsService;
    @PostMapping("/send-message")
    public void sendMessage(@RequestBody Message message){
        wsService.notifyGlobal(message.getMessageContent());
    }
    @PostMapping("/send-private-message/{user_id}")
    public void sendPrivateMessage(@PathVariable("user_id")Integer id, @RequestBody Message message){
        wsService.notifyUser(id,message.getMessageContent());
    }
}
