package com.terrence.loyalty.forumapi.domain.message;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDto {

    private long id;
    private String username;
    private String message;
    private LocalDateTime postedDate;

    public MessageDto() {}

    public MessageDto(String username, LocalDateTime postedDate, String message) {
        this.username = username;
        this.message = message;
        this.postedDate = postedDate;
    }

}
