package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Courses;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {
    Schedule s = new Schedule("Spring 2024",
            "Spring",
            0,
            "",
            Courses.getInstance(),
            0);

    @Test
    void approve() {
        s.approve();
        assertEquals(true, s.getApproved());
    }

    @Test
    void testToString() {
        s.addCourse("COMP435A");
        String test = "Courses in you're schedule include: \nCOMP435A | TR | 10:05:00 AM-11:20:00 AM | Jonathan Hutchins";
        assertEquals(s.toString(), test);
    }

    @Test
    void testCalendarView() {
        s.addCourse("COMP435A");
        String test = "============================================\n" +
                "Schedule View\n" +
                "============================================\n" +
                "++++Monday++++\n" +
                "8:00am - \n" +
                "8:15am - \n" +
                "8:30am - \n" +
                "8:45am - \n" +
                "9:00am - \n" +
                "9:15am - \n" +
                "9:30am - \n" +
                "9:45am - \n" +
                "10:00am - \n" +
                "10:15am - \n" +
                "10:30am - \n" +
                "10:45am - \n" +
                "11:00am - \n" +
                "11:15am - \n" +
                "11:30am - \n" +
                "11:45am - \n" +
                "12:00pm - \n" +
                "12:15pm - \n" +
                "12:30pm - \n" +
                "12:45pm - \n" +
                "1:00pm - \n" +
                "1:15pm - \n" +
                "1:30pm - \n" +
                "1:45pm - \n" +
                "2:00pm - \n" +
                "2:15pm - \n" +
                "2:30pm - \n" +
                "2:45pm - \n" +
                "3:00pm - \n" +
                "3:15pm - \n" +
                "3:30pm - \n" +
                "3:45pm - \n" +
                "4:00pm - \n" +
                "4:15pm - \n" +
                "4:30pm - \n" +
                "4:45pm - \n" +
                "5:00pm - \n" +
                "5:15pm - \n" +
                "5:30pm - \n" +
                "5:45pm - \n" +
                "6:00pm - \n" +
                "6:15pm - \n" +
                "6:30pm - \n" +
                "6:45pm - \n" +
                "7:00pm - \n" +
                "7:15pm - \n" +
                "7:30pm - \n" +
                "7:45pm - \n" +
                "++++Tuesday++++\n" +
                "8:00am - \n" +
                "8:15am - \n" +
                "8:30am - \n" +
                "8:45am - \n" +
                "9:00am - \n" +
                "9:15am - \n" +
                "9:30am - \n" +
                "9:45am - \n" +
                "10:00am - COMP435A\n" +
                "10:15am - COMP435A\n" +
                "10:30am - COMP435A\n" +
                "10:45am - COMP435A\n" +
                "11:00am - COMP435A\n" +
                "11:15am - \n" +
                "11:30am - \n" +
                "11:45am - \n" +
                "12:00pm - \n" +
                "12:15pm - \n" +
                "12:30pm - \n" +
                "12:45pm - \n" +
                "1:00pm - \n" +
                "1:15pm - \n" +
                "1:30pm - \n" +
                "1:45pm - \n" +
                "2:00pm - \n" +
                "2:15pm - \n" +
                "2:30pm - \n" +
                "2:45pm - \n" +
                "3:00pm - \n" +
                "3:15pm - \n" +
                "3:30pm - \n" +
                "3:45pm - \n" +
                "4:00pm - \n" +
                "4:15pm - \n" +
                "4:30pm - \n" +
                "4:45pm - \n" +
                "5:00pm - \n" +
                "5:15pm - \n" +
                "5:30pm - \n" +
                "5:45pm - \n" +
                "6:00pm - \n" +
                "6:15pm - \n" +
                "6:30pm - \n" +
                "6:45pm - \n" +
                "7:00pm - \n" +
                "7:15pm - \n" +
                "7:30pm - \n" +
                "7:45pm - \n" +
                "++++Wednesday++++\n" +
                "8:00am - \n" +
                "8:15am - \n" +
                "8:30am - \n" +
                "8:45am - \n" +
                "9:00am - \n" +
                "9:15am - \n" +
                "9:30am - \n" +
                "9:45am - \n" +
                "10:00am - \n" +
                "10:15am - \n" +
                "10:30am - \n" +
                "10:45am - \n" +
                "11:00am - \n" +
                "11:15am - \n" +
                "11:30am - \n" +
                "11:45am - \n" +
                "12:00pm - \n" +
                "12:15pm - \n" +
                "12:30pm - \n" +
                "12:45pm - \n" +
                "1:00pm - \n" +
                "1:15pm - \n" +
                "1:30pm - \n" +
                "1:45pm - \n" +
                "2:00pm - \n" +
                "2:15pm - \n" +
                "2:30pm - \n" +
                "2:45pm - \n" +
                "3:00pm - \n" +
                "3:15pm - \n" +
                "3:30pm - \n" +
                "3:45pm - \n" +
                "4:00pm - \n" +
                "4:15pm - \n" +
                "4:30pm - \n" +
                "4:45pm - \n" +
                "5:00pm - \n" +
                "5:15pm - \n" +
                "5:30pm - \n" +
                "5:45pm - \n" +
                "6:00pm - \n" +
                "6:15pm - \n" +
                "6:30pm - \n" +
                "6:45pm - \n" +
                "7:00pm - \n" +
                "7:15pm - \n" +
                "7:30pm - \n" +
                "7:45pm - \n" +
                "++++Thursday++++\n" +
                "8:00am - \n" +
                "8:15am - \n" +
                "8:30am - \n" +
                "8:45am - \n" +
                "9:00am - \n" +
                "9:15am - \n" +
                "9:30am - \n" +
                "9:45am - \n" +
                "10:00am - COMP435A\n" +
                "10:15am - COMP435A\n" +
                "10:30am - COMP435A\n" +
                "10:45am - COMP435A\n" +
                "11:00am - COMP435A\n" +
                "11:15am - \n" +
                "11:30am - \n" +
                "11:45am - \n" +
                "12:00pm - \n" +
                "12:15pm - \n" +
                "12:30pm - \n" +
                "12:45pm - \n" +
                "1:00pm - \n" +
                "1:15pm - \n" +
                "1:30pm - \n" +
                "1:45pm - \n" +
                "2:00pm - \n" +
                "2:15pm - \n" +
                "2:30pm - \n" +
                "2:45pm - \n" +
                "3:00pm - \n" +
                "3:15pm - \n" +
                "3:30pm - \n" +
                "3:45pm - \n" +
                "4:00pm - \n" +
                "4:15pm - \n" +
                "4:30pm - \n" +
                "4:45pm - \n" +
                "5:00pm - \n" +
                "5:15pm - \n" +
                "5:30pm - \n" +
                "5:45pm - \n" +
                "6:00pm - \n" +
                "6:15pm - \n" +
                "6:30pm - \n" +
                "6:45pm - \n" +
                "7:00pm - \n" +
                "7:15pm - \n" +
                "7:30pm - \n" +
                "7:45pm - \n" +
                "++++Friday++++\n" +
                "8:00am - \n" +
                "8:15am - \n" +
                "8:30am - \n" +
                "8:45am - \n" +
                "9:00am - \n" +
                "9:15am - \n" +
                "9:30am - \n" +
                "9:45am - \n" +
                "10:00am - \n" +
                "10:15am - \n" +
                "10:30am - \n" +
                "10:45am - \n" +
                "11:00am - \n" +
                "11:15am - \n" +
                "11:30am - \n" +
                "11:45am - \n" +
                "12:00pm - \n" +
                "12:15pm - \n" +
                "12:30pm - \n" +
                "12:45pm - \n" +
                "1:00pm - \n" +
                "1:15pm - \n" +
                "1:30pm - \n" +
                "1:45pm - \n" +
                "2:00pm - \n" +
                "2:15pm - \n" +
                "2:30pm - \n" +
                "2:45pm - \n" +
                "3:00pm - \n" +
                "3:15pm - \n" +
                "3:30pm - \n" +
                "3:45pm - \n" +
                "4:00pm - \n" +
                "4:15pm - \n" +
                "4:30pm - \n" +
                "4:45pm - \n" +
                "5:00pm - \n" +
                "5:15pm - \n" +
                "5:30pm - \n" +
                "5:45pm - \n" +
                "6:00pm - \n" +
                "6:15pm - \n" +
                "6:30pm - \n" +
                "6:45pm - \n" +
                "7:00pm - \n" +
                "7:15pm - \n" +
                "7:30pm - \n" +
                "7:45pm - \n" +
                "============================================\n";
        assertEquals(s.calendarView(), test);
    }

    @Test
    void getApproved() {
        s.approve();
        assertEquals(true, s.getApproved());
    }

    @Test
    void getSemester() {
        assertEquals("Spring", s.getSemester());
    }

    @Test
    void setSemester() {
        s.setSemester("Fall 2023");
        assertEquals("Fall 2023", s.getSemester());
    }

    private Schedule schedule;
    private CourseList courseList;
    private Courses refCourses;

    @Test
    public void testAddCourse() {
        refCourses = new Courses();
        refCourses.getCourse("COMP350A");
        courseList = new CourseList();
        schedule = new Schedule("Schedule 1", "Fall 2021", 0, "", refCourses, 1);
        assertTrue(schedule.addCourse("HUMA303A"));
    }

    @Test
    public void testApprove() {
        refCourses = new Courses();
        refCourses.getCourse("COMP350A");
        courseList = new CourseList();
        schedule = new Schedule("Schedule 1", "Fall 2021", 0, "", refCourses, 1);
        schedule.approve();
        assertTrue(schedule.getApproved());
    }

    @Test
    public void testGetSemester() {
        refCourses = new Courses();
        refCourses.getCourse("COMP350A");
        courseList = new CourseList();
        schedule = new Schedule("Schedule 1", "Fall 2021", 0, "", refCourses, 1);
        assertEquals("Fall 2021", schedule.getSemester());
    }

    @Test
    public void testSetSemester() {
        refCourses = new Courses();
        refCourses.getCourse("COMP350A");
        courseList = new CourseList();
        schedule = new Schedule("Schedule 1", "Fall 2021", 0, "", refCourses, 1);
        schedule.setSemester("Spring 2022");
        assertEquals("Spring 2022", schedule.getSemester());
    }

    @Test
    public void testGetName() {
        refCourses = new Courses();
        refCourses.getCourse("COMP350A");
        courseList = new CourseList();
        schedule = new Schedule("Schedule 1", "Fall 2021", 0, "", refCourses, 1);
        assertEquals("1Schedule 1", schedule.getName());
    }

    @Test
    public void testGetCourses() {
        refCourses = new Courses();
        refCourses.getCourse("COMP350A");
        courseList = new CourseList();
        schedule = new Schedule("Schedule 1", "Fall 2021", 0, "", refCourses, 1);
        schedule.addCourse("MATH161A");
        schedule.addCourse("COMP222A");
        assertEquals("MATH161A,COMP222A", schedule.getCourses());
    }

    @Test
    public void testGetCourseList() {
        refCourses = new Courses();
        refCourses.getCourse("COMP350A");
        courseList = new CourseList();
        schedule = new Schedule("Schedule 1", "Fall 2021", 0, "", refCourses, 1);
        assertNotNull(schedule.getCourseList());
        assertEquals(courseList.courses.toString(), schedule.getCourseList().courses.toString());
    }
}