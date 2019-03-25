package com.app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Recommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rid;
    private Integer reid;
    private Integer uid;
    private Timestamp deriveTime;
    private int deriveAlgorithm;
    private int feedback;
    private String keyword;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

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

    public Timestamp getDeriveTime() {
        return deriveTime;
    }

    public void setDeriveTime(Timestamp deriveTime) {
        this.deriveTime = deriveTime;
    }

    public int getDeriveAlgorithm() {
        return deriveAlgorithm;
    }

    public void setDeriveAlgorithm(int deriveAlgorithm) {
        this.deriveAlgorithm = deriveAlgorithm;
    }

    public int getFeedback() {
        return feedback;
    }

    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
