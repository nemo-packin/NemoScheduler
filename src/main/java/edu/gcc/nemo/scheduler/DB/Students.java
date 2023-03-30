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
//    private PreparedStatement loginStatement;
    private Map<String, Student> allStudents;
    private ArrayList<String> studentUsernames;
    private ArrayList<Integer> listOfIds;

    /**
     * Singleton for students
     */
    public static Students getStudentsInstance(){
        if(students == null){
            students = new Students();
            return students;
        }
        return students;
    }

    private Students() {
        try{
            conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            stmt = conn.createStatement();
//            loginStatement = conn.prepareStatement("select  * from Students where login = ?");
            allStudents = new HashMap<>();
            studentUsernames = new ArrayList<>();
            listOfIds = new ArrayList<>();
            loadAllStudents();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * getStudent
     * @param username uses this to reference the hashmap of students
     * @returns returns the student from the map if the login exists
     */
    public Student getStudent(String username){
        return allStudents.get(username);
    }

    /**
     *
     * @param id
     * @return "false" if id does not exist, "true" if id exists
     */
    public boolean doesStuIdExist(int id) {
        if(listOfIds.contains(id))
            return true;
        return false;
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
                allStudents.put(rs.getString("username"),
                        new Student(rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("name"),
                                rs.getInt("id"),
                                rs.getInt("grad_year")));
                studentUsernames.add(rs.getString("username"));
                listOfIds.add(rs.getInt("id"));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getListOfAllStudentUsernames(){
        return studentUsernames;
    }

}
