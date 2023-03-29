package edu.gcc.nemo.scheduler.util;

import edu.gcc.nemo.scheduler.Course;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PopulateCourseDB {
    /***
     * PopulateCourseDB: A utility to write a csv list of courses into the course database.
     *
     * Changelog:
     * Ken, 8/13: Initial Implementation
     */

    private static String curSemester;
    private static Connection conn;
    private static Statement statement;
    private static PreparedStatement addCourseStatement;


    /*
    main: Read arguments from command line to populate course database.    */
    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Usage: PopulateCourseDB [course_csv]  [semester]");
        } else {
            curSemester = args[1];
            String fileName = args[0];
            System.out.println("Reading file " + fileName + " for semester " + curSemester);
            ClassLoader c = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = c.getResourceAsStream(fileName);
            List<Course> courseList = parseFile(inputStream);
            try {
                conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");

                statement = conn.createStatement();
                addCourseStatement = conn.prepareStatement("insert into Courses (course_code, department, semester, time, day, prof, name, credit_hours, capacity) values (?, ?, ?, ?, ?, ?, ?, ?, ?);");
                courseList.forEach(course -> {
                    try {
                        addCourseStatement.setString(1, course.getCourseCode());
                        addCourseStatement.setString(2, course.getDepartment());
                        addCourseStatement.setString(3, course.getSemester());
                        addCourseStatement.setString(4, course.getTime());
                        addCourseStatement.setString(5, course.getDay());
                        addCourseStatement.setString(6, course.getProf());
                        addCourseStatement.setString(7, course.getName());
                        addCourseStatement.setInt(8, course.getCreditHours());
                        addCourseStatement.setInt(9, course.getCapacity());
                        addCourseStatement.addBatch();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                conn.setAutoCommit(false);
                addCourseStatement.executeBatch();
                conn.setAutoCommit(true);
                addCourseStatement.close();
                ResultSet r = statement.executeQuery("select * from Courses");
                while (r.next()) {
                    System.out.println(r.getString(2));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                        System.out.println("Closed database connection successfully.");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

    }

    /*
    parseFile: Use CSVReader to process inputstream into a list of courses
     */
    public static List<Course> parseFile(InputStream inputStream) {
        try {
            CSVReader c = new CSVReader(inputStream);
            return c.rows().map(PopulateCourseDB::getCourse).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    getCourse: Helper method to build a course from the map of keys and values generated for a row in CSVReader
     */
    private static Course getCourse(Map<String, String> m) {
        return new Course(m.get("crs_comp1") + m.get("crs_comp2") + m.get("crs_comp3"),
                m.get("crs_comp1"),
                curSemester,
                m.get("begin_tim") + '-' + m.get("end_tim"),
                m.get("monday_cde") + m.get("tuesday_cde") + m.get("wednesday_cde") + m.get("thursday_cde") + m.get("friday_cde"),
                m.get("first_name") + " " + m.get("last_name"),
                m.get("crs_title"),
                parseInt(m.get("credit_hrs")),
                parseInt(m.get("crs_capacity")));
    }

    /*
    parseInt: Helper method to parse int or return a reasonable value if it can't be parsed.
     */
    private static Integer parseInt(String toParse) {
        try {
            return Integer.parseInt(toParse);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
