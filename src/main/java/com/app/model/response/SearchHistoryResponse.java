package com.app.model.response;

import java.util.List;

/**
 * 用户搜索记录返回信息类
 * @param <T>
 */
public class SearchHistoryResponse<T> {
    private String errorCode;
    private String errorDesc;
    private boolean success;
    private List<T> data;

//    public SearchHistoryResponse(String errorCode, String errorDesc, boolean success, List<T> dataBean) {
//        this.errorCode = errorCode;
//        this.errorDesc = errorDesc;
//        this.success = success;
//        this.data = dataBean;
//    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
