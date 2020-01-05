package com.terrence.loyalty.forumapi.domain.message;

import com.terrence.loyalty.forumapi.exception.ForumException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        List<MessageDto> parentMessageDtos = new ArrayList<>();
        parentMessageDtos.addAll(
                messageRepository.findAllByParentIdEqualsOrderByPostedDateDesc(0)
                        .stream()
                        .map(message -> modelMapper.map(message, MessageDto.class))
                        .collect(Collectors.toList())
        );

        for (MessageDto parent : parentMessageDtos) {
            parent.addChildMessages(findChildMessages(parent.getId()));
        }

        return parentMessageDtos;
    }

    public MessageDto save(MessageDto messageDto) throws ForumException {
        if (messageDto.getUsername() == null || messageDto.getUsername().isEmpty()) {
            throw new ForumException(HttpStatus.BAD_REQUEST, "No username provided");
        }
        if (messageDto.getComment() == null || messageDto.getComment().isEmpty()) {
            throw new ForumException(HttpStatus.BAD_REQUEST, "No comment to post");
        }

        Message savedMessage = messageRepository.save(new Message(messageDto.getUsername(), LocalDateTime.now(), messageDto.getComment()));
        log.info("Saved new message: {}, From: {}", savedMessage.getComment(), savedMessage.getUsername());
        return modelMapper.map(savedMessage, MessageDto.class);
    }

    public List<MessageDto> getMessagesByUsername(String username) {
        return messageRepository.findAllByUsernameOrderByPostedDateDesc(username)
                .stream()
                .map(message -> modelMapper.map(message, MessageDto.class))
                .collect(Collectors.toList());
    }

    public MessageDto saveReply(MessageDto messageDto, long parentId) {
        if (parentId <= 0) {
            throw new ForumException(HttpStatus.BAD_REQUEST, "Invalid comment to reply");
        }
        if (messageDto.getUsername() == null || messageDto.getUsername().isEmpty()) {
            throw new ForumException(HttpStatus.BAD_REQUEST, "No username provided");
        }
        if (messageDto.getComment() == null || messageDto.getComment().isEmpty()) {
            throw new ForumException(HttpStatus.BAD_REQUEST, "No comment to post");
        }

        Message reply = messageRepository.save(new Message(messageDto.getUsername(), LocalDateTime.now(), messageDto.getComment(), parentId));
        log.info("Saved new reply: {}, from {}, replying to Message {}", reply.getComment(), reply.getUsername(), reply.getParentId());
        return modelMapper.map(reply, MessageDto.class);
    }

    private List<MessageDto> findChildMessages(long parentId) {
        List<Message> childMessages = messageRepository.findAllByParentIdEqualsOrderByPostedDateDesc(parentId);
        if (childMessages == null) {
            return null;
        }

        List<MessageDto> childDtos = childMessages
                .stream()
                .map(message -> modelMapper.map(message, MessageDto.class))
                .collect(Collectors.toList());

        for (MessageDto parent : childDtos) {
            parent.addChildMessages(findChildMessages(parent.getId()));
        }

        return childDtos;
    }


    private List<MessageDto> findChildMessages(String username, long parentId) {
        List<Message> childMessages = messageRepository.findAllByUsernameAndParentIdOrderByPostedDateDesc(username, parentId);
        if (childMessages == null) {
            return null;
        }

        List<MessageDto> childDtos = childMessages
                .stream()
                .map(message -> modelMapper.map(message, MessageDto.class))
                .collect(Collectors.toList());

        for (MessageDto parent : childDtos) {
            parent.addChildMessages(findChildMessages(username, parent.getId()));
        }

        return childDtos;
    }
}
