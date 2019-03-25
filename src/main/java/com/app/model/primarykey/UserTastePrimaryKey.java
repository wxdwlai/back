package com.app.model.primarykey;

import java.io.Serializable;

public class UserTastePrimaryKey implements Serializable {
    private Integer uid;
    private Integer tagId;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
}
