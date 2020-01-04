package com.terrence.loyalty.forumapi.domain.message;

import com.terrence.loyalty.forumapi.exception.ForumException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageService {

    @Autowired
    private final MessageRepository messageRepository;

    @Autowired
    private final ModelMapper modelMapper;

    public MessageService(MessageRepository messageRepository, ModelMapper modelMapper) {
        this.messageRepository = messageRepository;
        this.modelMapper = modelMapper;
    }

    public List<MessageDto> getAllMessages() {
        return messageRepository.findAll(Sort.by(Sort.Direction.DESC, "postedDate"))
                .stream()
                .map(message -> modelMapper.map(message, MessageDto.class))
                .collect(Collectors.toList());
    }

    public MessageDto save(MessageDto messageDto) throws ForumException {
        if (messageDto.getUsername() == null || messageDto.getUsername().isEmpty()) {
            throw new ForumException(HttpStatus.BAD_REQUEST, "No username provided");
        }
        if (messageDto.getMessage() == null || messageDto.getMessage().isEmpty()) {
            throw new ForumException(HttpStatus.BAD_REQUEST, "No message to post");
        }

        Message savedMessage = messageRepository.save(new Message(messageDto.getUsername(), LocalDateTime.now(), messageDto.getMessage()));
        log.info("Saved new message: {}, From: {}", savedMessage.getMessage(), savedMessage.getUsername());
        return modelMapper.map(savedMessage, MessageDto.class);
    }

    public List<MessageDto> getMessagesByUsername(String username) {
        return messageRepository.findAllByUsernameOrderByPostedDateDesc(username)
                .stream()
                .map(message -> modelMapper.map(message, MessageDto.class))
                .collect(Collectors.toList());
    }
}
