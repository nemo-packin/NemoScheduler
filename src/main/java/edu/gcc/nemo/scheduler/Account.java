package edu.gcc.nemo.scheduler;

public class Account {
    protected StatusSheet statusSheet;
    protected Schedule schedule;
    protected String login;
    protected String password;

    public Account(String login, String password){
        statusSheet = new StatusSheet();
        schedule = new Schedule("Spring");
        this.login = login;
        this.password = password;
    }

    // METHODS
    public void serialize() {

    }

    public void editAccount() {

    }

    //GETTERS AND SETTERS
    public String getPassword(){
        return password;
    }
}
