package edu.gcc.nemo.scheduler;

import java.util.ArrayList;
import java.util.List;

public class Student extends User{
    private int id;
    private int gradYear;
    private List<String> majors;
    private List<String> minors;

    public Student(String login, String password, String name, int id, int gradYear) {
        super(login, password, name);
        this.id = id;
        this.gradYear = gradYear;
        majors = new ArrayList<>();
        minors = new ArrayList<>();
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
}
