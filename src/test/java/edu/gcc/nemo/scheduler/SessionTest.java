package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Admins;
import edu.gcc.nemo.scheduler.DB.Courses;
import edu.gcc.nemo.scheduler.DB.Students;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

public class SessionTest {

    private Admins admins;
    private Students students;
    private Courses courses;
    private Session session;
    private Courses refCourses;


    @BeforeEach
    void setUp() {
        admins = new Admins();
        students = new Students();
        courses = new Courses();
        session = new Session(admins, students, courses);
    }

    @Test
    @DisplayName("Test authentication for student")
    void testAuthenticateForStudent() {
        students.getStudent("jon1");
        assertTrue(session.authenticate("jon1", "123"));
        assertEquals("student", session.getTypeOfUser());
        assertNotNull(session.getStu());
        assertNull(session.getAdmin());
        assertTrue(session.isAuthen());
    }

    @Test
    @DisplayName("Test authentication for admin")
    void testAuthenticateForAdmin() {
        admins.getAdmin("masterDude");
        assertTrue(session.authenticate("master1", "123"));
        assertEquals("admin", session.getTypeOfUser());
        assertNull(session.getStu());
        assertNotNull(session.getAdmin());
        assertTrue(session.isAuthen());
    }

    @Test
    @DisplayName("Test authentication with incorrect credentials")
    void testAuthenticateWithIncorrectCredentials() {
        students.getStudent("hotdog21");
        assertFalse(session.authenticate("username", "incorrect"));
        assertNull(session.getStu());
        assertNull(session.getAdmin());
        assertFalse(session.isAuthen());
    }

    @Test
    @DisplayName("Test authentication with non-existing user")
    void testAuthenticateWithNonExistingUser() {
        assertFalse(session.authenticate("non-existing", "password"));
        assertNull(session.getStu());
        assertNull(session.getAdmin());
        assertFalse(session.isAuthen());
    }

    @Test
    @DisplayName("Test saving schedule")
    void testSaveSchedule() {
        session.authenticate("g", "123");
        session.saveSchedule();
    }

    @Test
    @DisplayName("Test editing schedule")
    void testEditSchedule() {
        refCourses = new Courses();
        refCourses.getCourse("COMP350A");
        refCourses.getCourse("COMP151A");
        session.authenticate("g", "123");
        Schedule schedule = new Schedule("Schedule 1", "Fall 2021", 0, "", refCourses, 1);
//        session.stu = students.getStudent("username");
        session.getStudent("g").createNewSchedule("schedulexx", "Spring", refCourses);
        session.getStu().schedule = schedule;
        session.editSchedule();
    }

    @Test
    @DisplayName("Test searching students")
    void testSearchStudents() {
        admins.getAdmin("master2");
        students.getStudent("SmarfMagoosh");
        students.getStudent("emily");
        students.getStudent("ken");
        session.authenticate("master2", "123");
        Student[] result = session.searchStudents("SmarfMagoosh");
        assertNotNull(result);
        assertEquals(1, result.length-1);
    }

    @Test
    @DisplayName("Test searching courses")
    void testSearchCourses() {
        courses.getCourse("COMP350A");
        courses.getCourse("COMP155A");
        Course[] result = session.searchCourses("COMP350A");
        assertNotNull(result);
//        assertEquals("COMP350A", result[1].getCourseCode());
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

    @Test
    @DisplayName("Test get student for authenticated admin")
    public void testGetStudentForAuthenticatedAdmin() {
        Session session = new Session(admins, students, courses);
        session.authenticate("master1", "123");
        Student result = session.getStudent("jon1");
        Assertions.assertNotNull(result);
        Assertions.assertEquals("123", result.getPassword());
    }

    @Test
    @DisplayName("Test get student for non-authenticated user")
    public void testGetStudentForNonAuthenticatedUser() {
        Session session = new Session(admins, students, courses);
        Student result = session.getStudent("test_student");
        Assertions.assertNull(result);
    }
}