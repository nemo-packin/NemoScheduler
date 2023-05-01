package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Courses;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Student extends User{
    private int gradYear;
    private String major;
    private String minor;
    protected StatusSheet statusSheet;
    protected Schedule schedule;
    private Connection conn;
    public Student(String username, String password, String name, int id, int gradYear, String major, String minor) {
        super(username, password, name, id);
        this.gradYear = gradYear;
        this.major = major;
        this.minor = minor;
        statusSheet = new StatusSheet();

        try{
            conn =  DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param refCourses this MUST be passed in and called right after Student instance is created
     */
    public void loadScheduleFromDB(Courses refCourses){
        try{
            conn =  DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            PreparedStatement loadS = conn.prepareStatement("select * from Schedules where id = ?");
            loadS.setInt(1, id);
            ResultSet rs = loadS.executeQuery();
            while(rs.next()){
                schedule = new Schedule(rs.getString("name").replace(id + "", ""),
                                            rs.getString("semester"),
                                            rs.getInt("isApproved"),
                                            rs.getString("courses").toLowerCase(),
                                            refCourses,
                                            id);
            }
            loadS.close();
            conn.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * creates a new schedule for the student
     * @param nameForSchedule
     * @param semester
     * @param refCourses
     */
    public void createNewSchedule(String nameForSchedule, String semester, Courses refCourses){
        schedule = new Schedule(nameForSchedule, semester, 0, "", refCourses, id);
    }


    @Override
    public void printInfo() {
        System.out.println(
                "You are a student! Here is your info: " + "\n" +
                "Username: " + username + "\n" +
                "Name: " + name + "\n" +
                "Password: " + password + "\n" +
                "id: " + id + "\n" +
                "Grad year:" + gradYear + "\n" +
                "Majors: " + major + "\n" +
                "Minors: " + minor + "\n");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGradYear() {
        return gradYear;
    }

    public void setGradYear(int gradYear) {
        this.gradYear = gradYear;
    }

    @Override
    public String toString(){
        return name;
    }

}
