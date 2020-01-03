package com.terrence.loyalty.forumapi.domain.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MessageService {

    @Autowired
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Collection<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public Collection<Message> getMessagesByUsername(String username) {
        return messageRepository.findAllByUsernameOrderByPostedDateDesc(username);
    }
}
