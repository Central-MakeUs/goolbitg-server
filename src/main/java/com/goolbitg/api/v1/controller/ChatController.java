package com.goolbitg.api.v1.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.goolbitg.api.v1.dto.ChatMessageForm;
import com.goolbitg.api.v1.entity.chat.ChatMessage;
import com.goolbitg.api.v1.service.BuyOrNotService;
import com.goolbitg.api.v1.service.ChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final BuyOrNotService buyOrNotService;

    @MessageMapping("/chat/{roomId}")
    public void handle(@DestinationVariable("roomId") String roomId, @Payload ChatMessageForm message) {
        Long buyOrNotId = Long.valueOf(roomId);
        buyOrNotService.getBuyOrNot(buyOrNotId);

        ChatMessage chatMessage = 
            chatService.storeMessage(buyOrNotId, message.getUserId(), message.getContent());

        simpMessagingTemplate.convertAndSend("/topic/chat/" + roomId, chatMessage);
    }
}
