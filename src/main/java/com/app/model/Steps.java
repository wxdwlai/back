package com.app.model;

import com.app.model.primarykey.StepsPrimary;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@IdClass(StepsPrimary.class)
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Steps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stepId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reid;
    private String steps;
    private String stepImgs;

    @ManyToOne
    @JsonIgnoreProperties("stepsList")
    @JsonIgnore
    @JoinColumn(name = "reid",nullable = false,insertable = false,updatable = false)
    private Recipe recipe;

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Integer getStepId() {
        return stepId;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    public Integer getReid() {
        return reid;
    }

    public void setReid(Integer reid) {
        this.reid = reid;
    }

    public String getStepImgs() {
        return stepImgs;
    }

    public void setStepImgs(String stepImgs) {
        this.stepImgs = stepImgs;
    }

}
