package com.example.info;

import com.example.annotation.MethodAnnotation;
import com.example.annotation.ParameterAnnotation;

public class Employee extends People{

    private String company;

    private  int experience;

    public Employee(String name, int age, String company, int experience) {
        super(name, age);
        this.company = company;
        this.experience = experience;
    }

    public String getCompany() {
        return company;
    }

    @MethodAnnotation
    public void setCompany(@ParameterAnnotation String company) {
        this.company = company;
    }

    public int getExperience() {
        return experience;
    }

    @MethodAnnotation
    public void setExperience(@ParameterAnnotation int experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "company='" + company + '\'' +
                ", experience=" + experience +
                '}';
    }
}
