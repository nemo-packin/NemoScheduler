package edu.gcc.nemo.scheduler;

import java.util.*;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static Map<String, Student> studentList;
    private static Map<String, Admin> adminList;
    private static Student stuSignedIn = null;
    private static Admin adminSignedIn = null;

    public static void main(String[] args){
        studentList = new HashMap<>();
        adminList = new HashMap<>();

        System.out.println("Hello nemo packers!");

        System.out.println("Would you like to sign in or create account? \n" +
                "(Type: 'Sign in' or 'Create account' or 'Exit')");
        String input = sc.nextLine().toLowerCase().trim();

        while(true) {
            while (!input.equals("sign in") && !input.equals("create account") && !input.equals("exit")) {
                System.out.println("I'm sorry, I did not understand what you are trying to do. \n" +
                        "Would you like to sign in or create account? (Type: 'Sign in' or 'Create account' or 'Exit')");
                input = sc.nextLine().toLowerCase().trim();
            }
            if(input.equals("exit"))
                break;
            if (input.equals("create account")) {
                createAccount();
            }else if(input.equals("sign in")) {
                login();
            }
            while(stuSignedIn != null || adminSignedIn != null){
                System.out.println("Would you like to sign out or add course to schedule? \n" +
                        "(Type: 'Log out' or 'Add Course' or 'Exit)");
                input = sc.nextLine().toLowerCase().trim();
                // ADD LOG OUT OR ADD COURSE
                if (input.equals("log out") || input.equals("exit")) {
                    stuSignedIn = null;
                    adminSignedIn = null;
                    break;
                } else { //ADDING A COURSE (TYPING NEEDS TO BE EXACT HERE!!!)
                    displayAllCourses();
                    System.out.println("Enter the course code you would like to add (or type 'Exit' or 'Log out'): *Please type out exactly!");
                    input = sc.nextLine().trim(); //NOT DOING 'TOLOWERCASE'!!!!
                    if (input.equals("Log out") || input.equals("Exit")) {
                        stuSignedIn = null;
                        adminSignedIn = null;
                        break;
                    }
                    addCourse(input);
                }
            }
            if(input.equals("exit"))
                break;
            System.out.println("Would you like to sign in or create account? \n" +
                    "(Type: 'Sign in' or 'Create account' or 'Exit')");
            sc.nextLine().toLowerCase().trim();
            input = sc.nextLine().toLowerCase().trim();

        }
        System.out.println("Goodbye nemo-er!");
//        Schedule s = new Schedule("Spring");
//        s.addCourse("COMP 342 A");
//        System.out.println(s.toString());

    }
    public static void createAccount(){
        System.out.println("Did you want to create a student or admin account? (Type 'exit' to go back)");
        String choice = sc.nextLine().toLowerCase().trim();
        while(!choice.equals("student") && !choice.equals("admin") && !choice.equals("exit")){
            System.out.println("I'm sorry, I did not understand what you are trying to do. \n" +
                    "Did you want to create a student or admin account?");
            choice = sc.nextLine().toLowerCase().trim();
        }
        if(choice.equals("exit"))
            return;

        System.out.println("Please enter the login you would like to use:  (Type 'exit' to go back)");
        String login = sc.nextLine().trim();
        while(doesLoginExist(login)) {
            System.out.println("I'm sorry, but that username already exists, please pick a different one");
            login = sc.nextLine().trim();
        }
        if(login.equals("exit"))
            return;

        System.out.println("Please enter the password you would like to use:  (Type 'exit' to go back to home)");
        String password = sc.nextLine().trim();
        if(password.equals("exit"))
            return;

        System.out.println("Please re-enter your password:");
        String confirm = sc.nextLine().trim();
        while(!confirm.equals(password)){
            System.out.println("Password did not match. Please re-enter your password:");
            confirm = sc.nextLine().trim();
        }

        System.out.println("What name would you like your username to be? (Type 'exit' to back to home)");
        String username = sc.nextLine().trim();
        if(username.equals("exit"))
            return;

        if(choice.equals("admin")){
            adminList.put(login, new Admin(login, password, username));
        }else{
            System.out.println("What is your graduation year?");
            int gradYear = sc.nextInt(); // DOUBLE CHECK FOR ERRORS HERE: FOR EXAMPLE IF THEY ENTER STRING
            studentList.put(login, new Student(login, password, username, studentList.size() + 1, gradYear));
        }
        System.out.println("You have successfully created an account!");

    }
    public static void login(){
        System.out.println("To login in please enter your login (Type 'exit' to go back to home)");
        String login = sc.nextLine().toLowerCase().trim();
        if(login.equals("exit"))
            return;
        while(!doesLoginExist(login)){
            System.out.println("That login does not exists");
            System.out.println("To login in please enter your username");
            login = sc.nextLine().trim();
        }
        if(studentList.get(login) != null){
            Student tempStu = studentList.get(login);

            System.out.println("To login in please enter your password");
            String password = sc.nextLine().trim();
            while(!tempStu.checkPassword(password)){
                System.out.println("Password was incorrect, please try again.");
                password = sc.nextLine().trim();
            }
            stuSignedIn = tempStu;
            stuSignedIn.printInfo();
        }else{
            Admin tempAdmin = adminList.get(login);

            System.out.println("To login in please enter your password");
            String password = sc.nextLine().trim();
            while(!tempAdmin.checkPassword(password)){
                System.out.println("Password was incorrect, please try again.");
                password = sc.nextLine().trim();
            }
            adminSignedIn = tempAdmin;
            adminSignedIn.printInfo();
        }
        System.out.println("You successfully logged in!");
    }

    public static boolean doesLoginExist(String login){
        if(studentList.get(login) == null && adminList.get(login) == null)
            return false;
        return true;
    }

    public static void displayAllCourses(){
        if(stuSignedIn != null){
            System.out.println("The following is a list of all the classes: \n");
            for(Course c : stuSignedIn.account.schedule.allCourseList){
                System.out.println(c.toString());
            }
        }else if(adminSignedIn != null){
            System.out.println("The following is a list of all the classes: \n");
            for(Course c : adminSignedIn.account.schedule.allCourseList){
                System.out.println(c.toString());
            }
        }
        else{
            System.out.println("You are not signed in!");
        }
    }

    public static void addCourse(String courseCode){
        if(stuSignedIn != null){
            stuSignedIn.account.schedule.addCourse(courseCode);
            System.out.println("Course successfully added!");
            System.out.println(stuSignedIn.account.schedule.toString());
        }else if(adminSignedIn != null){
            adminSignedIn.account.schedule.addCourse(courseCode);
            System.out.println("Course successfully added!");
            System.out.println(adminSignedIn.account.schedule.toString());
        }
        else{
            System.out.println("You are not signed in!");
        }
    }

}
