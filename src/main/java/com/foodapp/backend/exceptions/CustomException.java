package com.foodapp.backend.exceptions;

import com.foodapp.backend.dto.ValidateNotNullResultDto;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CustomException extends RuntimeException {
    private static final String ERRORS_NAME = "details";

    public HttpStatus code;
    public Map<String, Object> details;
    public List<Object> lstDetails;
    public String errorMessage;


    public CustomException(HttpStatus code, String message, Map<String, Object> details) {
        super(message);
        this.code = code;
        this.details = details;
    }

    public CustomException(HttpStatus code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(HttpStatus code) {
        super(code.getReasonPhrase());
        this.code = code;
    }


    public CustomException(int code) {
        super(HttpStatus.valueOf(code).getReasonPhrase());
        this.code = HttpStatus.valueOf(code);
    }

    public CustomException(String message) {
        super(message);
        this.code = HttpStatus.BAD_REQUEST;
    }

    public CustomException(String message, List<ValidateNotNullResultDto> errorsItem, HttpStatus code) {
        super(message);
        this.details = new LinkedHashMap<>();
        this.errorMessage = message;
        details.put(ERRORS_NAME, errorsItem);
        this.code = code;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getCode() {
        return code;
    }

    public Map<String, Object> getExtensions() {
        return this.details;
    }
}
