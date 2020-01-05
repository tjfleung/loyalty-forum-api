package com.terrence.loyalty.forumapi.domain.message;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MessageDto {

    private long id;
    private String username;
    private String comment;
    private LocalDateTime postedDate;
    private long parentId;
    private List<MessageDto> childMessages = new ArrayList<>();

    public MessageDto() {}

    public MessageDto(String username, LocalDateTime postedDate, String comment) {
        this.username = username;
        this.comment = comment;
        this.postedDate = postedDate;
    }

    public MessageDto(String username, LocalDateTime postedDate, String comment, long parentId) {
        this.username = username;
        this.comment = comment;
        this.postedDate = postedDate;
        this.parentId = parentId;
    }

    public MessageDto(String username, LocalDateTime postedDate, String comment, long parentId, List<MessageDto> childMessages) {
        this.username = username;
        this.comment = comment;
        this.postedDate = postedDate;
        this.parentId = parentId;
        this.childMessages = childMessages;
    }

    public void addChildMessage(MessageDto childMessage) {
        this.childMessages.add(childMessage);
    }

    public void addChildMessages(List<MessageDto> childMessages) {
        this.childMessages.addAll(childMessages);
    }

}
