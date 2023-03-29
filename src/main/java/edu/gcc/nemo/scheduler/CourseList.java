package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Courses;

import java.util.ArrayList;
import java.util.List;

public abstract class CourseList{
    protected List<Course> courses;
    private final Courses refCourses;
    // Constructor
    public CourseList(Courses refCourses) {
        courses = new ArrayList<Course>();
        this.refCourses = refCourses;
    }

    //Methods
    public void addCourse(String courseCode) {
        refCourses.getCourse(courseCode);
        //Check to see if the course is already in the schedule
        for(Course c: courses){
            if (c.getCourseCode().equals(courseCode)){
                System.out.println("Course was already found in the schedule!\n");
                return;
            }
        }
        //If the course was not already in the schedule
        courses.add(refCourses.getCourse(courseCode));
        System.out.println("Course successfully added!\n");

    }

    public void removeCourse(String courseCode) {
        //Check to make sure that the course is in the schedule before removing it
        boolean removed = false;
        for(int i = 0; i < courses.size(); i++) {
            if(courses.get(i).getCourseCode().equals(courseCode)) {
                courses.remove(i);
                removed = true;
                break;
            }
        }
        // Handle removal and not found cases
        if(removed) {
            System.out.println("Course was successfully removed!\n");
        } else {
            System.out.println("Course was not found in the schedule.\n");
        }
    }
}
