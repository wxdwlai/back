package com.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.FetchMode;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
//@JsonIgnoreProperties(value={"comments"})
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "uid")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;
    private String userName;
    private String age;
    private int sex;
    private String intro;
    private String location;
    private String image;
    private String token;
    @NotNull
    private String password;

    @OneToMany(mappedBy = "userInfo",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"userInfo","recipeTags","comments"})
    private List<Recipe> recipes;

    @OneToMany(mappedBy = "userInfo",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"userInfo","recipe"})
    private List<ViewLogs> viewLogs;

    @OneToMany(mappedBy = "userInfo",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnoreProperties("userInfo")
    private List<UserCollects> userCollects;

    /**
     * 用户消息表
     */
    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Fetch(org.hibernate.annotations.FetchMode.JOIN)
    @JsonIgnoreProperties("userInfo")
    private List<Comment> comments;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("userInfo")
    private List<CommentReply> commentReplies;

    @OneToMany(mappedBy = "userInfo",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnoreProperties("userInfo")
    private List<UserTaste> userTastes;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<ViewLogs> getViewLogs() {
        return viewLogs;
    }

    public void setViewLogs(List<ViewLogs> viewLogs) {
        this.viewLogs = viewLogs;
    }

    public List<UserCollects> getUserCollects() {
        return userCollects;
    }

    public void setUserCollects(List<UserCollects> userCollects) {
        this.userCollects = userCollects;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<CommentReply> getCommentReplies() {
        return commentReplies;
    }

    public void setCommentReplies(List<CommentReply> commentReplies) {
        this.commentReplies = commentReplies;
    }

    public List<UserTaste> getUserTastes() {
        return userTastes;
    }

    public void setUserTastes(List<UserTaste> userTastes) {
        this.userTastes = userTastes;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
