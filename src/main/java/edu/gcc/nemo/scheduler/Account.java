package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Courses;

import java.util.ArrayList;
import java.util.List;

public class Account {
    protected StatusSheet statusSheet;
    protected List<Schedule> scheduleList;
    protected String login;
    protected String password;

    public Account(String login, String password){
        statusSheet = new StatusSheet();
        scheduleList = new ArrayList<>();
        this.login = login;
        this.password = password;
    }

    // METHODS
    public void serialize() {

    }

    public void editAccount() {}


    //FIGURE OUT WHAT IS GOING ON WITH THE METHOD BELOW

    // adds schedule to account
    public void addSchedule(String name, String semester, int isApproved, String courses, Student student, Courses refCourses){
        scheduleList.add(new Schedule(name, semester, isApproved, courses, student, refCourses));
    }

    //GETTERS AND SETTERS
    public String getPassword(){
        return password;
    }
}
