package edu.gcc.nemo.scheduler.DB;

import edu.gcc.nemo.scheduler.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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


    /**
    Get instance of the Courses singleton.
    */
    public static Courses getInstance() {
        if(instance == null) {
            instance = new Courses();
        }
        return instance;
    }

    /**
     * Constructor
     */
    public Courses() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            statement = conn.createStatement();
            courseCodeStatement = conn.prepareStatement("select  * from Courses where LOWER(course_code) = ?");
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
            conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            courseCodeStatement = conn.prepareStatement("select  * from Courses where LOWER(course_code) = ?");
            courseCodeStatement.setString(1, courseCode.toLowerCase());
            ResultSet rs = courseCodeStatement.executeQuery();
            if(rs.next()) {
                Course out = new Course(
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
                conn.close();
                return out;
            }
            throw new IllegalArgumentException("Course not found for course code.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * getAllCourses
     * populates a map<courseCode, Course> all the courses in the database
     */
    public List<Course> getAllCourses() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            statement = conn.createStatement();
            List<Course> courseList = new ArrayList<>();
            ResultSet rs = statement.executeQuery("select * from Courses");
            while(rs.next()) {
                courseList.add(new Course(
                        rs.getString("course_code"),
                        rs.getString("department"),
                        rs.getString("semester"),
                        rs.getString("time"),
                        rs.getString("day"),
                        rs.getString("prof"),
                        rs.getString("name"),
                        rs.getInt("credit_hours"),
                        rs.getInt("capacity")));
            }
            conn.close();
            return courseList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Course> runQuery(List<String> conditions, List<List<String>> values) {
        String queryString = "select * from Courses";
        if(conditions.size() > 0) {
            queryString += " WHERE (" + conditions.get(0) + ")";
        }
        for(int i = 1; i < conditions.size(); i ++) {
            queryString += "AND (" + conditions.get(i) + ")";
        }
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            PreparedStatement ps = conn.prepareStatement(queryString);
            int pos = 1;
            for(int i = 0; i < values.size(); i++){
                System.out.println(values.get(i));
                for(int j = 0; j < values.get(i).size(); j++){
                    if(queryString.length() > 3 && queryString.charAt(queryString.indexOf("?")-2) == 'E'){
                        ps.setString(pos, values.get(i).get(j) + "%");
                        pos++;
                    }else {
                        ps.setString(pos, values.get(i).get(j));
                        pos++;
                    }
                }
            }
            List<Course> courseList = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                courseList.add(new Course(
                        rs.getString("course_code"),
                        rs.getString("department"),
                        rs.getString("semester"),
                        rs.getString("time"),
                        rs.getString("day"),
                        rs.getString("prof"),
                        rs.getString("name"),
                        rs.getInt("credit_hours"),
                        rs.getInt("capacity")));
            }
            conn.close();
            return courseList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
