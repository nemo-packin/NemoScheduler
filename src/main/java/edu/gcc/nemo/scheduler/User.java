package edu.gcc.nemo.scheduler;

import java.util.List;
import java.util.ArrayList;

public abstract class User {

    protected String username;
    protected String name;
    protected String password;
    protected int id;

    public User(String username, String password, String name, int id){
        this.username = username;
        this.password = password;
        this.name = name;
        this.id = id;
    }

    //Methods
    public void username() {

    }

    public void updateLoadAccountDBInfo(){

    }

    public void editAccount() {

    }

    //NEW METHOD FOR SAVING ACCOUNT TO DATABASE

    public void saveSchedule() {

    }

    public void editSchedule() {

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
