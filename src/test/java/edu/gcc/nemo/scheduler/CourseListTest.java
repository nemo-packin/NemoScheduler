package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Courses;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CourseListTest {
    private CourseList courseList;

    @Test
    public void testAddCourse() {
        Courses courses = Courses.getInstance();
        courseList = new CourseList(courses);
        // Test adding a course that does not overlap with any existing courses
        assertTrue(courseList.addCourse("MATH161B"));
        assertEquals(1, courseList.courses.size());

        // Test adding a course that overlaps with an existing course
        assertFalse(courseList.addCourse("MATH161B"));
        assertEquals(1, courseList.courses.size());
    }

    @Test
    public void testRemoveCourse() {
        Courses courses = Courses.getInstance();
        courseList = new CourseList(courses);
        // Test removing a course that exists in the schedule
        courseList.addCourse("HUMA303A");
        assertTrue(courseList.removeCourse("HUMA303A"));
        assertEquals(0, courseList.courses.size());

        // Test removing a course that does not exist in the schedule
        assertFalse(courseList.removeCourse("MATH101"));
        assertEquals(0, courseList.courses.size());
    }

    @Test
    public void testCheckOverlap() {
        Courses courses = Courses.getInstance();
        courseList = new CourseList(courses);
        // Test checking overlap with a course that does not overlap with any existing courses
        Course c1 = new Course("MATH101", "Mathematics", "Fall", "9:00AM-10:00AM", "Monday", "Dr. Smith", "Introduction to Calculus", 3, 30);
        assertFalse(courseList.checkOverlap(c1));

        // Test checking overlap with a course that overlaps with an existing course
        Course c2 = new Course("CHEM105A", "Chemistry", "Fall", "9:00AM-10:00AM", "Monday", "Dr. Jones", "Chemistry for engineers", 4, 30);
        courseList.addCourse("CHEM105A");
        assertTrue(courseList.checkOverlap(c2));
    }

    @Test
    public void testCoursesAsString() {
        Courses courses = Courses.getInstance();
        courseList = new CourseList(courses);
        // Test converting an empty course list to a string
        assertEquals("", courseList.coursesAsString());

        // Test converting a non-empty course list to a string
        courseList.addCourse("COMP435A");
        assertEquals("COMP435A | TR | 10:05:00 AM-11:20:00 AM | Jonathan Hutchins,", courseList.coursesAsString());
    }
}