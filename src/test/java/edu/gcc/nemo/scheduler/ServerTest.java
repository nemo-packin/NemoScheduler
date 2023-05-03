//package edu.gcc.nemo.scheduler;
//
//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.SpringApplication;
//
//class ServerTest {
//
//    @BeforeAll
//    static void setUp() {
//        // Initialize the Spring application context
//        SpringApplication.run(Main.class);
//    }
//
//    @Test
//    void testSingletonObjects() {
//        // Verify that the singleton objects are not null
//        assertNotNull(Server.students);
//        assertNotNull(Server.admins);
//        assertNotNull(Server.courses);
//    }
//
//    @Test
//    void testSession() {
//        // Verify that the session is created with the correct parameters
//        Session session = new Session(Server.admins, Server.students, Server.courses);
//        assertSame(Server.admins, session.getAdmins());
//        assertSame(Server.students, session.getStudents());
//        assertSame(Server.courses, session.getCourses());
//    }
//}