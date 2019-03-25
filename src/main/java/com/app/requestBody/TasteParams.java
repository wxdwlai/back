package com.app.requestBody;

import java.util.List;

public class TasteParams {
//    private Integer tagId;
    private String key;
    private List<Integer> list;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }
}
