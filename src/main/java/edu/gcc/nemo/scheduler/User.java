package edu.gcc.nemo.scheduler;

import java.util.List;
import java.util.ArrayList;

public abstract class User {

    protected StatusSheet statusSheet;
    protected List<Schedule> scheduleList;
    protected String login;
    protected String name;
    protected String password;
    protected int id;

    public User(String login, String password, String name, int id){
        statusSheet = new StatusSheet();
        scheduleList = new ArrayList<>();
        this.login = login;
        this.password = password;
        this.name = name;
        this.id = id;
    }

    //Methods
    public void login() {

    }

    public void updateLoadAccountDBInfo(){

    }

    public void serialize() {

    }

    public void editAccount() {

    }

    //NEW METHOD FOR SAVING ACCOUNT TO DATABASE

    public void saveSchedule() {

    }

    public void editSchedule() {

    }

    public List<Course> searchClasses() {
        return null;
    }

    //NEW
    public boolean checkPassword(String pass){
        if(pass.equals(password))
            return true;
        return false;
    }

    public int getId() {
        return id;
    }

    public String getPassword(){
        return password;
    }

    public abstract void printInfo();



}
