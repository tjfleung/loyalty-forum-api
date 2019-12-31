package com.terrence.loyalty.forumapi.domain.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;

@Service
public class MessageService {

    @Autowired
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional(readOnly = true)
    public Collection<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Transactional
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public Collection<Message> getMessagesByUsername(String username) {
        return messageRepository.findMessagesByUsername(username);
    }
}
