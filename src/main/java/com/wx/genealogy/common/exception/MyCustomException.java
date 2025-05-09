package com.wx.genealogy.common.exception;

public class MyCustomException extends Exception {
    private static final long serialVersionUID = 1L;
    private String message;
    //构造方法
    public MyCustomException(String message) {
        this.setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

