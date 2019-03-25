package com.app.model;

import com.app.model.primarykey.RecipeTypesPrimaryKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.lang.reflect.Type;

/**
 * recipe与types表的多对多联系的中间表格
 */
@Entity
@IdClass(RecipeTypesPrimaryKey.class)
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class RecipeTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer typeId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reid;

    @ManyToOne
    @JsonIgnoreProperties("recipeTypes")
    @JsonIgnore
    @JoinColumn(name = "reid",nullable = false,insertable = false,updatable = false)
    private Recipe recipe;

    @ManyToOne
    @JsonIgnoreProperties("recipeTypes")
    @JoinColumn(name = "typeId",nullable = false,insertable = false,updatable = false)
    private Types types;

    @Override
    public String toString() {
        return "RecipeTypes{" +
                "typeId=" + typeId +
                ", reid=" + reid +
                ", recipe=" + recipe +
                ", types=" + types +
                '}';
    }

    public Integer getReid() {
        return reid;
    }

    public void setReid(Integer reid) {
        this.reid = reid;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Types getTypes() {
        return types;
    }

    public void setTypes(Types types) {
        this.types = types;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

}
