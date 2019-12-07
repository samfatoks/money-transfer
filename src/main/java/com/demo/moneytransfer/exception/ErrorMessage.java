package com.demo.moneytransfer.exception;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class ErrorMessage {

    private int status;
    private int code;
    private String message;

    public ErrorMessage() {}

    public ErrorMessage(AppException ex){
        this.setCode(ex.code);
        this.setStatus(ex.status);
        this.setMessage(ex.getMessage());
    }

    public ErrorMessage(NotFoundException ex){
        this.code = -1;
        this.status = Response.Status.NOT_FOUND.getStatusCode();
        this.message = ex.getMessage();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}