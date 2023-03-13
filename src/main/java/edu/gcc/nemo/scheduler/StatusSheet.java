package edu.gcc.nemo.scheduler;

import java.util.List;

public class StatusSheet extends CourseList {
    List<String> major;
    List<String> minor;
    int gradYear;

    //Getters and Setters
    public int getGradYear() {
        return gradYear;
    }

    public void setGradYear(int gradYear) {
        this.gradYear = gradYear;
    }

    //Methods
    public String serialize() {
        return null;
    }
}
