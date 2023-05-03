package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Admins;
import edu.gcc.nemo.scheduler.DB.Courses;
import edu.gcc.nemo.scheduler.DB.Students;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import edu.gcc.nemo.scheduler.DB.Admins;
import edu.gcc.nemo.scheduler.DB.Courses;
import edu.gcc.nemo.scheduler.DB.Students;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SessionTest {
    private Admins admins;
    private Students students;
    private Courses courses;

    @BeforeEach
    public void setup() {
        // initialize the test data
        admins = new Admins();
        students = Students.getInstance();
        courses = Courses.getInstance();
        // add test students and admins
//        students.addStudent(new Student("test_student", "password"));
//        admins.addAdmin(new Admin("test_admin", "password"));
//        studeStudents.getInstance();
    }

    @Test
    @DisplayName("Test authentication with invalid credentials")
    public void testAuthenticateWithInvalidCredentials() {
        Session session = new Session(admins, students, courses);
        boolean result = session.authenticate("test_student", "wrong_password");
        Assertions.assertFalse(result);
        Assertions.assertNull(session.getStu());
    }

    @Test
    @DisplayName("Test get status sheet for non-authenticated user")
    public void testGetStatusSheetForNonAuthenticatedUser() {
        Session session = new Session(admins, students, courses);
        String result = session.getStatusSheet();
        Assertions.assertNull(result);
    }

//    @Test
//    @DisplayName("Test get student for authenticated admin")
//    public void testGetStudentForAuthenticatedAdmin() {
//        Session session = new Session(admins, students, courses);
//        session.authenticate("test_admin", "password");
//        Student result = session.getStudent("test_student");
//        Assertions.assertNotNull(result);
////        Assertions.assertEquals("test_student", result.getUsername());
//    }

    @Test
    @DisplayName("Test get student for non-authenticated user")
    public void testGetStudentForNonAuthenticatedUser() {
        Session session = new Session(admins, students, courses);
        Student result = session.getStudent("test_student");
        Assertions.assertNull(result);
    }
}