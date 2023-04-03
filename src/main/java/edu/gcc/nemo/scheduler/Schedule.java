package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Courses;
import java.sql.*;
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
    }

    //Methods
    public void saveSchedule(){
        String sql = "INSERT INTO Schedules (id, name, semester, courses, isApproved) VALUES(?,?,?,?,?)";
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, name + "_" + id);
            pstmt.setString(3, semester);
            pstmt.setString(4,courseList.coursesAsString());
            if(isApproved)
                pstmt.setInt(5,1);
            else
                pstmt.setInt(5,0);

        }catch (SQLException e){
            throw new RuntimeException(e);
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

    public void addCourse(String courseCode){
        if (courseList.addCourse(courseCode))
            isApproved = false;
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

    public void setSemester(String semester) {
        this.semester = semester;
    }

}
