package edu.gcc.nemo.scheduler.DB;

import edu.gcc.nemo.scheduler.Admin;
import edu.gcc.nemo.scheduler.Student;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class Admins {

    private static Admins admins = null;
    private Connection conn;
    private Statement stmt;
    private PreparedStatement loginStatement;
    private Map<String, Admin> allAdmins;

    /**
     * Singleton for students
     */
    public static Admins getAdmins(){
        if(admins == null){
            return new Admins();
        }
        return admins;
    }

    // Constructor
    public Admins(){
        try{
            conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            stmt = conn.createStatement();
            loginStatement = conn.prepareStatement("select  * from Admins where login = ?");
            allAdmins = new HashMap<>();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * getAdmin
     * @param login the login the person uses to access their account
     * @returns returns the admin from the map if the login exists
     */
    public Admin getStudent(String login){
        return allAdmins.get(login);
    }

    /**
     * getAllAdmins
     * Populates a map with all admins from the db.
     * The key is the login, and the value is the admin
     */
    public void getAllAdmins(){
        try{
            ResultSet rs = stmt.executeQuery("select * from Admins");
            while(rs.next()){
                allAdmins.put(rs.getString("login"),
                        new Admin(rs.getString("login"),
                                rs.getString("password"),
                                rs.getString("name")));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


}