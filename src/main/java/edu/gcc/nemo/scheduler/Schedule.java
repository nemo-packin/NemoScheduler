package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Courses;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Schedule{
    private CourseList courseList;
    private String coursesString = "";
    private boolean isApproved;
    private String semester;
    private Connection conn;
    private Statement stmt;
    private String name;
    private final Courses refCourses;

    private int id;

    // Constructor
    /**
     *
     * @param name is the identifier in the database -> it is a comination of
     * @param semester
     * @param isApproved
     * @param courses
     * @param id
     * @param refCourses
     */
    public Schedule (String name, String semester, int isApproved, String courses, Courses refCourses, int id){
        this.id = id;
        this.name = id + name;
        this.semester = semester;
        if(isApproved == 1)
            this.isApproved = true;
        else this.isApproved = false;
        this.coursesString = courses;
        this.refCourses = refCourses;
        courseList = new CourseList(refCourses);
        if(coursesString.length() > 0) {
            for(String courseCode : coursesString.split(",")) {
                courseList.addCourse(courseCode);
            }
        }

    }

    /**
     *
     * @param courses the string of courses from the database
     * populates the courseList with the courses from the database
     */
    public void courseListBuilder(String courses){
        List<String> temp = Arrays.asList(courses.split(","));
        for (int i = 0; i < temp.size(); i++){
            courseList.addCourse(temp.get(i));
        }
    }

    // TEMPORARY toString method to check classes added to schedule
    public String toString(){
        String listOfCoursesInSchedule = "Courses in you're schedule include: \n";
        for(Course c: courseList.courses){
            listOfCoursesInSchedule += c.toString();
        }
        return listOfCoursesInSchedule;
    }

    public boolean addCourse(String courseCode){
        if (courseList.addCourse(courseCode)) {
            isApproved = false;
            return true;
        } else {
            System.out.println("Did not add course");
            return false;
        }
    }

    public void removeCourse(String courseCode){
        if(courseList.removeCourse(courseCode))
            isApproved = false;
    }

    public void approve() {
        isApproved = true;
    }

    //Getters and Setters
    public boolean getApproved() {
        return isApproved;
    }

    public String getSemester() {
        return semester;
    }


    public int getId() {
        return id;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getName() {
        return this.name;
    }

    public String getCourses() {
        this.coursesString = this.courseList.courses.stream().map((course -> course.getCourseCode())).collect(Collectors.joining(","));
        return this.coursesString;
    }

    public CourseList getCourseList() {
        return courseList;
    }
    public String calendarView() {
        //Set up the format for storing once the data is parsed
        ArrayList<Course> Monday = new ArrayList<Course>();
        ArrayList<Course> Tuesday = new ArrayList<Course>();
        ArrayList<Course> Wednesday = new ArrayList<Course>();
        ArrayList<Course> Thursday = new ArrayList<Course>();
        ArrayList<Course> Friday = new ArrayList<Course>();
        Map<String, ArrayList<Course>> days = new LinkedHashMap<String, ArrayList<Course>>();
        days.put("M", Monday);
        days.put("T", Tuesday);
        days.put("W", Wednesday);
        days.put("Th", Thursday);
        days.put("F", Friday);
        Map<String, String> prefixes = new LinkedHashMap<String, String>();
        prefixes.put("M", "Monday");
        prefixes.put("T", "Tuesday");
        prefixes.put("W", "Wednesday");
        prefixes.put("Th", "Thursday");
        prefixes.put("F", "Friday");
        String[] times = {"8:00am", "8:15am", "8:30am", "8:45am", "9:00am", "9:15am", "9:30am", "9:45am",
                "10:00am", "10:15am", "10:30am", "10:45am", "11:00am", "11:15am", "11:30am", "11:45am",
                "12:00pm", "12:15pm", "12:30pm", "12:45pm", "1:00pm", "1:15pm", "1:30pm", "1:45pm",
                "2:00pm", "2:15pm", "2:30pm", "2:45pm", "3:00pm", "3:15pm", "3:30pm", "3:45pm",
                "4:00pm", "4:15pm", "4:30pm", "4:45pm", "5:00pm", "5:15pm", "5:30pm", "5:45pm",
                "6:00pm", "6:15pm", "6:30pm", "6:45pm", "7:00pm", "7:15pm", "7:30pm", "7:45pm"};

        //Add the courses to their correct days map
        for(Course c : courseList.courses) {
            String d = c.getDay();
            String[] parsed = d.split("(?=\\p{Upper})");
            for(String p : parsed) {
                switch (p) {
                    case "M": Monday.add(c); break;
                    case "T": Tuesday.add(c); break;
                    case "W": Wednesday.add(c); break;
                    case "R": Thursday.add(c); break;
                    case "F": Friday.add(c); break;
                    default:
                }
            }
        }

        //Create the string to return
        Course holdCourse = null;
        int repeatsLeft = 0;
        String finalString = "";
        finalString += ("============================================\n");
        finalString += ("Schedule View\n");
        finalString += ("============================================\n");
        //For each day of the week
        for(String key : days.keySet()) {
            finalString += "++++" + prefixes.get(key) + "++++" + "\n";
            //For every 15 minute increment
            for(String time : times) {
                finalString += (time + " - ");
                //For every course that day
                for(Course it : days.get(key)) {

                    String[] it_nums = it.getTime().split(":");
                    String[] nums = time.split(":");
                    Integer it_hour = Integer.parseInt(it_nums[0]);
                    Integer it_minute = Integer.parseInt(it_nums[1]) / 15 * 15;
                    Integer hour = Integer.parseInt(nums[0]);
                    Integer minute = Integer.parseInt(nums[1].substring(0,2));
                    //If the course is at that time
                    if(it_hour.equals(hour) && it_minute.equals(minute)) {
                        if(key.equals("T") || key.equals("Th")) {
                            repeatsLeft = 5;

                        } else {
                            repeatsLeft = 4;
                        }
                        holdCourse = it;
                    }
                }
                if(repeatsLeft > 0) {
                    finalString += holdCourse.getCourseCode();
                    repeatsLeft --;
                }
                finalString += "\n";
            }
        }
        finalString += ("============================================\n");;
        return finalString;
    }
}
