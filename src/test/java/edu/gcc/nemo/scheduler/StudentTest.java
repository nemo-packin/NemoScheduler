package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Courses;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

class StudentTest {
    private Courses refCourses;
    private Student student;

    @BeforeEach
    void setUp() {
        refCourses = Courses.getInstance();
        student = new Student("username", "password", "name", 123, 2023, "major", "minor");
        student.createNewSchedule("schedule name", "fall 2023", refCourses);
    }

//    @Test
//    void testLoadScheduleFromDB() throws SQLException {
//        student.loadScheduleFromDB(refCourses);
//        Assertions.assertNotNull(student.schedule);
//    }

    @Test
    void testCreateNewSchedule() {
        Assertions.assertEquals(student.schedule.getName(), "123schedule name");
        Assertions.assertEquals(student.schedule.getSemester(), "fall 2023");
    }

    @Test
    void testGettersAndSetters() {
        Assertions.assertEquals(student.getId(), 123);
        student.setId(456);
        Assertions.assertEquals(student.getId(), 456);
        Assertions.assertEquals(student.getGradYear(), 2023);
        student.setGradYear(2024);
        Assertions.assertEquals(student.getGradYear(), 2024);
    }

    @Test
    void testPrintInfo() {
        Assertions.assertDoesNotThrow(() -> student.printInfo());
    }

    @Test
    void testToString() {
        Assertions.assertEquals(student.toString(), "name");
    }
}