package com.terrence.loyalty.forumapi.exception;

import org.springframework.http.HttpStatus;

public class ForumException extends RuntimeException {

    private HttpStatus httpStatus;

    public ForumException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
