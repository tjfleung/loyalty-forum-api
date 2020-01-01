package com.terrence.loyalty.forumapi.rest.message;

import com.terrence.loyalty.forumapi.domain.message.Message;
import com.terrence.loyalty.forumapi.domain.message.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
@Slf4j
public class MessageController {

    @Autowired
    private final MessageService messageService;

    @Autowired
    private final ModelMapper modelMapper;

    public MessageController(MessageService messageService, ModelMapper modelMapper) {
        this.messageService = messageService;
        this.modelMapper = modelMapper;
    }

    @CrossOrigin
    @GetMapping
    public List<MessageDto> getMessages() {
        return messageService.getAllMessages().stream()
                .map(message -> modelMapper.map(message, MessageDto.class))
                .sorted((m1, m2) -> m2.getPostedDate().compareTo(m1.getPostedDate()))
                .collect(Collectors.toList());
    }

    @CrossOrigin
    @PostMapping
    public MessageDto postMessage(@RequestBody MessageDto messageDto) {
        if (messageDto.getUsername().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No username provided");
        }
        if (messageDto.getMessage().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No message to post");
        }

        Message message = new Message(messageDto.getUsername(), LocalDateTime.now(), messageDto.getMessage());
        log.info("New message {}, From: {}", message.getMessage(), message.getUsername());
        return modelMapper.map(messageService.save(message), MessageDto.class);
    }

    @CrossOrigin
    @GetMapping("/{username}")
    public List<MessageDto> getMessagesByUsername(@PathVariable String username) {
        log.info("Getting messages for username: {}", username);
        return messageService.getMessagesByUsername(username).stream()
                .map(message -> modelMapper.map(message, MessageDto.class))
                .collect(Collectors.toList());
    }
}
