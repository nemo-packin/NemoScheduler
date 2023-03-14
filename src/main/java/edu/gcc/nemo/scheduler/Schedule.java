package edu.gcc.nemo.scheduler;

import java.util.List;

public class Schedule extends CourseList {
//    List<Course> courses;
    private Boolean isApproved;
    private String semester;

    // Constructor
    public Schedule(String semester){
        isApproved = false;
        this.semester = semester;
    }

    //Methods
    public String serialize() {
        return null;
    }

    public void approve() {
        isApproved = true;
        System.out.println("You're class was approved!");
    }

    // TEMPORARY toString method to check classes added to schedule
    public String toString(){
        String listOfCoursesInSchedule = "Courses in you're schedule include: \n";
        for(Course c: courses){
            listOfCoursesInSchedule += c.toString();
        }
        return listOfCoursesInSchedule;
    }

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
}
