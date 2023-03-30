package edu.gcc.nemo.scheduler;

import java.util.List;

public class Admin extends User {

    public Admin(String login, String password, String name, int id){
        super(login, password, name, id);

    }

    @Override
    public void printInfo() {
        System.out.println(
                "You are an admin! Here is your info: " + "\n" +
                "Login: " + account.login + "\n" +
                "Name: " + name + "\n" +
                "Password: " + account.password + "\n");
    }

    //Methods
    public List<Student> searchStudent() {
        return null;
    }

    @Override
    public String toString(){
        return name;
    }


}
