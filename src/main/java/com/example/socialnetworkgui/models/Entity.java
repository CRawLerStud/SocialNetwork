package com.example.socialnetworkgui.models;

import java.io.Serializable;

public class Entity<ID> implements Serializable {

    private static final long serialVersionUID = 157946123123824L;
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}