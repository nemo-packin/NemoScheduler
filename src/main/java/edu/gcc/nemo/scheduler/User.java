package edu.gcc.nemo.scheduler;

import java.util.List;

public abstract class User {
    protected Account account;
    protected String name;
    protected int id;

    public User(String login, String password, String name, int id){
        this.name = name;
        this.id = id;
        account = new Account(login, password, id);
    }

    //Methods
    public void login() {

    }

    public void updateLoadAccountDBInfo(){

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
        if(pass.equals(account.getPassword()))
            return true;
        return false;
    }

    public int getId() {
        return id;
    }

    public abstract void printInfo();

}
