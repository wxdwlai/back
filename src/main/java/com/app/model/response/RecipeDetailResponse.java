package com.app.model.response;


import com.app.model.Recipe;
import com.app.model.Steps;

import java.util.List;
import java.util.Map;

/**
 * 服务器返回信息类
 *      菜谱详情页返回信息
 * @param <T>
 */
public class RecipeDetailResponse<T> {
    private String errorCode;
    private String errorDesc;
    private boolean success;
    private String message;
    private Data data;

//    public RecipeDetailResponse(String errorCode, String errorDesc, boolean success, String message, Data data) {
//        this.errorCode = errorCode;
//        this.errorDesc = errorDesc;
//        this.success = success;
//        this.message = message;
//        this.data = data;
////        this.data = new Data(data,list);
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private Recipe data;
        private List<?> tags;

//        public Object getTypes() {
//            return types;
//        }
//
//        public void setTypes(Object types) {
//            this.types = types;
//        }


        public List<?> getTags() {
            return tags;
        }

        public void setTags(List<?> tags) {
            this.tags = tags;
        }

        public Data(Recipe data, List<?> tags) {
            this.data = data;
            this.tags = tags;
        }

        public Recipe getData() {
            return data;
        }

        public void setData(Recipe data) {
            this.data = data;
        }
    }
}
