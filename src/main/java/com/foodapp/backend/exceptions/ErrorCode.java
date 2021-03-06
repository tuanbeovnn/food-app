package com.foodapp.backend.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @Author Tuan Nguyen
 */
public enum ErrorCode {

    SUCCESS(HttpStatus.OK, "Success", "Success"),
    ID_NOT_FOUND(HttpStatus.NOT_FOUND, "LMS-404", "Could not find the Id"),
    API_NOT_FOUND(HttpStatus.NOT_FOUND, "LMS-404", "API Not Found"),
    AUTHORIZATION_FIELD_MISSING(HttpStatus.FORBIDDEN, "LMS-40011", "Please log in"),
    CAN_NOT_DELETE_COURSE(HttpStatus.BAD_REQUEST, "LMS-40018", "Student is studying"),
    CAN_NOT_DELETE_ROLE(HttpStatus.BAD_REQUEST, "LMS-40019", "Role is using"),
    SIGNATURE_NOT_CORRECT(HttpStatus.FORBIDDEN, "LMS-40001", "Signature not correct"),
    EXPIRED(HttpStatus.FORBIDDEN, "LMS-40003", "Expired"),
    PASSWORD_DID_NOT_MATCH(HttpStatus.BAD_REQUEST, "LMS-40005", "Password did not match"),
    CAN_NOT_UPLOAD_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "LMS-40006", "Can not upload file"),
    CODE_EXIST(HttpStatus.BAD_REQUEST, "LMS-400", "The code exists"),
    ANSWER_NOT_FOUND(HttpStatus.BAD_REQUEST, "Could not find the answer", "Could not find the answer"),
    EMPTY_PRODUCT(HttpStatus.BAD_REQUEST, "LMS-400", "The product is empty"),
    USER_EXISTS(HttpStatus.BAD_REQUEST,"LMS-40019","User had been already exists."),
    USER_NOT_LOGIN(HttpStatus.INTERNAL_SERVER_ERROR,"LMS-40020","Customer needs to login"),
    ACTIVED_ACCOUNT(HttpStatus.INTERNAL_SERVER_ERROR,"LMS-40021","Account has been activated"),
    METHOD_SUPPORT(HttpStatus.INTERNAL_SERVER_ERROR,"LMS-40024","Method does not support"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN,"LMS-40024","Access Denied"),
    UNSUPPORT_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "LMS-40020", "Unsupport this file extension");


    private final HttpStatus status;
    private String code;
    private String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public HttpStatus status() {
        return status;
    }

    public String message() {
        return message;
    }
}
