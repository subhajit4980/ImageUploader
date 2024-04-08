package com.subhajit.ImageUploader.Payload.Response;

public class Custom_Response {
    String message;
    boolean success;

    public Custom_Response(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
