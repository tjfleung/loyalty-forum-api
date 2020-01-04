package com.terrence.loyalty.forumapi.exception;

public class ForumExceptionResponse {

    private final int httpStatus;
    private final String errorMessage;

    public ForumExceptionResponse(int httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
