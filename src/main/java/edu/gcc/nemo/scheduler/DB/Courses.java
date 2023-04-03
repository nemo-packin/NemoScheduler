package edu.gcc.nemo.scheduler.DB;

import edu.gcc.nemo.scheduler.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Courses {
    /***
     * Courses: A singleton class to access the course database.
     *
     * Changelog:
     * Ken, 3/18/23: Initial Implementation
     */
    private static Courses instance = null;
    private Connection conn;
    private Statement statement;
    private PreparedStatement courseCodeStatement;
    private Map<String, Course> allCourses;
    private ArrayList<String> courseCodes;


    /**
     Get instance of the Courses singleton.
     */
    public static Courses getCoursesInstance() {
        if(instance == null) {
            instance = new Courses();
        }
        return instance;
    }

    /**
     * Constructor
     */
    private Courses() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            statement = conn.createStatement();
            courseCodeStatement = conn.prepareStatement("select  * from Courses where course_code = ?");
            allCourses = new HashMap<>();
            courseCodes = new ArrayList<>();
            getAllCourses();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /***
     * getCourse: Retrieve a course by its course code from the database
     * @param courseCode the course code
     * @return the course object corresponding to the course code
     */
    public Course getCourse(String courseCode) {
        return allCourses.get(courseCode);
    }

    /***
     * getAllCourses
     * populates a map<courseCode, Course> all the courses in the database
     */
    private void getAllCourses() {
        try {
            ResultSet rs = statement.executeQuery("select * from Courses");
            while(rs.next()) {
                allCourses.put(rs.getString("course_code"),new Course(
                        rs.getString("course_code"),
                        rs.getString("department"),
                        rs.getString("semester"),
                        rs.getString("time"),
                        rs.getString("day"),
                        rs.getString("prof"),
                        rs.getString("name"),
                        rs.getInt("credit_hours"),
                        rs.getInt("capacity")));
                courseCodes.add(rs.getString("course_code"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getAllCourseCodes(){
        return courseCodes;
    }
}
