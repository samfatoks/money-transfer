package com.demo.moneytransfer.exception;

public class AppException extends Exception {

    private static final long serialVersionUID = -8999932578270387947L;
    /** application specific error code */
    int code;

    /**
     *
     * @param code
     * @param message
     */
    public AppException(int code, String message) {
        super(message);
        this.code = code;
    }

    public AppException() { }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}