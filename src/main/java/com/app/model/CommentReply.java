package com.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class CommentReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mid;

    private int mmid;

    private Integer puid;

    private Integer reid;

    private Integer ruid;

    private String context;

    private boolean isDelete;

    //标记此条评论消息是否显示展示在用户的消息接收页面
    @JsonIgnore
    private boolean isShow;

    private Timestamp createTime;

    private int type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"commentReplies","comments","viewLogs","userCollects","commentReplies","userTastes","recipes"})
    @JoinColumn(name = "puid",nullable = false,insertable = false,updatable = false)
    private  UserInfo userInfo;

//    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    @JsonIgnore
//    @JoinColumn(name = "ruid",nullable = false,insertable = false,updatable = false)
//    private Comment comment;

    @ManyToOne
    @JsonIgnoreProperties({"commentReplies","userInfo"})
//    @JsonIgnore
    @JoinColumn(name = "mmid",nullable = false,updatable = false,insertable = false)
    private Comment comment;

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public int getMmid() {
        return mmid;
    }

    public void setMmid(int mmid) {
        this.mmid = mmid;
    }

    public Integer getPuid() {
        return puid;
    }

    public void setPuid(Integer puid) {
        this.puid = puid;
    }

    public Integer getRuid() {
        return ruid;
    }

    public void setRuid(Integer ruid) {
        this.ruid = ruid;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Integer getReid() {
        return reid;
    }

    public void setReid(Integer reid) {
        this.reid = reid;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
