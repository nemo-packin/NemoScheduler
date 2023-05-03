package edu.gcc.nemo.scheduler;

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