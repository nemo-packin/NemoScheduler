package edu.gcc.nemo.scheduler;

import java.util.ArrayList;
import java.util.List;

public class Student extends User{
    private int gradYear;
    private List<String> majors;
    private List<String> minors;

    public Student(String login, String password, String name, int id, int gradYear) {
        super(login, password, name, id);
        this.gradYear = gradYear;
        majors = new ArrayList<>();
        minors = new ArrayList<>();
    }

    @Override
    public void printInfo() {
        System.out.println(
                "You are a student! Here is your info: " + "\n" +
                "Login: " + login + "\n" +
                "Name: " + name + "\n" +
                "Password: " + password + "\n" +
                "id: " + id + "\n" +
                "Grad year:" + gradYear + "\n" +
                "Majors: " + majors + "\n" +
                "Minors: " + minors + "\n");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGradYear() {
        return gradYear;
    }

    public void setGradYear(int gradYear) {
        this.gradYear = gradYear;
    }

    public List<String> getMajor() {
        return majors;
    }

    public void addMajor(String major) {
        majors.add(major);
    }

    public List<String> getMinor() {
        return minors;
    }

    public void addMinor(String minor) {
        minors.add(minor);
    }

    @Override
    public String toString(){
        return name;
    }

}
