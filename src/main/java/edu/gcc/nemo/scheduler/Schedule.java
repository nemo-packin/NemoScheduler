package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Courses;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class Schedule extends CourseList {
    List<Course> courseList = new ArrayList<>();
    String coursesString = "";
    private int isApproved;
    private String semester;
    private Connection conn;
    private Statement stmt;
    private String name;
    private final Courses refCourses;

    // Constructor
    public Schedule (String name, String semester, int isApproved, String courses, Student student, Courses refCourses){
        int id = student.getId();
        this.name = id + name;
        this.semester = semester;
        this.isApproved = 0;
        this.coursesString = courses;
        this.refCourses = refCourses;
    }

    /**
     *
     * @param courses the string of courses from the database
     * populates the courseList with the courses from the database
     */
    public void courseListBuilder(String courses){
        List<String> temp = Arrays.asList(courses.split(","));
        for (int i = 0; i < temp.size(); i++){
            Course tempCourse = refCourses.getCourse(temp.get(i));
            courseList.add(tempCourse);
        }
    }

    //Methods
    public String serialize() {
        return null;
    }

    public void approve() {
        isApproved = 1;
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

    public void addCourseToSchedule(String courseCode){
        for (int i = 0; i < allCourseList.length; i++){
            if (allCourseList[i].getCourseCode().equals(courseCode)){
                courses.add(allCourseList[i]);
            }
        }
    }

    private void loadSchedule(String name){
        try{
            PreparedStatement loadS = conn.prepareStatement("select * from Schedules where name = ?");
            loadS.setString(1, name);
            ResultSet rs = stmt.executeQuery("select * from Schedules where name = " + name);
            while(rs.next()){
                this.name = rs.getString("Name");
                this.semester = rs.getString("Semester");
                this.isApproved = rs.getInt("isApproved");
                this.coursesString = rs.getString("Courses");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void checkOverlap(Schedule schedule){
        int conflicts = 0;
        List<Course> overlappingCourses = new ArrayList<>();
        for (int i = 0; i < schedule.courses.size(); i++){
            Course temp = courses.get(i);
            String day = schedule.courses.get(i).getDay();
            String time = schedule.courses.get(i).getTime();
            for (int j = 0; j < courses.size(); j++){
                if (courses.get(j).getDay().equals(day) && courses.get(j).getTime().equals(time)){
                    conflicts++;
                }
                if (conflicts > 1){
                    overlappingCourses.add(temp);
                    overlappingCourses.add(courses.get(j));
                } else {
                    overlappingCourses.remove(temp);
                }
            }
        }
        System.out.println("your schedule has " + overlappingCourses.size() + " conflicts.");
        if (overlappingCourses.size() > 0){
            System.out.print("the overlapping courses are: ");
            for (int i = 0; i < overlappingCourses.size(); i++){
                System.out.print(overlappingCourses.get(i).getCourseCode());
            }
            System.out.print("\n");
        }
    }

    public void saveSchedule(Schedule schedule){
        try {
            File savedSchedule = new File("saved_schedule.txt");
            savedSchedule.createNewFile();
            FileWriter myWriter = new FileWriter("saved_schedule.txt");
            StringBuilder scheduleString = new StringBuilder("");
            System.out.println(schedule.courses.size());
            for (int i = 0; i < schedule.courses.size(); i++){
                scheduleString.append(schedule.courses.toString());
            }
            myWriter.write(String.valueOf(scheduleString));
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    //Getters and Setters
    public int getApproved() {
        return isApproved;
    }

    public void setApproved(int approved) {
        isApproved = approved;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
