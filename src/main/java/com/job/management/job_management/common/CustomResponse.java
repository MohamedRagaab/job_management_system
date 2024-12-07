package com.job.management.job_management.common;

public class CustomResponse<T> {
    private final boolean success;
    private final String message;
    private final T data;

    public CustomResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
