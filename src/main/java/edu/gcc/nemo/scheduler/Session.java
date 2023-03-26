package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Students;
import edu.gcc.nemo.scheduler.DB.Admins;
import edu.gcc.nemo.scheduler.DB.Courses;

import java.sql.*;

public class Session {
    private User user;
    private boolean authenticated = false;

    private Admins refAdmins;
    private Students refStudents;
    private Courses refCourses;

    public Session(Admins admins, Students students, Courses courses) {
        refAdmins = admins;
        refStudents = students;
        refCourses = courses;
    }

    public boolean authenticate(String login, String password) {
        if(refStudents.getStudent(login) != null)
            user = refStudents.getStudent(login);
        else if(refAdmins.getAdmin(login) != null)
            user = refAdmins.getAdmin(login);
        if(user != null && user.account.password.equals(password)) {
            authenticated = true;
        }
        return authenticated;
    }

    public boolean getAuthen(){
        return authenticated;
    }

    void saveSchedule() {

    }

    void editSchedule() {}

    public String searchStudents(String username) {
        return null;
    }

    String searchCourses() {return null;}

    String getSchedule() {return null;}

    String getStatusSheet() {return null;}

}
