//package edu.gcc.nemo.scheduler;
//
//import edu.gcc.nemo.scheduler.DB.Admins;
//import edu.gcc.nemo.scheduler.DB.Courses;
//import edu.gcc.nemo.scheduler.DB.Students;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.*;
//
//@SpringBootTest
//class SessionTest {
//    private Admins admins;
//    private Students students;
//    private Courses courses;
//    private Session session;
//
//    @BeforeEach
//    void setUp() {
//        admins = new Admins();
//        students = new Students();
//        courses = Courses.getInstance();
//        session = new Session(admins, students, courses);
//    }
//
//    @Test
//    void testAuthenticateAdmin() {
//        admins.addAdmin("admin", "password");
//        boolean result = session.authenticate("admin", "password");
//        Assertions.assertTrue(result);
//        Assertions.assertEquals("admin", session.userType());
//    }
//
//    @Test
//    void testAuthenticateStudent() {
//        students.addStudent("student", "password");
//        boolean result = session.authenticate("student", "password");
//        Assertions.assertTrue(result);
//        Assertions.assertEquals("student", session.userType());
//    }
//
//    @Test
//    void testAuthenticateInvalidUser() {
//        boolean result = session.authenticate("invalid", "password");
//        Assertions.assertFalse(result);
//        Assertions.assertEquals("", session.userType());
//    }
//
//    @Test
//    void testAuthenticateInvalidPassword() {
////        students.addStudent("student", "password");
//        students.
//        boolean result = session.authenticate("student", "invalid");
//        Assertions.assertFalse(result);
//        Assertions.assertEquals("", session.userType());
//    }
//
//    @Test
//    void testGetAuth() {
//        boolean result = session.getAuth();
//        Assertions.assertFalse(result);
//    }
//
//    @Test
//    void testLogout() {
//        session.logout();
//        Assertions.assertFalse(session.getAuth());
//        Assertions.assertEquals("", session.userType());
//    }
//
//    @Test
//    void testNewCalendar() {
//        students.addStudent("student", "password");
//        session.authenticate("student", "password");
//        Map<String, String> data = new HashMap<>();
//        data.put("nameForSchedule", "schedule");
//        data.put("semester", "Fall 2021");
//        session.newCalendar(data);
//        List<List<String>> result = session.getCalendar();
//        Assertions.assertEquals(0, result.size());
//    }
//
//    @Test
//    void testGetCalendar() {
//        students.addStudent("student", "password");
//        session.authenticate("student", "password");
//        Map<String, String> data = new HashMap<>();
//        data.put("nameForSchedule", "schedule");
//        data.put("semester", "Fall 2021");
//        session.newCalendar(data);
//        List<List<String>> result = session.getCalendar();
//        Assertions.assertEquals(3, result.size());
//        Assertions.assertEquals(Collections.emptyList(), result.get(0));
//        Assertions.assertEquals(Collections.emptyList(), result.get(1));
//        Assertions.assertEquals(Collections.emptyList(), result.get(2));
//    }
//}