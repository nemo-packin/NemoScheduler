package edu.gcc.nemo.scheduler;

import java.util.ArrayList;
import java.util.List;

public abstract class CourseList extends tempAllCourseList{
    protected List<Course> courses;

    // Constructor
    public CourseList() {
        courses = new ArrayList<Course>();
    }

    //Methods
    public void addCourse(String courseCode) {
        courses.add(getCourse(courseCode));
//        for(Course c: allCourseList){
//            if (c.getCourseCode().equals(courseCode)){
//                courses.add(c);
//                System.out.println("Course was successfully added!");
//                break;
//            }
//        }
    }

    public void removeCourse(String courseCode) {

    }
}
