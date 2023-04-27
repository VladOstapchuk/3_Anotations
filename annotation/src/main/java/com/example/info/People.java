package com.example.info;

import com.example.annotation.MethodAnnotation;
import com.example.annotation.ParameterAnnotation;

public class People {

    private String name;

    private int age;

    public People(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @MethodAnnotation
    public String getName() {
        return name;
    }

    public void setName(@ParameterAnnotation String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
