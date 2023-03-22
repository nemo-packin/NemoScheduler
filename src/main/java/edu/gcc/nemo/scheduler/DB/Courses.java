package edu.gcc.nemo.scheduler.DB;

import edu.gcc.nemo.scheduler.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    /***
     *
     */
    private Courses() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            statement = conn.createStatement();
            courseCodeStatement = conn.prepareStatement("select  * from Courses where course_code = ?");
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
        try {
            courseCodeStatement.setString(1, courseCode);
            ResultSet rs = courseCodeStatement.executeQuery();
            if(rs.next()) {
                return new Course(
                        rs.getString("course_code"),
                        rs.getString("department"),
                        rs.getString("semester"),
                        rs.getString("time"),
                        rs.getString("day"),
                        rs.getString("prof"),
                        rs.getString("name"),
                        rs.getInt("credit_hours"),
                        rs.getInt("capacity")
                );
            }
            throw new IllegalArgumentException("Course not found for course code.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * getAllCourses
     * @return a list of all the courses in the database
     */
    public List<Course> getAllCourses() {
        List<Course> result = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("select * from Courses");
            while(rs.next()) {
                result.add(new Course(
                        rs.getString("course_code"),
                        rs.getString("department"),
                        rs.getString("semester"),
                        rs.getString("time"),
                        rs.getString("day"),
                        rs.getString("prof"),
                        rs.getString("name"),
                        rs.getInt("credit_hours"),
                        rs.getInt("capacity")
                ));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Get instance of the Courses singleton.
     */
    public static Courses getInstance() {
        if(instance == null) {
            instance = new Courses();
        }
        return instance;
    }
}
