package com.terrence.loyalty.forumapi.domain.message;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String userName;

    @NotNull
    private LocalDateTime postedDate;

    @NotNull
    private String message;

    protected Message() {}

    public Message(@NotNull String userName, @NotNull LocalDateTime postedDate, @NotNull String message) {
        this.userName = userName;
        this.postedDate = postedDate;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
