package com.app.model.response;

import com.app.model.UserInfo;

/**
 * “我的”页面返回信息
 * 其他接口的返回信息同样适用
 * @param <T>
 */
public class UserDetailResponse<T> {
    private int errorCode;
    private boolean success;
    private String message;
    private T data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
