package com.app.model;

import com.app.model.primarykey.UserCollectsPrimaryKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@IdClass(UserCollectsPrimaryKey.class)
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class UserCollects {

    @Id
    private Integer reid;

    @Id
    private Integer uid;

    //0：收藏菜谱
    //1：点赞菜谱
    @Id
    private boolean type;

    @ManyToOne
    @JsonIgnoreProperties({"userCollects","recipes","viewLogs","comments","commentReplies","userTastes","recipes"})
    @JoinColumn(name = "uid",insertable = false,updatable = false,nullable = false)
    private UserInfo userInfo;

    @ManyToOne
    @JsonIgnoreProperties({"userCollects","stepsList","viewLogs","recipeTypes","recipeTags","userCollects","comments"})
    @JoinColumn(name = "reid",insertable = false,updatable = false,nullable = false)
    private Recipe recipe;

    private Timestamp time;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserCollects{" +
                "reid=" + reid +
                ", uid=" + uid +
                ", userInfo=" + userInfo +
                ", recipe=" + recipe +
                ", time=" + time +
                '}';
    }
}
