package edu.gcc.nemo.scheduler;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.gcc.nemo.scheduler.util.PopulateCourseDB;
import edu.gcc.nemo.scheduler.DB.Courses;

import java.sql.Statement;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


import org.junit.jupiter.api.Test;

public class PopulateCoursesDBTest {

    private static final String TEST_DB_URL = "jdbc:sqlite::memory:";
    private static final String TEST_CSV_FILE = "2020.csv";
    private static final String TEST_SEMESTER = "Test Semester";
    private static final String TEST_COURSE_CODE = "COMP1234";
    private static final String TEST_DEPARTMENT = "COMP";
    private static final String TEST_TIME = "9:00 AM - 12:00 PM";
    private static final String TEST_DAY = "MTWRF";
    private static final String TEST_PROF = "John Doe";
    private static final String TEST_NAME = "Test Course";
    private static final int TEST_CREDIT_HOURS = 3;
    private static final int TEST_CAPACITY = 20;

    private Connection conn;
    private Statement statement;

    @Test
    public void testParseFile() {
        String input = "crs_comp1,crs_comp2,crs_comp3,begin_tim,end_tim,monday_cde,tuesday_cde,wednesday_cde,thursday_cde,friday_cde,first_name,last_name,crs_title,credit_hrs,crs_capacity\n" +
                "COMP,SCI,101,10:00,11:30,Y,Y,Y,Y,Y,John,Doe,Intro to CS,3,30\n" +
                "MATH,211,,13:00,14:30,Y,Y,Y,Y,Y,Jane,Doe,Calculus I,4,25\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        List<Course> courseList = PopulateCourseDB.parseFile(inputStream);
        assertEquals(2, courseList.size());
        Course course1 = courseList.get(0);
        assertEquals("COMPSCI101", course1.getCourseCode());
        assertEquals("COMP", course1.getDepartment());
        assertEquals("10:00-11:30", course1.getTime());
        assertEquals("YYYYY", course1.getDay());
        assertEquals("John Doe", course1.getProf());
        assertEquals("Intro to CS", course1.getName());
        assertEquals(3, course1.getCreditHours());
        assertEquals(30, course1.getCapacity());
        Course course2 = courseList.get(1);
        assertEquals("MATH211", course2.getCourseCode());
        assertEquals("MATH", course2.getDepartment());
        assertEquals("13:00-14:30", course2.getTime());
        assertEquals("YYYYY", course2.getDay());
        assertEquals("Jane Doe", course2.getProf());
        assertEquals("Calculus I", course2.getName());
        assertEquals(4, course2.getCreditHours());
        assertEquals(25, course2.getCapacity());
    }

//    @Test
//    void testMainWithValidArgs() throws SQLException {
//        conn = DriverManager.getConnection(TEST_DB_URL);
//        statement = conn.createStatement();
//        statement.executeUpdate("create table Courses (id integer primary key, course_code text, department text, semester text, time text, day text, prof text, name text, credit_hours integer, capacity integer)");
//
//        // Given
//        String[] args = {TEST_CSV_FILE, TEST_SEMESTER};
//        InputStream inputStream = new ByteArrayInputStream("crs_comp1,crs_comp2,crs_comp3,begin_tim,end_tim,monday_cde,tuesday_cde,wednesday_cde,thursday_cde,friday_cde,first_name,last_name,crs_title,credit_hrs,crs_capacity\nCOMP,123,4,9:00 AM,12:00 PM,M,T,W,R,F,John,Doe,Test Course,3,20\n".getBytes());
//
//        // When
//        System.setIn(inputStream);
//        PopulateCourseDB.main(args);
//
//        // Then
//        ResultSet r = statement.executeQuery("select * from Courses");
//        assertTrue(r.next());
//        assertEquals(TEST_COURSE_CODE, r.getString("course_code"));
//        assertEquals(TEST_DEPARTMENT, r.getString("department"));
//        assertEquals(TEST_SEMESTER, r.getString("semester"));
//        assertEquals(TEST_TIME, r.getString("time"));
//        assertEquals(TEST_DAY, r.getString("day"));
//        assertEquals(TEST_PROF, r.getString("prof"));
//        assertEquals(TEST_NAME, r.getString("name"));
//        assertEquals(TEST_CREDIT_HOURS, r.getInt("credit_hours"));
//        assertEquals(TEST_CAPACITY, r.getInt("capacity"));
//        assertFalse(r.next());
//    }

    @Test
    void testParseFile2() {
        // Test that parseFile method returns a list of courses from a valid CSV input
        String csvInput = "crs_comp1,crs_comp2,crs_comp3,begin_tim,end_tim,monday_cde,tuesday_cde,wednesday_cde,thursday_cde,friday_cde,first_name,last_name,crs_title,credit_hrs,crs_capacity\n" +
                "CSC,100L,001,09:00,10:30,Y,Y,,Y,,John,Doe,Intro to Programming,4,30\n" +
                "CSC,200L,001,13:00,14:30,Y,Y,,,Y,John,Doe,Data Structures,4,25\n" +
                "CSC,300L,001,10:30,12:00,,Y,Y,Y,,Y,Jane,Smith,Algorithms,4,20\n";
        InputStream inputStream = new ByteArrayInputStream(csvInput.getBytes());
        List<Course> courseList = PopulateCourseDB.parseFile(inputStream);

        assertEquals(3, courseList.size());
        assertEquals("CSC100L001", courseList.get(0).getCourseCode());
        assertEquals("CSC200L001", courseList.get(1).getCourseCode());
        assertEquals("CSC300L001", courseList.get(2).getCourseCode());
    }

//    @Test
//    public void testGetCourse() {
//        String[] headers = {"crs_comp1", "crs_comp2", "crs_comp3", "begin_tim", "end_tim", "monday_cde", "tuesday_cde", "wednesday_cde", "thursday_cde", "friday_cde", "first_name", "last_name", "crs_title", "credit_hrs", "crs_capacity"};
//        String[] values = {"COMP", "SCI", "101", "10:00", "11:30", "Y", "Y", "Y", "Y", "Y", "John", "Doe", "Intro to CS", "3", "30"};
//        Course course = PopulateCourseDB(PopulateCourseDB.zipToMap(headers, values));
//        assertEquals("COMPSCI101", course.getCourseCode());
//        assertEquals("COMP", course.getDepartment());
//        assertEquals("10:00-11:30", course.getTime());
//        assertEquals("Y Y Y Y Y", course.getDay());
//        assertEquals("John Doe", course.getProf());
//        assertEquals("Intro to CS", course.getName());
//        assertEquals(3, course.getCreditHours());
//        assertEquals(30, course.getCapacity());
//    }
}