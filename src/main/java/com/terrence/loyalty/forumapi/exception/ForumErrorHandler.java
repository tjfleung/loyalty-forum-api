package com.terrence.loyalty.forumapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ForumErrorHandler {

    @ExceptionHandler(ForumException.class)
    protected ResponseEntity<ForumExceptionResponse> handleForumException(ForumException ex) {
        log.error("ForumException: ", ex);
        return new ResponseEntity<>(new ForumExceptionResponse(ex.getHttpStatus().value(), ex.getMessage()), ex.getHttpStatus());
    }
}
