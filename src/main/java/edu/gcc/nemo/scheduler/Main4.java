package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Admins;
import edu.gcc.nemo.scheduler.DB.Courses;
import edu.gcc.nemo.scheduler.DB.Students;

import java.sql.*;
import java.util.*;

public class Main4 {
    private static Scanner sc = new Scanner(System.in);
    private static Student stuSignedIn = null;
    private static Admin adminSignedIn = null;
    private static Connection conn;
    //singleton objects
    private static Students students;
    private static Admins admins;
    private static Courses courses;
    private static Session session;

    public static void main(String[] args) {
        // created singleton objects
        students = Students.getStudentsInstance();
        admins = Admins.getAdminsInstance();
        courses = Courses.getCoursesInstance();

        session = new Session(admins, students, courses);
        System.out.println("Hello nemo packers!");

        System.out.println("Would you like to sign in or create account? \n" +
                "(Type: 'Sign in' or 'Create account' or 'Exit')");
        String input = sc.nextLine().toLowerCase().trim();

        while (true) {
            while (!input.equals("sign in") && !input.equals("create account") && !input.equals("exit")) {
                System.out.println("I'm sorry, I did not understand what you are trying to do. \n" +
                        "Would you like to sign in or create account? (Type: 'Sign in' or 'Create account' or 'Exit')");
                input = sc.nextLine().toLowerCase().trim();
            }
            if (input.equals("exit"))
                break;
            if (input.equals("create account")) {
                createAccount();
            } else if (input.equals("sign in")) {
                login();
            }

            //*****************************************************************************************

            while (stuSignedIn != null || adminSignedIn != null) {
                System.out.println("Would you like to sign out or add course to schedule? \n" +
                        "(Type: 'Log out' or 'Add Course' or 'Remove Course' or 'Search Student' or 'Exit')");
                input = sc.nextLine().toLowerCase().trim();
                System.out.println(input);
                // ADD LOG OUT OR ADD COURSE
                if (input.equals("log out") || input.equals("exit")) {
                    stuSignedIn = null;
                    adminSignedIn = null;
                    break;
                } else if (input.equals("add course")) { //ADDING A COURSE (TYPING NEEDS TO BE EXACT HERE!!!)
                    displayAllCourses();
                    System.out.println("Enter the course code you would like to add (or type 'Exit' or 'Log out'): *Please type out exactly!");
                    input = sc.nextLine().trim(); //NOT DOING 'TOLOWERCASE'!!!!
                    if (input.equals("log out") || input.equals("exit")) {
                        stuSignedIn = null;
                        adminSignedIn = null;
                        break;
                    }
                    try {
                        addCourse(input);
                    } catch (Exception e) {
                        System.out.println("I'm sorry, I didn't understand that...");
                    }
                } else if (input.equals("remove course")) {
                    displayAllCourses();
                    System.out.println("Enter the course code you would like to remove (or type 'Exit' or 'Log out'): *Please type out exactly!");
                    input = sc.nextLine().trim(); //NOT DOING 'TOLOWERCASE'!!!!
                    if (input.equals("log out") || input.equals("exit")) {
                        stuSignedIn = null;
                        adminSignedIn = null;
                        break;
                    }
                    removeCourse(input);
                } else if (input.equals("search student")) {

                }
            }
            if (input.equals("exit"))
                break;
            System.out.println("Would you like to sign in or create account? \n" +
                    "(Type: 'Sign in' or 'Create account' or 'Exit')");
            sc.nextLine().toLowerCase().trim();
            input = sc.nextLine().toLowerCase().trim();

        }
        System.out.println("Goodbye nemo-er!");

    }

    public static void createAccount() {
        System.out.println("Please enter the username you would like to use:  (Type 'exit' to go back)");
        String username = sc.nextLine().trim();
        while (doesUsernameExist(username)) {
            System.out.println("I'm sorry, but that username already exists, please pick a different one");
            username = sc.nextLine().trim();
        }
        if (username.equals("exit"))
            return;

        int id = -1;
        do {
            System.out.println("Please enter an your id: ");
            while (true) {
                if (sc.hasNextInt()) {
                    break;
                } else {
                    System.out.println("Input is invalid!");
                }
            }
            id = sc.nextInt();
        }while(id == -1 || doesIdExist(id));


        System.out.println("Please enter the password you would like to use:  (Type 'exit' to go back to home)");
        String password = sc.nextLine().trim();
        if (password.equals("exit"))
            return;

        System.out.println("Please re-enter your password:");
        String confirm = sc.nextLine().trim();
        while (!confirm.equals(password)) {
            System.out.println("Password did not match. Please re-enter your password:");
            confirm = sc.nextLine().trim();
        }

        System.out.println("What is your name? (Type 'exit' to back to home)");
        String name = sc.nextLine().trim();
        if (name.equals("exit"))
            return;

        System.out.println("What is your graduation year?");

        //Error Checking
        while (true) {
            if (sc.hasNextInt()) {
                break;
            } else {
                sc.next();
                System.out.println("That's not a number! What is your graduation year?");
            }
        }
        int gradYear = sc.nextInt();
        studentList.put(login, new Student(login, password, username, studentList.size() + 1, gradYear));
        System.out.println("You have successfully created an account!");

    }

    public static void login() {
        System.out.println("To login in please enter your login (Type 'Exit' to go back to home)");
        String login = sc.nextLine().trim();
        while (!doesLoginExist(login) && !login.toLowerCase().equals("exit")) {
            System.out.println("That login does not exists");
            System.out.println("To login in please enter your username");
            login = sc.nextLine().trim();
        }
        if (login.toLowerCase().equals("exit"))
            return;
        if (studentList.get(login) != null) {
            Student tempStu = studentList.get(login);

            System.out.println("To login in please enter your password");
            String password = sc.nextLine().trim();
            while (!tempStu.checkPassword(password)) {
                System.out.println("Password was incorrect, please try again.");
                password = sc.nextLine().trim();
            }
            stuSignedIn = tempStu;
            stuSignedIn.printInfo();
        } else {
            Admin tempAdmin = adminList.get(login);

            System.out.println("To login in please enter your password");
            String password = sc.nextLine().trim();
            while (!tempAdmin.checkPassword(password)) {
                System.out.println("Password was incorrect, please try again.");
                password = sc.nextLine().trim();
            }
            adminSignedIn = tempAdmin;
            adminSignedIn.printInfo();
        }
        System.out.println("You successfully logged in!");
    }

    /**
     * @param username
     * @return "false" if username does not exist, "true" if username exists
     */
    public static boolean doesUsernameExist(String username) {
        if (students.getStudent(username) == null && admins.getAdmin(username) == null)
            return false;
        return true;
    }

    /**
     *
     * @param id
     * @return "false" if id does not exist, "true" if id exists
     */
    public static boolean doesIdExist(int id) {
        if (students.doesStuIdExist(id) || admins.doesAdminIdExist(id))
            return true;
        return false;
    }

    public static void displayAllCourses() {
        if (stuSignedIn != null) {
            System.out.println("The following is a list of all the classes: \n");
            System.out.println(stuSignedIn.schedule.toString());
        } else if (adminSignedIn != null) {
            System.out.println("As an admin you do not have a schedule!");
        } else {
            System.out.println("You are not signed in!");
        }
    }

    public static void addCourse(String courseCode) {
        if (stuSignedIn != null) {
            stuSignedIn.schedule.addCourseToSchedule(courseCode);
            System.out.println(stuSignedIn.schedule.toString());
        } else if (adminSignedIn != null) {
            System.out.println("As an admin you do not have a schedule!");
        } else {
            System.out.println("You are not signed in!");
        }
    }

    public static void removeCourse(String courseCode) {
        if (stuSignedIn != null) {
            stuSignedIn.schedule.removeCourseFromSchedule(courseCode);
            System.out.println(stuSignedIn.schedule.toString());
        } else if (adminSignedIn != null) {
            System.out.println("As an admin you do not have a schedule!");
        } else {
            System.out.println("You are not signed in!");
        }
    }
}
