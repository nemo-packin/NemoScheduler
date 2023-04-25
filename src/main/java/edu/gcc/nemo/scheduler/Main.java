package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Admins;
import edu.gcc.nemo.scheduler.DB.Courses;
import edu.gcc.nemo.scheduler.DB.Students;
import edu.gcc.nemo.scheduler.util.EarlyExitException;
import edu.gcc.nemo.scheduler.util.GoBackException;
import edu.gcc.nemo.scheduler.util.TextInputException;

import java.sql.*;
import java.util.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
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
        System.out.println("Hello World!");
        // created singleton objects
        students = Students.getInstance();
        admins = Admins.getAdminsInstance();
        courses = Courses.getInstance();
        SpringApplication.run(Main.class, args);

        session = new Session(admins, students, courses);
        System.out.println("Hello nemo packers!");

        Map<String, Object> input;
        State state = new State();
        boolean cont = true;
        while (cont) {
            try {
                input = getInput(state);
                cont = step(input, state);
            } catch (TextInputException e) {
                System.out.println("WHOOPS! - " + e.getMessage());
            } catch (EarlyExitException e) {
                cont = false;
            } catch (GoBackException e) {
                state.goBack();
            }
        }
        System.out.println("Goodbye nemo-er!");

    }

    public static String getLine() throws EarlyExitException, GoBackException {
        System.out.print(" > ");
        String input = sc.nextLine().toLowerCase().trim();
        if (input.equals("exit"))
            throw new EarlyExitException();
        if (input.equals("back"))
            throw new GoBackException();
        return input;
    }

    public static Map<String, Object> getInput(State state) throws EarlyExitException, TextInputException, GoBackException {
        Map<String, Object> m = new HashMap<>();
        switch (state.getCurrent()) {
            case SIGNED_OUT:
                System.out.println("Would you like to sign in or create account? \n" +
                        "(Type: 'Sign in' or 'Create account' or 'Exit')");
                m.put("action", getLine());
                break;
            case CREATE_ACCOUNT:
                m = getAccountDetails();
                break;
            case AUTHENTICATE:
                m = getLoginInfo();
                break;
            case ADD_COURSE:
                System.out.println("Enter the course code for the course you would like to add!");
                m.put("course_code", getLine());
                break;
            case REMOVE_COURSE:
                System.out.println("Enter the course code for the course you would like to remove!");
                m.put("course_code", getLine());
                break;
            case SEARCH_COURSE:
                System.out.println("Enter search as list of filters separated by semicolons ([filter]-[value];[filter]-[value]).\n" +
                        "Valid filters are 'course code', 'prof', 'semester', 'time', 'day', 'credit hours')!");
                m.put("query", getLine());
                break;
            case SEARCH_STUDENT:
                System.out.println("Enter student query!");
                m.put("query", getLine());
                break;
            case VIEW_SCHEDULE:
                break;
            case EDIT_SCHEDULE:
                System.out.println("Would you like to add or remove course (`add course` / `remove_course`)?");
                m.put("action", getLine());
                break;
            case CREATE_SCHEDULE:
                m = getCreateScheduleInfo();
                break;
            case HOME:
                System.out.println("Would you like to sign out, create a schedule, view schedule, edit a schedule, or search course? \n" +
                        "(Type: 'Log out' or 'Edit schedule' or 'Search Student' or" +
                        "'View Schedule' or 'Create Schedule' or 'Exit' or 'Search Course')");
                m.put("action", getLine());
                break;
        }
        return m;
    }

    public static boolean step(Map<String, Object> input, State state) throws TextInputException {
        switch (state.getCurrent()) {
            case SIGNED_OUT:
                switch ((String) input.get("action")) {
                    case "sign in":
                        state.update(RouteName.AUTHENTICATE);
                        break;
                    case "create account":
                        state.update(RouteName.CREATE_ACCOUNT);
                        break;
                    default:
                        throw new TextInputException();
                }
                break;
            case CREATE_ACCOUNT:
                addStudentToDB(
                        (String)input.get("username"),
                        (String)input.get("password"),
                        (String)input.get("name"),
                        (int)input.get("id"),
                        (int)input.get("year"),
                        (String)input.get("major"),
                        (String)input.get("minor"));
                System.out.println("You have successfully created an account");
                state.update(RouteName.SIGNED_OUT);
                break;
            case AUTHENTICATE:
                String username = (String)input.get("username");
                String password = (String)input.get("password");
                if (session.authenticate(username, password)){
                    if (session.getTypeOfUser().equals("student")){
                        stuSignedIn = students.getStudent(username);
                        System.out.println("STUDENT");
                    } else{
                        adminSignedIn = admins.getAdmin(username);
                        System.out.println("ADMIN");
                    }
                    System.out.println("You successfully logged in as a " + session.getTypeOfUser() + "!");
                    state.update(RouteName.HOME);

                } else {
                    throw new TextInputException("Failed to authenticate!");
                }
                break;
            case ADD_COURSE:
                String courseCodeAdd = (String)input.get("course_code");
                addCourse(courseCodeAdd);
                state.update(RouteName.HOME);
                break;
            case REMOVE_COURSE:
                String courseCodeRem = (String)input.get("course_code");
                removeCourse(courseCodeRem);
                state.update(RouteName.HOME);
                break;
            case SEARCH_COURSE:
                String query = (String)input.get("query");
                searchCourse(query);
                state.update(RouteName.HOME);
                break;
            case SEARCH_STUDENT:
                String stuQuery = (String)input.get("query");
                studentSearch(stuQuery);
                state.update(RouteName.HOME);
                break;
            case VIEW_SCHEDULE:
                displayAllSchedule();
                state.update(RouteName.HOME);
                break;
            case EDIT_SCHEDULE:
                switch ((String) input.get("action")) {
                    case "add course":
                        state.update(RouteName.ADD_COURSE);
                        break;
                    case "remove course":
                        state.update(RouteName.REMOVE_COURSE);
                        break;
                    default:
                        throw new TextInputException("Must `add course` or `remove course`");
                }
                break;
            case CREATE_SCHEDULE:
                stuSignedIn.createNewSchedule((String)input.get("name"),
                        (String)input.get("semester"),
                        courses);
                session.saveSchedule();
                state.update(RouteName.HOME);
                break;
            case HOME:
                switch ((String) input.get("action")) {
                    case "create schedule":
                        state.update(RouteName.CREATE_SCHEDULE);
                        break;
                    case "edit schedule":
                        if(stuSignedIn.schedule == null)
                            throw new TextInputException("Must create a schedule first");
                        state.update(RouteName.EDIT_SCHEDULE);
                        break;
                    case "view schedule":
                        if(stuSignedIn.schedule == null)
                            throw new TextInputException("Must create a schedule first");
                        state.update(RouteName.VIEW_SCHEDULE);
                        break;
                    case "search course":
                        state.update(RouteName.SEARCH_COURSE);
                        break;
                    case "search student":
                        state.update(RouteName.SEARCH_STUDENT);
                        break;
                    case "log out":
                        return false;
                    default:
                        throw new TextInputException("Must `create schedule` or `edit schedule` or `view schedule` or `search course` or `exit`");
                }
                break;
        }
        return true;
    }

    private static Map<String, Object> getAccountDetails() throws TextInputException, EarlyExitException, GoBackException {
        Map<String, Object> m = new HashMap<>();
        System.out.println("Please enter the username you would like to use:  (Type 'back' to go back)");
        String username = getLine();
        if (doesUsernameExist(username))
            throw new TextInputException("I'm sorry, but that username already exists, please pick a different one");
        m.put("username", username);
        System.out.println("Please enter an your id: ");
        int id;
        try {
            id = Integer.parseInt(getLine());
        } catch (NumberFormatException e) {
            throw new TextInputException("Id must be an integer.");
        }
        if (id < -1)
            throw new TextInputException("Id must be positive.");
        if (doesIdExist(id))
            throw new TextInputException("id already exists");
        m.put("id", id);
        System.out.println("Please enter the password you would like to use:  (Type 'back' to go back to home)");
        String password = getLine();
        System.out.println("Please re-enter your password:");
        String confirm = getLine();
        if (!confirm.equals(password))
            throw new TextInputException("Passwords must match");
        m.put("password", password);
        System.out.println("What is your name? (Type 'back' to back to home)");
        String name = getLine();
        m.put("name", name);
        System.out.println("What is your graduation year?");
        int year = -1;
        try {
            year = Integer.parseInt(getLine());
        } catch (NumberFormatException e) {
            throw new TextInputException("Year must be an integer.");
        }
        if (year < -1)
            throw new TextInputException("Year must be positive.");
        m.put("year", year);
        System.out.println("what is you major?  If you don't have a major, please type 'NA'");
        String major = getLine();
        if (major.equals(""))
            throw new TextInputException("Please input a valid major or NA");
        m.put("major", major);
        System.out.println("what is you minor?  If you don't have a minor, please type 'NA'");
        String minor = getLine();
        if (minor.equals(""))
            throw new TextInputException("Please input a valid minor or NA");
        m.put("minor", minor);
        return m;
    }

    public static Map<String, Object> getLoginInfo() throws EarlyExitException, GoBackException {
        System.out.println("To login in please enter your username (Type 'Back' to go back to home)");
        String username = getLine();
        System.out.println("Please enter your password (Type 'Back' to go back to home)");
        String password = getLine();
        return new HashMap<>(Map.of("username", username, "password", password));
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
     *
     * @param username
     * @param password
     * @param name
     * @param id
     * @param gradYear
     */
    private static void addStudentToDB(String username, String password, String name, int id, int gradYear, String major, String minor) {
        String sql = "INSERT INTO STUDENTS (id, grad_year, majors, minors, name, username, password) VALUES(?,?,?,?,?,?,?)";
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setInt(2, gradYear);
            pstmt.setString(3, major);
            pstmt.setString(4, minor);
            pstmt.setString(5, name);
            pstmt.setString(6, username);
            pstmt.setString(7, password);
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, Object> getCreateScheduleInfo() throws TextInputException, EarlyExitException, GoBackException {
        if (session.getTypeOfUser().equals("student")) {
            System.out.println("What would you like the name of your schedule to be?");
            String name = getLine();
            if (name.trim().equals("")) {
                throw new TextInputException("The name of your schedule cannot be empty!");
            }
            System.out.println("What semester is this for? ('Fall' or 'Spring')");
            String semester = getLine();
            if (!semester.toLowerCase().equals("fall") && !semester.toLowerCase().equals("spring")) {
                throw new TextInputException("That semester is not aviable!");
            }
            return Map.of("name", name, "semester", semester);
        } else {
            throw new TextInputException("You're not signed in as a student!");
        }
    }


    public static void displayAllSchedule() {
        if (stuSignedIn != null) {
            System.out.println("The following is a list of all the classes: \n");
            System.out.println(stuSignedIn.schedule.calendarView());
        } else if (adminSignedIn != null) {
            System.out.println("As an admin you do not have a schedule!");
        } else {
            System.out.println("You are not signed in!");
        }
    }

    public static void addCourse(String courseCode) throws TextInputException {
        if (stuSignedIn != null) {
            try {
                stuSignedIn.schedule.addCourse(courseCode);
                System.out.println(stuSignedIn.schedule.toString());
                session.saveSchedule();
            } catch (IllegalArgumentException e) {
                throw new TextInputException("Could not find course");
            }
        } else if (adminSignedIn != null) {
            System.out.println("As an admin you do not have a schedule!");
        } else {
            System.out.println("You are not signed in!");
        }
    }

    public static void removeCourse(String courseCode) {
        if (stuSignedIn != null) {
            stuSignedIn.schedule.removeCourse(courseCode);
            System.out.println(stuSignedIn.schedule.toString());
            session.saveSchedule();
        } else if (adminSignedIn != null) {
            System.out.println("As an admin you do not have a schedule!");
        } else {
            System.out.println("You are not signed in!");
        }
    }

    public static void searchCourse(String query) {
        Course[] cs = session.searchCourses(query);
        for(Course c : cs) {
            System.out.println(c);
        }
    }
    public static void studentSearch(String stuQuery) {
        Student[] ss = session.searchStudents(stuQuery);
        for(Student s : ss) {
            System.out.println(s);
        }
    }
}
