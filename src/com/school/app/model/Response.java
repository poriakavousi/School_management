package com.school.app.model;

import java.time.LocalDateTime;

public class Response<T> {
    private LocalDateTime time;
    private int status;
    private String message;
    private T data;

    public Response(int status, String message, T data) {
        this.time = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public LocalDateTime getTime() { return time; }
    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }

    public static <T> Response<T> ok(String msg, T data) {
        return new Response<>(200, msg, data);
    }

    public static <T> Response<T> created(String msg, T data) {
        return new Response<>(201, msg, data);
    }

    public static <T> Response<T> error(int code, String msg) {
        return new Response<>(code, msg, null);
    }

    @Override
    public String toString() {
        return "Response{" +
                "time=" + time +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
