package edu.gcc.nemo.scheduler;

import java.util.List;

public abstract class User {
    protected Account account;
    protected String name;

    public User(String login, String password, String name){
        this.name = name;
        account = new Account(login, password);
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

    public abstract void printInfo();

}
