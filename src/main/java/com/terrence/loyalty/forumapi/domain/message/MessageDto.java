package com.terrence.loyalty.forumapi.domain.message;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDto {

    private long id;
    private String username;
    private String comment;
    private LocalDateTime postedDate;

    public MessageDto() {}

    public MessageDto(String username, LocalDateTime postedDate, String comment) {
        this.username = username;
        this.comment = comment;
        this.postedDate = postedDate;
    }

}
