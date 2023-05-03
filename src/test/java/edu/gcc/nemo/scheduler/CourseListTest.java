//package edu.gcc.nemo.scheduler;
//
//import edu.gcc.nemo.scheduler.DB.Courses;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class CourseListTest {
//    //Holder schedule for testing
//    Schedule s = new Schedule("Spring 2024",
//            "Spring",
//            0,
//            "",
//            Courses.getInstance(),
//            0);
//
//    @Test
//    void addCourse() {
//        String test = "Courses in you're schedule include: \n";
//
//        //Adding a course successfully
//        s.addCourse("COMP 444 A");
//        assertEquals(test + "COMP 444 A", s.toString());
//
//        //Adding a course a second time
//        s.addCourse("COMP 444 A");
//        assertEquals(test + "COMP 444 A", s.toString());
//
//        //Adding a course successfully with another course in the schedule
//        s.addCourse("PSYC A");
//        assertEquals(test + "COMP 444 APSYC A", s.toString());
//
//    }
//
//    @Test
//    void removeCourse() {
//        String test = "Courses in you're schedule include: \n";
//
//        //Adding a course successfully
//        s.addCourse("COMP 444 A");
//
//        //Removing a course successfully
//        s.removeCourse("COMP 444 A");
//        assertEquals(test + "", s.toString());
//
//        //Removing a course a second time
//        s.removeCourse("COMP 444 A");
//        assertEquals(test + "", s.toString());
//
//        //Removing a course that has not been in the schedule
//        s.removeCourse("COMPS 999");
//        assertEquals(test + "", s.toString());
//    }
//}