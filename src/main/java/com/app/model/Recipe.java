package com.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "reid")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reid;
    private Integer uid;
    private String title;
    private double score;
    private String ings;
    private String image;
    private int visiteds;
    private int collects;
    private int likes;
    private String tags;
    private String intro;

    /**
     * recipe表与user表
     */
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "uid",updatable = false,insertable = false)
    @JsonIgnoreProperties(value = {"recipes","viewLogs","userCollects","comments","commentReplies","userTastes"})
    private UserInfo userInfo;
    /**
     * recipe表与steps表一对多的关系连接
     */
    @OneToMany(mappedBy="recipe",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    @JsonIgnoreProperties("recipe")
    private List<Steps> stepsList;

    /**
     * recipe表与userInfo表是多对多的表连接关系
     * 维持中间连接关系的表格是viewLogs表（浏览记录表）
     */
    @OneToMany(mappedBy = "recipe",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"recipe","userInfo"})
    private List<ViewLogs> viewLogs;

    @OneToMany(mappedBy = "recipe",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnoreProperties("recipe")
    private List<RecipeTypes> recipeTypes;

    @OneToMany(mappedBy = "recipe",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnoreProperties("recipe")
    private List<RecipeTags> recipeTags;

    @OneToMany(mappedBy = "recipe",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"recipe","userInfo"})
    private List<UserCollects> userCollects;

    /**
     * 菜谱评论表
     * @return
     */
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("recipe")
    private List<Comment> comments;

    public Integer getReid() {
        return reid;
    }

    public void setReid(Integer reid) {
        this.reid = reid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getIngs() {
        return ings;
    }

    public void setIngs(String ings) {
        this.ings = ings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getVisiteds() {
        return visiteds;
    }

    public void setVisiteds(int visiteds) {
        this.visiteds = visiteds;
    }

    public int getCollects() {
        return collects;
    }

    public void setCollects(int collects) {
        this.collects = collects;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public List<Steps> getStepsList() {
        return stepsList;
    }

    public void setStepsList(List<Steps> stepsList) {
        this.stepsList = stepsList;
    }

    public List<ViewLogs> getViewLogs() {
        return viewLogs;
    }

    public void setViewLogs(List<ViewLogs> viewLogs) {
        this.viewLogs = viewLogs;
    }

    public List<RecipeTypes> getRecipeTypes() {
        return recipeTypes;
    }

    public void setRecipeTypes(List<RecipeTypes> recipeTypes) {
        this.recipeTypes = recipeTypes;
    }

    public List<RecipeTags> getRecipeTags() {
        return recipeTags;
    }

    public void setRecipeTags(List<RecipeTags> recipeTags) {
        this.recipeTags = recipeTags;
    }

    public List<UserCollects> getUserCollects() {
        return userCollects;
    }

    public void setUserCollects(List<UserCollects> userCollects) {
        this.userCollects = userCollects;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
