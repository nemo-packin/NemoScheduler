package edu.gcc.nemo.scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CourseTest {

    private Course course;

    @Test
    public void testGetCourseCode() {
        course = new Course("COMP141", "Computer Science", "Fall 2023", "9:00-9:50", "MWF", "Jonathon Hutchins", "Programming I", 3, 30);
        Assertions.assertEquals("COMP141", course.getCourseCode());
    }

    @Test
    public void testGetDepartment() {
        course = new Course("COMP141", "Computer Science", "Fall 2023", "9:00-9:50", "MWF", "Jonathon Hutchins", "Programming I", 3, 30);
        Assertions.assertEquals("Computer Science", course.getDepartment());
    }

    @Test
    public void testGetSemester() {
        course = new Course("COMP141", "Computer Science", "Fall 2023", "9:00-9:50", "MWF", "Jonathon Hutchins", "Programming I", 3, 30);
        Assertions.assertEquals("Fall 2023", course.getSemester());
    }

    @Test
    public void testGetTime() {
        course = new Course("COMP141", "Computer Science", "Fall 2023", "9:00-9:50", "MWF", "Jonathon Hutchins", "Programming I", 3, 30);
        Assertions.assertEquals("9:00-9:50", course.getTime());
    }

    @Test
    public void testGetDay() {
        course = new Course("COMP141", "Computer Science", "Fall 2023", "9:00-9:50", "MWF", "Jonathon Hutchins", "Programming I", 3, 30);
        Assertions.assertEquals("MWF", course.getDay());
    }

    @Test
    public void testGetProf() {
        course = new Course("COMP141", "Computer Science", "Fall 2023", "9:00-9:50", "MWF", "Jonathon Hutchins", "Programming I", 3, 30);
        Assertions.assertEquals("Jonathon Hutchins", course.getProf());
    }

    @Test
    public void testGetName() {
        course = new Course("COMP141", "Computer Science", "Fall 2023", "9:00-9:50", "MWF", "Jonathon Hutchins", "Programming I", 3, 30);
        Assertions.assertEquals("Programming I", course.getName());
    }

    @Test
    public void testGetCreditHours() {
        course = new Course("COMP141", "Computer Science", "Fall 2023", "9:00-9:50", "MWF", "Jonathon Hutchins", "Programming I", 3, 30);
        Assertions.assertEquals(3, course.getCreditHours());
    }

    @Test
    public void testGetCapacity() {
        course = new Course("COMP141", "Computer Science", "Fall 2023", "9:00-9:50", "MWF", "Jonathon Hutchins", "Programming I", 3, 30);
        Assertions.assertEquals(30, course.getCapacity());
    }

    @Test
    public void testToString() {
        course = new Course("COMP141", "Computer Science", "Fall 2023", "9:00-9:50", "MWF", "Jonathon Hutchins", "Programming I", 3, 30);
        Assertions.assertEquals("COMP141 | MWF | 9:00-9:50 | Jonathon Hutchins", course.toString());
    }

    @Test
    public void testCourseInfo() {
        course = new Course("COMP141", "Computer Science", "Fall 2023", "9:00-9:50", "MWF", "Jonathon Hutchins", "Programming I", 3, 30);
        Assertions.assertEquals("Course Code: COMP141\n" +
                "Name: Programming I\n" +
                "Department: Computer Science\n" +
                "Semester: Fall 2023\n" +
                "Time: 9:00-9:50\n" +
                "Day: MWF\n" +
                "Professor: Jonathon Hutchins\n" +
                "Credit Hours: 3\n" +
                "Capacity: 30\n" +
                "------------------------ \n\n", course.courseInfo());
    }
}