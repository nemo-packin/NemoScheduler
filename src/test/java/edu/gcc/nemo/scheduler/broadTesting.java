package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Courses;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class broadTesting {
//    User user = new Student("username", "password", "John", 123456, 2025, "Mechanical Engineering", "Robotics");
    Student student = new Student("burgler69", "gOodPasSwOrD", "Deet", 123456, 2025, "Mechanical Engineering", "Dance");
    Schedule sched = new Schedule("myShedule", "Fall 2023", 0, "", Courses.getInstance(), 1);

    @Test
    void UserUN() {
        assertEquals("burgler69", student.username);
    }

    @Test
    void UserPW() {
        assertEquals("gOodPasSwOrD", student.password);
        assertEquals("gOodPasSwOrD", student.getPassword());
    }

    @Test
    void UserN() {
        assertEquals("Deet", student.name);
    }

    @Test
    void UserID() {
        assertEquals(123456, student.id);
        assertEquals(123456, student.getId());
    }

    @Test
    void UserGY() {
        assertEquals(2025, student.getGradYear());
    }

    @Test
    void UserToString() {
        assertEquals("Deet", student.toString());
    }

    @Test
    void User1() {
        try{
            student.setGradYear(-100800);
            student.setId(3*5);
            System.out.println(student.getId());
            student.setId(Math.decrementExact(43));
            System.out.println(student.getId());
            student.setGradYear(Math.floorMod(223, 8));
            student.printInfo();
            student.loadScheduleFromDB(Courses.getInstance());
            student.editSchedule();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
