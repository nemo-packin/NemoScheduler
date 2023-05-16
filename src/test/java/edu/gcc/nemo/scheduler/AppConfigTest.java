package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Admins;
import edu.gcc.nemo.scheduler.DB.Courses;
import edu.gcc.nemo.scheduler.DB.Students;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppConfigTest {

    @Test
    public void testAdminsBean() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Admins admins = context.getBean(Admins.class);
        Assertions.assertNotNull(admins);
    }

    @Test
    public void testStudentsBean() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Students students = context.getBean(Students.class);
        Assertions.assertNotNull(students);
    }

    @Test
    public void testCoursesBean() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Courses courses = context.getBean(Courses.class);
        Assertions.assertNotNull(courses);
    }
}