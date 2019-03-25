package com.app.model;

import com.app.model.primarykey.MessagePrimaryKey;
import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
//@JsonIdentityInfo(property = "",generator = ObjectIdGenerators.PropertyGenerator.class)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "mid")
public class  Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mid;

    private Integer reid;

    private Integer puid;

    private Timestamp createTime;

    private String context;

    private int type;

    private boolean isDelete;

    @ManyToOne
    @JsonIgnoreProperties("comments")
    @JsonIgnore
    @JoinColumn(name = "reid", updatable = false, insertable = false,nullable = false)
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "puid", nullable = false,insertable = false,updatable = false)
    @JsonIgnoreProperties(value = {"comments","viewLogs","userCollects","commentReplies"})
    private UserInfo userInfo;

    @OneToMany(mappedBy = "comment",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<CommentReply> commentReplies;

    public Integer getReid() {
        return reid;
    }

    public void setReid(Integer reid) {
        this.reid = reid;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getPuid() {
        return puid;
    }

    public void setPuid(Integer puid) {
        this.puid = puid;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<CommentReply> getCommentReplies() {
        return commentReplies;
    }

    public void setCommentReplies(List<CommentReply> commentReplies) {
        this.commentReplies = commentReplies;
    }
}
