package com.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.List;
import java.util.Set;

@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Tags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;
    private String tagName;

    @OneToMany(mappedBy = "tags",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnoreProperties("tags")
    private List<RecipeTags> recipeTags;

    @OneToMany(mappedBy = "tags", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("tags")
    private List<UserTaste> userTastes;

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<UserTaste> getUserTastes() {
        return userTastes;
    }

    public void setUserTastes(List<UserTaste> userTastes) {
        this.userTastes = userTastes;
    }

    @Override
    public String toString() {
        return "Tags{" +
                "tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                ", recipeTags=" + recipeTags +
                '}';
    }

    public List<RecipeTags> getRecipeTags() {
        return recipeTags;
    }

    public void setRecipeTags(List<RecipeTags> recipeTags) {
        this.recipeTags = recipeTags;
    }
}
