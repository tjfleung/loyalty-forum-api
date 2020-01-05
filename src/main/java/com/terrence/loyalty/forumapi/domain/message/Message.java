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
    private String username;

    @NotNull
    private LocalDateTime postedDate;

    @NotNull
    private String comment;

    protected Message() {}

    public Message(@NotNull String username, @NotNull LocalDateTime postedDate, @NotNull String comment) {
        this.username = username;
        this.postedDate = postedDate;
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String message) {
        this.comment = message;
    }

}
