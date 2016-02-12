package com.seva60plus.hum.model;

import android.graphics.drawable.Drawable;

public class Country {
    private String name;
    private String code;
    private Drawable flag;
    public Country(String name, String code, Drawable flag){
        this.name = name;
        this.code = code;
        this.flag = flag;
    }
    public String getName() {
        return name;
    }
    public Drawable getFlag() {
        return flag;
    }
    public String getCode() {
        return code;
    }
}
