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
            while (stuSignedIn != null || adminSignedIn != null) {
                System.out.println("Would you like to sign out or add course to schedule? \n" +
                        "(Type: 'Log out' or 'Add Course' or 'Remove Course' or 'Search Student' or\n" +
                        "'Show Schedule' or 'Create Schedule' or 'Exit')");
                input = sc.nextLine().toLowerCase().trim();
                System.out.println(input);
                // ADD LOG, OUT, OR ADD COURSE
                if (input.equals("log out") || input.equals("exit")) {
                    stuSignedIn = null;
                    adminSignedIn = null;
                    break;
                } else if(input.equals("create schedule")){
                    stuCreateSchedule();
                }else if (input.equals("add course")) { //ADDING A COURSE (TYPING NEEDS TO BE EXACT HERE!!!)
                    if(stuSignedIn == null) {
                        System.out.println("You cannot 'Add Course' as an admin!");
                    }else if(stuSignedIn.schedule == null){
                        System.out.println("You must first create a schedule to add courses to!");
                    }else{
                        displayAllSchedule();
                        System.out.println("Enter the course code you would like to add (or type 'Exit' or 'Log out'):");
                        input = sc.nextLine().trim().toUpperCase();
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
                    }
                } else if (input.equals("remove course")) {
                    if(stuSignedIn == null) {
                        System.out.println("You cannot 'Add Course' as an admin!");
                    }else if(stuSignedIn.schedule == null){
                        System.out.println("You must first create a schedule to add courses to!");
                    }else{
                        displayAllSchedule();
                        System.out.println("Enter the course code you would like to remove (or type 'Exit' or 'Log out'): *Please type out exactly!");
                        input = sc.nextLine().trim(); //NOT DOING 'TOLOWERCASE'!!!!
                        if (input.equals("log out") || input.equals("exit")) {
                            stuSignedIn = null;
                            adminSignedIn = null;
                            break;
                        }
                        removeCourse(input);
                    }
                } else if (input.equals("show schedule")) {
                    if(stuSignedIn == null) {
                        System.out.println("You cannot 'Add Course' as an admin!");
                    }else if(stuSignedIn.schedule == null){
                        System.out.println("You must first create a schedule to add courses to!");
                    }else{
                        displayAllSchedule();
                    }
                } else if (input.equals("search student")) {

                }
            }
            if (input.equals("exit"))
                break;
            System.out.println("Would you like to sign in or create account? \n" +
                    "(Type: 'Sign in' or 'Create account' or 'Exit')");
            sc.nextLine();
//            input = sc.nextLine().toLowerCase().trim();

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

        System.out.println("what is you major?  If you don't have a major, please type 'NA'");
        String major = sc.nextLine().trim();
        while (major.equals("")){
            System.out.println("Please enter a valid major or 'NA'");
            major = sc.nextLine().trim();
        }

        System.out.println("what is you minor?  If you don't have a minor, please type 'NA'");
        String minor = sc.nextLine().trim();
        while (minor.equals("")){
            System.out.println("Please enter a valid minor or 'NA'");
            minor = sc.nextLine().trim();
        }

        // calls the method to add the student to the database
        addStudentToDB(username, password, name, id, gradYear, major, minor);
//        studentList.put(login, new Student(login, password, username, studentList.size() + 1, gradYear, major, minor));
        System.out.println("You have successfully created an account!");
    }

    public static void login() {
        System.out.println("To login in please enter your username (Type 'Exit' to go back to home)");
        String username = sc.nextLine().trim();
        if (username.toLowerCase().equals("exit"))
            return;
        System.out.println("Please enter your password (Type 'Exit' to go back to home)");
        String password = sc.nextLine().trim();
        if (password.toLowerCase().equals("exit"))
            return;
        if (session.authenticate(username, password)){
            if (session.getTypeOfUser().equals("student")){
                stuSignedIn = students.getStudent(username);
                System.out.println("STUDENT");
            } else{
                adminSignedIn = admins.getAdmin(username);
                System.out.println("ADMIN");
            }
            System.out.println("You successfully logged in as a " + session.getTypeOfUser() + "!");
        } else
            return;
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
    /**
     * adds a new student to the database
     * @param username
     * @param password
     * @param name
     * @param id
     * @param gradYear
     */
    private static void addStudentToDB (String username, String password, String name, int id, int gradYear, String major, String minor){
        String sql = "INSERT INTO STUDENTS (id, gradYear, majors, minors, name, username, password) VALUES(?,?,?,?,?,?,?)";
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setInt(2, gradYear);
            pstmt.setString(3, major);
            pstmt.setString(4, minor);
            pstmt.setString(5, name);
            pstmt.setString(6, username);
            pstmt.setString(7, password);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private static void stuCreateSchedule(){
        if(session.isAuthen()){
            if(session.getTypeOfUser().equals("student")){
                System.out.println("What would you like the name of your schedule to be?");
                String name = sc.nextLine();
                while(name.trim().equals("")){
                    System.out.println("The name of your schedule cannot be empty!");
                    name = sc.nextLine();
                }
                System.out.println("What semester is this for? ('Fall' or 'Spring')");
                String semester = sc.nextLine().trim();
                while(!semester.toLowerCase().equals("fall") && !semester.toLowerCase().equals("spring")){
                    System.out.println("That semester is not aviable!");
                    semester = sc.nextLine();
                }
                stuSignedIn.createNewSchedule(name, semester, courses);
                System.out.println("You successfully created a schedule!");
            }else{
                System.out.println("You're not signed in as a student!");
            }
        }else{
            System.out.println("You're not signed in!");
        }
    }

    public static void displayAllSchedule() {
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
