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
    public void addCourse(String courseCode) {
        Course c = refCourses.getCourse(courseCode);
        //Check to see if the course is already in the schedule
        if(checkOverlap(c))
            return;
        //If the course was not already in the schedule
        courses.add(c);
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
//        {
//        int conflicts = 0;
//        List<Course> overlappingCourses = new ArrayList<>();
//        for (int i = 0; i < courses.size(); i++){
////            Course temp = courses.get(i);
//            String day = courses.get(i).getDay();
//            String time = courses.get(i).getTime();
//
//
////            for (int j = 0; j < courses.size(); j++){
//                if (c.getDay().equals(day) && c.getTime().equals(time)){
//                    conflicts++;
//                }
////                if (conflicts > 1){
////                    overlappingCourses.add(temp);
////                    overlappingCourses.add(courses.get(j));
////                } else {
////                    overlappingCourses.remove(temp);
////                }
////            }
//        }
//        System.out.println("your schedule has " + overlappingCourses.size() + " conflicts.");
//        if (overlappingCourses.size() > 0){
//            System.out.print("the overlapping courses are: ");
//            for (int i = 0; i < overlappingCourses.size(); i++){
//                System.out.print(overlappingCourses.get(i).getCourseCode());
//            }
//            System.out.print("\n");
//        }
//    }
}
