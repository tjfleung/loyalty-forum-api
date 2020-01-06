package com.terrence.loyalty.forumapi.controller;

import com.terrence.loyalty.forumapi.domain.message.MessageDto;
import com.terrence.loyalty.forumapi.domain.message.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@Slf4j
@CrossOrigin("*")
public class MessageController {

    @Autowired
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<MessageDto> getAllMessages() {
        log.info("Getting all messages");
        return messageService.getAllMessages();
    }

    @PostMapping
    @Transactional
    public MessageDto postMessage(@RequestBody MessageDto messageDto) {
        log.info("Posting new message");
        return messageService.save(messageDto);
    }

    @GetMapping("/{username}")
    @Transactional(readOnly = true)
    public List<MessageDto> getMessagesByUsername(@PathVariable String username) {
        log.info("Getting messages for username: {}", username);
        return messageService.getMessagesByUsername(username);
    }

    @PostMapping("/{id}")
    @Transactional
    public MessageDto postReply(@PathVariable long id, @RequestBody MessageDto messageDto) {
        log.info("Posting new reply");
        return messageService.saveReply(messageDto, id);
    }
}
