package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Courses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseList{
    protected List<Course> courses;
    private final Courses refCourses = Courses.getInstance();
    // Constructor
    public CourseList() {
        courses = new ArrayList<Course>();
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
            System.out.println(courseCode);
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
//        System.out.println(course);
//        System.out.println("Course time: " + course.getTime());
        String[] inputTimes = course.getTime().split("-");
//        System.out.println(Arrays.deepToString(inputTimes));
        int inputStartTime = courseTimeValue(inputTimes[0]);
        int inputEndTime = courseTimeValue(inputTimes[1]);

        System.out.println("Input times-> Start: " + inputStartTime + " End: " + inputEndTime);
        for (Course c : courses) {
            String day = c.getDay();
            String[] times = c.getTime().split("-");
            int startTime = courseTimeValue(times[0]);
            int endTime = courseTimeValue(times[1]);

//            System.out.println("Overlap Days: " + overlapDays(day, course.getDay()));
//            System.out.println("Schedule course-> Start: " + startTime + " End: " + endTime);
            if(c.getCourseCode().equals(course.getCourseCode())){
//                System.out.println("This class is already in the schedule!");
                return true;
            } else if (overlapDays(day, course.getDay()) && (
                    (inputStartTime >= startTime && inputStartTime < endTime) ||
                            (inputEndTime > startTime && inputEndTime<= endTime)) ) {
//                System.out.println("You have a conflicting class in your schedule!");
//                System.out.println("You're class over laps with: " + c.toString());
                return true;
            }
        }
        return false;
    }

    // checks and sees if any days overlap
    private boolean overlapDays(String courseDays1, String courseDays2){
        for(int i = 0; i < courseDays2.length(); i++){
            if(courseDays1.indexOf(courseDays2.charAt(i)) != -1) return true;
        }
        return false;
    }

    // converts the time into an integer value
    private int courseTimeValue(String time) {
        time = time.replace(" ", "");
        String[] splitT = time.split(":");
        Arrays.deepToString(splitT);
        int totalT = 0;
        if (splitT[2].contains("PM") && !splitT[0].contains("12")) {
            //All PM times except Noon
            totalT = (Integer.parseInt(splitT[0]) + 12) * 60;//Convert to Military
        } else if (splitT[0].contains("12") && !splitT[2].contains("PM")) {
            //Catch 12AM classes
            totalT = (Integer.parseInt(splitT[0]) + 12) * 60;//Convert to Military
        }else{
            totalT = Integer.parseInt(splitT[0]) * 60;
        }
        System.out.println("TOTAL: " + totalT);
        return totalT + Integer.parseInt(splitT[1]);
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
