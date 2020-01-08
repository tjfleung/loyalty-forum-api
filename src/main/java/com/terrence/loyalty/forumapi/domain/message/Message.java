package com.terrence.loyalty.forumapi.domain.message;

import com.terrence.loyalty.forumapi.domain.location.Location;

import javax.persistence.*;
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

    private long parentId;

    @Embedded
    private Location location;

    protected Message() {}

    public Message(@NotNull String username, @NotNull LocalDateTime postedDate, @NotNull String comment) {
        this.username = username;
        this.postedDate = postedDate;
        this.comment = comment;
    }

    public Message(@NotNull String username, @NotNull LocalDateTime postedDate, @NotNull String comment, Location location, long parentId) {
        this.username = username;
        this.postedDate = postedDate;
        this.comment = comment;
        this.location = location;
        this.parentId = parentId;
    }

    public Message(@NotNull String username, @NotNull LocalDateTime postedDate, @NotNull String comment, Location location) {
        this.username = username;
        this.postedDate = postedDate;
        this.comment = comment;
        this.location = location;
    }

    public Message(@NotNull String username, @NotNull LocalDateTime postedDate, @NotNull String comment, long parentId, Location location) {
        this.username = username;
        this.postedDate = postedDate;
        this.comment = comment;
        this.parentId = parentId;
        this.location = location;
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

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
