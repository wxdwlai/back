package com.app.model.primarykey;

import java.io.Serializable;

public class UserCollectsPrimaryKey implements Serializable {
    private Integer reid;
    private Integer uid;
    private boolean type;

    public Integer getReid() {
        return reid;
    }

    public void setReid(Integer reid) {
        this.reid = reid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
