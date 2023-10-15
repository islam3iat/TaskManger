package com.project.TaskManger.notification;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;
import java.time.LocalTime;

@Controller
public class MessageController {

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ResponseMessage getMessage(Message message) throws InterruptedException {
        Thread.sleep(1000);
        return new ResponseMessage(HtmlUtils.htmlEscape(message.getMessageContent()));
    }
    @MessageMapping("/private-message")
    @SendToUser("/topic/private-messages")
    public ResponseMessage getPrivateMessage(final Message message, Principal principal) throws InterruptedException {
        Thread.sleep(1000);
        LocalTime now = LocalTime.now();
        return new ResponseMessage(HtmlUtils.htmlEscape("["+now+"] "+ principal.getName()+": "+ message.getMessageContent()));
    }
}