package edu.gcc.nemo.scheduler;

import java.util.ArrayList;
import java.util.List;

public abstract class CourseList extends tempAllCourseList{
    protected List<Course> courses;

    private final Courses refCourses;

    // Constructor
    public CourseList(Courses c) {
        courses = new ArrayList<Course>();
    }

    //Methods
    public void addCourse(String courseCode) {
        refCourses.getCourse(sourseCode)
        //Check to see if the course is already in the schedule
        for(Course c: courses){
            if (c.getCourseCode().equals(courseCode)){
                System.out.println("Course was already found in the schedule!\n");
                return;
            }
        }

        //If the course was not already in the schedule
        courses.add(getCourse(courseCode));
        System.out.println("Course successfully added!\n");
//        for(Course c: allCourseList){
//            if (c.getCourseCode().equals(courseCode)){
//                courses.add(c);
//                System.out.println("Course was successfully added!");
//                break;
//            }
//        }
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
