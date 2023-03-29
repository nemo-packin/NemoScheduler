package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Courses;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Schedule{
    private CourseList courseList;
    private String coursesString = "";
    private boolean isApproved;
    private String semester;
    private Connection conn;
    private Statement stmt;
    private String name;
    private final Courses refCourses;

    // Constructor

    /**
     *
     * @param name is the identifier in the database -> it is a comination of
     * @param semester
     * @param isApproved
     * @param courses
     * @param student
     * @param refCourses
     */
    public Schedule (String name, String semester, int isApproved, String courses, Student student, Courses refCourses){
        int id = student.getId();
        this.name = id + name;
        this.semester = semester;
        if(isApproved == 1)
            this.isApproved = true;
        else this.isApproved = false;
        this.coursesString = courses;
        this.refCourses = refCourses;
        courseList = new CourseList(refCourses);
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

    //Methods
    public String serialize() {
        return null;
    }

    // TEMPORARY toString method to check classes added to schedule
    public String toString(){
        String listOfCoursesInSchedule = "Courses in you're schedule include: \n";
        for(Course c: courseList.courses){
            listOfCoursesInSchedule += c.toString();
        }
        return listOfCoursesInSchedule;
    }

    public void addCourseToSchedule(String courseCode){
        isApproved = false;
        courseList.addCourse(courseCode);
    }

    public void removeCourseFromSchedule(String courseCode){
        isApproved = false;
        courseList.removeCourse(courseCode);
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

    public void setSemester(String semester) {
        this.semester = semester;
    }

//    private void loadSchedule(String name){
//        try{
//            PreparedStatement loadS = conn.prepareStatement("select * from Schedules where name = ?");
//            loadS.setString(1, name);
//            ResultSet rs = stmt.executeQuery("select * from Schedules where name = " + name);
//            while(rs.next()){
//                this.name = rs.getString("Name");
//                this.semester = rs.getString("Semester");
//                this.isApproved = rs.getInt("isApproved");
//                this.coursesString = rs.getString("Courses");
//            }
//        }catch (SQLException e){
//            throw new RuntimeException(e);
//        }
//    }


//    public void saveSchedule(Schedule schedule){
//        try {
//            File savedSchedule = new File("saved_schedule.txt");
//            savedSchedule.createNewFile();
//            FileWriter myWriter = new FileWriter("saved_schedule.txt");
//            StringBuilder scheduleString = new StringBuilder("");
//            System.out.println(schedule.courses.size());
//            for (int i = 0; i < schedule.courses.size(); i++){
//                scheduleString.append(schedule.courses.toString());
//            }
//            myWriter.write(String.valueOf(scheduleString));
//            myWriter.close();
//            System.out.println("Successfully wrote to the file.");
//        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//    }
}
