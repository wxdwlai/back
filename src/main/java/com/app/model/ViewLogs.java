package com.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.soap.Text;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class ViewLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vid;
    private Integer uid;
    private Integer reid;
    private Timestamp viewTime;
    private int preferDegree;
    private double visitTime;//浏览时长
    private int visitedTimes;//浏览次数
    private String comment;

    @ManyToOne
    @JsonIgnoreProperties({"viewLogs","comments","userCollects",""})
    @JoinColumn(name = "reid",nullable = false,insertable = false,updatable = false)
    private Recipe recipe;

    @ManyToOne
    @JsonIgnoreProperties("viewLogs")
    @JoinColumn(name = "uid",nullable = false,insertable = false,updatable = false)
    private UserInfo userInfo;

    public Integer getVid() {
        return vid;
    }

    public void setVid(Integer vid) {
        this.vid = vid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getReid() {
        return reid;
    }

    public void setReid(Integer reid) {
        this.reid = reid;
    }

    public Timestamp getViewTime() {
        return viewTime;
    }

    public void setViewTime(Timestamp viewTime) {
        this.viewTime = viewTime;
    }

    public int getPreferDegree() {
        return preferDegree;
    }

    public void setPreferDegree(int preferDegree) {
        this.preferDegree = preferDegree;
    }

    public double getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(double visitTime) {
        this.visitTime = visitTime;
    }

    public int getVisitedTimes() {
        return visitedTimes;
    }

    public void setVisitedTimes(int visitedTimes) {
        this.visitedTimes = visitedTimes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "ViewLogs{" +
                "vid=" + vid +
                ", uid=" + uid +
                ", reid=" + reid +
                ", viewTime=" + viewTime +
                ", preferDegree=" + preferDegree +
                ", visitTime=" + visitTime +
                ", visitedTimes=" + visitedTimes +
                ", comment='" + comment + '\'' +
                ", recipe=" + recipe +
                '}';
    }
}
