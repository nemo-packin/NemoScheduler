package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Courses;

import java.util.ArrayList;
import java.util.List;

public class CourseList{
    protected List<Course> courses;
    private final Courses refCourses;
    // Constructor
    public CourseList(Courses refCourses) {
        courses = new ArrayList<Course>();
        this.refCourses = refCourses;
    }

    //Methods
    public boolean addCourse(String courseCode) {
        Course c = refCourses.getCourse(courseCode);
        //Check to see if the course is already in the schedule
        if(checkOverlap(c))
            return false;
        //If the course was not already in the schedule
        courses.add(c);
        System.out.println("Course successfully added!\n");
        return true;
    }

    public boolean removeCourse(String courseCode) {
        //Check to make sure that the course is in the schedule before removing it
        boolean removed = false;
        for(int i = 0; i < courses.size(); i++) {
            if(courses.get(i).getCourseCode().equals(courseCode.toUpperCase().trim())) {
                courses.remove(i);
                removed = true;
                break;
            }
        }
        // Handle removal and not found cases
        if(removed) {
            System.out.println("Course was successfully removed!\n");
            return true;
        } else {
            System.out.println("Course was not found in the schedule.\n");
            return false;
        }
    }

    /**
     * @param course is the course you want to check and see if it overlaps with any other courses
     * @return "true" if there is a course overlap. "false" if there is not a course overlap
     */
    private boolean checkOverlap(Course course) {
        for (Course c : courses) {
            String day = c.getDay();
            String time = c.getTime();
            if(c.getCourseCode().equals(course.getCourseCode())){
                System.out.println("This class is already in the schedule!");
                return true;
            } else if (course.getDay().equals(day) && course.getTime().equals(time)) {
                System.out.println("You have a conflicting class in your schedule!");
                System.out.println("You're class over laps with: " + c.toString());
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return returns a string of courses that is comma deliminated
     */
    public String coursesAsString(){
        StringBuilder stb = new StringBuilder();
        for(Course c: courses){
            stb.append(c.toString());
            stb.append(",");
        }
        return stb.toString();
    }
}
