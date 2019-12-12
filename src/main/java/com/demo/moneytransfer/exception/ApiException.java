package com.demo.moneytransfer.exception;

public class ApiException extends Exception {

    private static final long serialVersionUID = -8999932578270387947L;

    Integer status;
    int code;
    /**
     *
     * @param status
     * @param code
     * @param message
     */
    public ApiException(int status, int code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public ApiException() { }

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