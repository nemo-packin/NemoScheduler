package edu.gcc.nemo.scheduler;

import java.util.List;

public class Schedule extends CourseList {
    List<Course> courses;
    Boolean isApproved;
    String semester;

    //Getters and Setters
    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    //Methods
    public String serialize() {
        return null;
    }

    public void approve() {

    }
}
