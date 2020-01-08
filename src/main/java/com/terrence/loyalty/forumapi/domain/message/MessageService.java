package com.terrence.loyalty.forumapi.domain.message;

import com.terrence.loyalty.forumapi.domain.location.Location;
import com.terrence.loyalty.forumapi.domain.location.LocationService;
import com.terrence.loyalty.forumapi.exception.ForumException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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
    private MessageAdapter messageAdapter;

    @Autowired
    private LocationService locationService;

    public MessageService(MessageRepository messageRepository, MessageAdapter messageAdapter, LocationService locationService) {
        this.messageRepository = messageRepository;
        this.messageAdapter = messageAdapter;
        this.locationService = locationService;
    }

    public List<MessageDto> getAllMessages() {
        List<MessageDto> parentMessageDtos = new ArrayList<>();
        parentMessageDtos.addAll(
                messageRepository.findAllByParentIdEqualsOrderByPostedDateDesc(0)
                        .stream()
                        .map(message -> messageAdapter.adapt(message))
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

        Location location = new Location();
        if (messageDto.getLocation() != null) {
            location = locationService.getLocation(messageDto.getLocation().getLocation());
        }

        Message savedMessage = messageRepository.save(new Message(messageDto.getUsername(), LocalDateTime.now(), messageDto.getComment(), location, messageDto.getParentId()));
        log.info("Saved new message: {}, From: {}", savedMessage.getComment(), savedMessage.getUsername());
        return messageAdapter.adapt(savedMessage);
    }

    public List<MessageDto> getMessagesByUsername(String username) {
        return messageRepository.findAllByUsernameOrderByPostedDateDesc(username)
                .stream()
                .map(message -> messageAdapter.adapt(message))
                .collect(Collectors.toList());
    }

    public MessageDto saveReply(MessageDto messageDto, long parentId) {
        if (parentId <= 0) {
            throw new ForumException(HttpStatus.BAD_REQUEST, "Invalid comment to reply");
        }

        messageDto.setParentId(parentId);
        MessageDto reply = save(messageDto);
        log.info("Saved new reply: {}, from {}, replying to Message {}", reply.getComment(), reply.getUsername(), reply.getParentId());
        return reply;
    }

    private List<MessageDto> findChildMessages(long parentId) {
        List<Message> childMessages = messageRepository.findAllByParentIdEqualsOrderByPostedDateDesc(parentId);
        if (childMessages == null) {
            return null;
        }

        List<MessageDto> childDtos = childMessages
                .stream()
                .map(message -> messageAdapter.adapt(message))
                .collect(Collectors.toList());

        for (MessageDto parent : childDtos) {
            parent.addChildMessages(findChildMessages(parent.getId()));
        }

        return childDtos;
    }

}
