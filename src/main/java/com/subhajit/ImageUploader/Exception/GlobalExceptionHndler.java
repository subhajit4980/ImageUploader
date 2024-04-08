package com.subhajit.ImageUploader.Exception;

import com.amazonaws.services.wafv2.model.CustomResponse;
import com.subhajit.ImageUploader.Payload.Response.Custom_Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHndler {
    @ExceptionHandler
    public ResponseEntity<Custom_Response> handleImageUploadException(ImageUploadException exception)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Custom_Response("Image not uploaded",false));
    }
}
