package edu.gcc.nemo.scheduler;

import java.util.List;

public class StatusSheet {
    private List<String> major;
    private List<String> minor;
    private int gradYear;
    private CourseList completedCourse;

    public StatusSheet() {}

    //Getters and Setters
    public int getGradYear() {
        return gradYear;
    }

    public void setGradYear(int gradYear) {
        this.gradYear = gradYear;
    }
}
