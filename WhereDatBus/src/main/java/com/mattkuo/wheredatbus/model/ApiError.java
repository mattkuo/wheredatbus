package com.mattkuo.wheredatbus.model;

public class ApiError {
    private String mCode;
    private String mMessage;

    public ApiError(String code, String message) {
        mCode = code;
        mMessage = message;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }
}
