package edu.gcc.nemo.scheduler;

import java.util.List;

public class StatusSheet {
    private List<String> majors;
    private List<String> minors;
    private int gradYear;
    private CourseList courses;

    public StatusSheet(List<String> majors, List<String> minors, int gradYear) {
        this.majors = majors;
        this.minors = minors;
        this.gradYear = gradYear;
        this.courses = new CourseList();
    }

    public boolean addCourse(String code) {
        return this.courses.addCourse(code);
    }

    public boolean removeCourse(String code) {
        return this.courses.removeCourse(code);
    }

    public int getGradYear() {
        return gradYear;
    }

    public void setGradYear(int gradYear) {
        this.gradYear = gradYear;
    }


    public CourseList getCourses() {
        return courses;
    }

}
