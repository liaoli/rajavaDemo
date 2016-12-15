package com.livestar.rxjavademo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/15 0015.
 */

public class People {
    private String name;
    private int age;
    private boolean six;
    private String[] children;

    public People(String name, int age, boolean six, String[] children) {
        this.name = name;
        this.age = age;
        this.six = six;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSix() {
        return six;
    }

    public void setSix(boolean six) {
        this.six = six;
    }

    public String[] getChildren() {
        return children;
    }

    public void setChildren(String[] children) {
        this.children = children;
    }
}
