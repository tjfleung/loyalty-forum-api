package com.terrence.loyalty.forumapi.rest.message;

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

}
