package com.terrence.loyalty.forumapi.domain.message;

import org.springframework.stereotype.Component;

@Component
public class MessageAdapter {

    public MessageDto adapt(Message message) {
        MessageDto messageDto = new MessageDto();

        messageDto.setId(message.getId());
        messageDto.setUsername(message.getUsername());
        messageDto.setPostedDate(message.getPostedDate());
        messageDto.setComment(message.getComment());
        messageDto.setParentId(message.getParentId());
        messageDto.setLocation(message.getLocation());

        return messageDto;
    }
}
