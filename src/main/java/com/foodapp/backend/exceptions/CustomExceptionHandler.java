package com.foodapp.backend.exceptions;

import com.foodapp.backend.utils.ResponseEntityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity handleException(MethodArgumentNotValidException ex) {
        Map<String, String> details = new HashMap<>();
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        for (FieldError fieldError : errors) {
            if (details.get(fieldError.getField()) == null) {
                details.put(fieldError.getField(), "");
            }
            details.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntityBuilder.getBuilder()
                .setCode(HttpStatus.BAD_REQUEST)
                .setMessage("Validation errors")
                .setDetails(details)
                .build();
    }


    // token ko du quyen
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity processAccessDeniedExcpetion(AccessDeniedException e) {
        return ResponseEntityBuilder.getBuilder()
                .setCode(HttpStatus.FORBIDDEN)
                .setMessage(e.getMessage())
                .build();
    }

    // loi do ko ho tro phuong thuc
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseEntity processMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return ResponseEntityBuilder.getBuilder()
                .setCode(HttpStatus.METHOD_NOT_ALLOWED)
                .setMessage(exception.getMessage())
                .build();
    }



    @ExceptionHandler(AppException.class)
    public Object handleException(HttpServletRequest request, AppException re)
            throws IOException {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(re.getCode());
        errorResponse.setMessage(re.getMessage());
        return new ResponseEntity<>(errorResponse, re.getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleCityNotFoundException(
            CustomException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
//        body.put("code", ex.getCode());
        ex.getExtensions().put("timestamp", LocalDateTime.now());
        ex.getExtensions().put("code", ex.getCode());
        ex.getExtensions().put("error message", ex.getErrorMessage());
        return new ResponseEntity<>(ex.getExtensions(), ex.getCode());
    }

    //    @ExceptionHandler(value = ClientException.class)
//    @ResponseBody
//    public ResponseEntity clientException(ClientException ex) throws Exception {
//        return ResponseEntityBuilder.getBuilder()
//                .setCode(ex.getCode())
//                .setMessage(ex.getMessage())
//                .build();
//    }
    //cu exception
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public ResponseEntity<ResponseEntity> serverException(Throwable ex) {
//        ResponseEntityBuilder builder = ResponseEntityBuilder.getBuilder()
//                .setCode(ex instanceof CustomException ? ((CustomException) ex).code : HttpStatus.INTERNAL_SERVER_ERROR)
//                .setMessage(ex.getMessage());
//        if (ex instanceof ClientException) {
//            ClientException exception = (ClientException) ex;
//            if (exception.details != null) {
//                builder.setDetails((exception.details));
//            }
//        }
//        return builder.build();
//    }
}


