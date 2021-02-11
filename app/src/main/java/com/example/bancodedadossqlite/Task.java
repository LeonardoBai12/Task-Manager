package com.example.bancodedadossqlite;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Task implements Serializable {

    @SerializedName("id")
    @Expose
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @SerializedName("task")
    @Expose
    private String task;

    public String getCardLink() {
        return cardLink;
    }

    public void setCardLink(String cardLink) {
        this.cardLink = cardLink;
    }

    @SerializedName("card_link")
    @Expose
    private String cardLink;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
