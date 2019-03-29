package com.app.model;

import com.app.model.primarykey.RecipeTagsPrimaryKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
@IdClass(RecipeTagsPrimaryKey.class)
public class RecipeTags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;

    @ManyToOne
    @JsonIgnoreProperties("recipeTags")
    @JsonIgnore
    @JoinColumn(name = "reid",nullable = false,insertable = false,updatable = false)
    private Recipe recipe;

    @ManyToOne
    @JsonIgnoreProperties({"recipeTags","userTastes"})
    @JoinColumn(name = "tagId",nullable = false,insertable = false,updatable = false)
    private Tags tags;

    @Override
    public String toString() {
        return "RecipeTags{" +
                "reid=" + reid +
                ", tagId=" + tagId +
                ", recipe=" + recipe +
                ", tags=" + tags +
                '}';
    }

    public Integer getReid() {
        return reid;
    }

    public void setReid(Integer reid) {
        this.reid = reid;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }
}
