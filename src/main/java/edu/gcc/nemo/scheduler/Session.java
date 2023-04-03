package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Students;
import edu.gcc.nemo.scheduler.DB.Admins;
import edu.gcc.nemo.scheduler.DB.Courses;
import org.eclipse.jetty.util.StringUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Session {
    private Student stu;
    private Admin admin;
    private String typeOfUser;
    private boolean authenticated = false;

    private final Admins refAdmins;
    private final Students refStudents;
    private final Courses refCourses;

    public Session(Admins admins, Students students, Courses courses) {
        refAdmins = admins;
        refStudents = students;
        refCourses = courses;
        stu = null;
        admin = null;
        typeOfUser = "";
    }

    public boolean authenticate(String username, String password) {
        refStudents.reloadStudents();
        if(refStudents.getStudent(username) != null) {
            stu = refStudents.getStudent(username);
            typeOfUser = "student";
        }
        else if(refAdmins.getAdmin(username) != null) {
            admin = refAdmins.getAdmin(username);
            typeOfUser = "admin";
        }else{
            System.out.println("Failed to authenticate!");
        }
        if(admin != null && admin.password.equals(password)) {
            authenticated = true;
        }else if(stu != null && stu.password.equals(password)){
            stu.loadScheduleFromDB(Courses.getInstance());
            authenticated = true;
        }else{
            System.out.println("Failed to authenticate!");
        }
        return authenticated;
    }

    public boolean isAuthen(){
        return authenticated;
    }

    void saveSchedule() {
        Schedule schedule = this.stu.schedule;
        String sql = "INSERT INTO Schedules (id, name, semester, courses, isApproved) VALUES(?,?,?,?,?)";
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, schedule.getId());
            pstmt.setString(2, schedule.getName());
            pstmt.setString(3, schedule.getSemester());
            pstmt.setString(4, schedule.getCourses());
            pstmt.setString(5, schedule.getApproved() ? "true" : "false");
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println("You already have a schedule with this name! Updating...");
            sql = "update Schedules set id = ?, name = ?, semester = ?, courses = ?, isApproved = ? where name = ?";
            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, schedule.getId());
                pstmt.setString(2, schedule.getName());
                pstmt.setString(3, schedule.getSemester());
                pstmt.setString(4, schedule.getCourses());
                pstmt.setString(5, schedule.getApproved() ? "true" : "false");
                pstmt.setString(6,schedule.getName());
                pstmt.executeUpdate();
                conn.close();
            } catch (SQLException e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    void editSchedule() {
        Schedule schedule = stu.schedule;
//        System.out.println("You already have a schedule with this name! Updating...");
        String sql = "update Schedules set id = ?, name = ?, semester = ?, courses = ?, isApproved = ? where name = ?";
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, schedule.getId());
            pstmt.setString(2, schedule.getName());
            pstmt.setString(3, schedule.getSemester());
            pstmt.setString(4, schedule.getCourses());
            pstmt.setString(5, schedule.getApproved() ? "true" : "false");
            pstmt.setString(6,schedule.getName());
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e2) {
            throw new RuntimeException(e2);
        }
    }

    /**
     * @param stuNameSearchVal is the username the admin is searching for
     * @return returns an arraylist of all names that contains the search value
     */
    public Student[] searchStudents(String stuNameSearchVal) {
        if(authenticated && typeOfUser.equals("admin")){
            ArrayList<String> listOfStuNames = refStudents.getListOfAllStudentUsernames();
            ArrayList<String> resultNameMatches = new ArrayList<>();
            for(String names : listOfStuNames){
                if(names.contains(stuNameSearchVal))
                    resultNameMatches.add(names);
            }
            Student[] stuArr = new Student[resultNameMatches.size()];
            for(int i = 0; i < stuArr.length; i++){
                stuArr[i] = refStudents.getStudent(resultNameMatches.get(i));
            }
            return stuArr;
        }
        System.out.println("You do not have proper credentials to search for " + stuNameSearchVal + "!");
        return null;
    }

    /**
     * @param courseCodeSearchVal is the name of the course the user is searching for
     * @return an array of courses with course codes that contains the search value
     */
    public Course[] searchCourses(String courseCodeSearchVal) {
        String searchVal = courseCodeSearchVal.replace(" ", "");
        CourseSearch cs = new CourseSearch();
        cs.addFilter(CourseFieldNames.courseCode, searchVal, FilterMatchType.CONTAINS);
        return cs.getResults().toArray(new Course[0]);
    }

    public String getSchedule() {
        return null;
    }

    /**
     * this is used for an admin specifically selecting a student
     * @param username
     * @return returns the instance of that student
     */
    public Student getStudent(String username){
        if(authenticated && typeOfUser.equals("admin")){
            return refStudents.getStudent(username);
        }
        System.out.println("You do not have proper credentials!");
        return null;
    }

    public String getStatusSheet() {
        if(typeOfUser.equals("student"))
            return stu.statusSheet.toString();
        System.out.println("You do not have a status sheet");
        return null;
    }

    /**
     * @returns the type of user on this session
     */
    public String getTypeOfUser(){
        return typeOfUser;
    }

    public Student getStu(){
        return stu;
    }

    public Admin getAdmin(){
        return admin;
    }
}
