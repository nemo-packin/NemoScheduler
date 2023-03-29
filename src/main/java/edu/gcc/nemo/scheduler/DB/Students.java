package edu.gcc.nemo.scheduler.DB;

import edu.gcc.nemo.scheduler.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Students {

    private static Students students = null;
    private Connection conn;
    private Statement stmt;
    private PreparedStatement loginStatement;
    private Map<String, Student> allStudents;
    private Map<String, String> nameToLoginMap;
    private ArrayList<String> studentUsernames;

    /**
     * Singleton for students
     */
    public static Students getStudentsInstance(){
        if(students == null){
            return new Students();
        }
        return students;
    }

    private Students() {
        try{
            conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            stmt = conn.createStatement();
            loginStatement = conn.prepareStatement("select  * from Students where login = ?");
            allStudents = new HashMap<>();
            studentUsernames = new ArrayList<>();
            nameToLoginMap = new HashMap<>();
            loadAllStudents();
//            getAllStudentUsers();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * getStudent
     * @param login the login the person uses to access their account
     * @returns returns the student from the map if the login exists
     */
    public Student getStudent(String login){
        return allStudents.get(login);
    }

    /**
     * @param username
     * @return this returns the matching login for the specific username
     */
    public String getLogin(String username){
        return nameToLoginMap.get(username);
    }

    /**
     * loadAllStudents
     * -Populates a map with all students from the db. The key is the login, and the value is the student
     * -It also populates a map of username to logins
     * -An array list of usernames is also filled
     */
    private void loadAllStudents(){
        try{
            ResultSet rs = stmt.executeQuery("select * from Students");
            while(rs.next()){
                allStudents.put(rs.getString("login"),
                        new Student(rs.getString("login"),
                                rs.getString("password"),
                                rs.getString("username"),
                                rs.getInt("id"),
                                rs.getInt("grad_year")));
                nameToLoginMap.put(rs.getString("username"),
                                rs.getString("login"));
                studentUsernames.add(rs.getString("username"));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getListOfAllStudentUsernames(){
        return studentUsernames;
    }

}
