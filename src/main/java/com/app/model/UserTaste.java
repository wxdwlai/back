package com.app.model;

import com.app.model.primarykey.UserTastePrimaryKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.catalina.LifecycleState;

import javax.persistence.*;
import java.util.List;

@Entity
@IdClass(UserTastePrimaryKey.class)
public class UserTaste {
    @Id
    private Integer uid;
    @Id
    private Integer tagId;

    @ManyToOne
    @JsonIgnoreProperties({"userTastes","recipeTags"})
    @JoinColumn(name = "tagId",insertable = false,updatable = false,nullable = false)
    private Tags tags;

    @ManyToOne
    @JsonIgnoreProperties({"userCollects","recipes","viewLogs","comments","commentReplies","userTastes"})
    @JoinColumn(name = "uid",insertable = false,updatable = false,nullable = false)
    private UserInfo userInfo;

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

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
