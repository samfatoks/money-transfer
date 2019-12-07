package com.demo.moneytransfer.exception;

public class AppException extends Exception {

    private static final long serialVersionUID = -8999932578270387947L;


    Integer status;

    /** application specific error code */
    int code;


    /**
     *
     * @param status
     * @param code
     * @param message
     */
    public AppException(int status, int code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public AppException() { }

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

}