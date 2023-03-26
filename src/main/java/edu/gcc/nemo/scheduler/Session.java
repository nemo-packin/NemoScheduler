package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Students;
import edu.gcc.nemo.scheduler.DB.Admins;
import edu.gcc.nemo.scheduler.DB.Courses;

import java.sql.*;
import java.util.ArrayList;

public class Session {
//    private User user;
    private Student stu;
    private Admin admin;
    private String typeOfUser;
    private boolean authenticated = false;

    private Admins refAdmins;
    private Students refStudents;
    private Courses refCourses;

    public Session(Admins admins, Students students, Courses courses) {
        refAdmins = admins;
        refStudents = students;
        refCourses = courses;
        stu = null;
        admin = null;
        typeOfUser = "";
    }

    public boolean authenticate(String login, String password) {
        if(refStudents.getStudent(login) != null) {
            stu = refStudents.getStudent(login);
            typeOfUser = "student";
        }
        else if(refAdmins.getAdmin(login) != null) {
            admin = refAdmins.getAdmin(login);
            typeOfUser = "admin";
        }else{
            System.out.println("Failed to authenticate!");
        }
        if(admin != null && admin.account.password.equals(password)) {
            authenticated = true;
        }else if(stu != null && stu.account.password.equals(password)){
            authenticated = true;
        }
        return authenticated;
    }

    public boolean isAuthen(){
        return authenticated;
    }

    void saveSchedule() {

    }

    void editSchedule() {}

    /**
     * @param stuNameSearchVal is the name the user is searching for
     * @return returns an arraylist of all names that contains the search value
     */
    public ArrayList<String> searchStudents(String stuNameSearchVal) {
        if(authenticated && typeOfUser.equals("admin")){
            ArrayList<String> listOfStuNames = refStudents.getListOfAllStudentUsernames();
            ArrayList<String> resultMatches = new ArrayList<>();
            for(String names : listOfStuNames){
                if(names.contains(stuNameSearchVal))
                    resultMatches.add(names);
            }
            return resultMatches;
        }
        System.out.println("You do not have proper credentials to search for " + stuNameSearchVal + "!");
        return null;
    }

    String searchCourses() {return null;}

    public String getSchedule() {
        return null;
    }

    /**
     * this is used for an admin specifically selecting a student
     * @param username
     * @return returns the instance of that student
     */
    public Student getStudent(String username){
        if(authenticated && typeOfUser.equals("admin")){
            return refStudents.getStudent(refStudents.getLogin(username));
        }
        System.out.println("You do not have proper credentials!");
        return null;
    }

    String getStatusSheet() {return null;}

    /**
     * @returns the type of user on this session
     */
    public String getTypeOfUser(){
        return typeOfUser;
    }

}
