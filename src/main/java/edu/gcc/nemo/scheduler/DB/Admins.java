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
    public static Admins getAdminsInstance(){
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
            loadAllAdmins();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * getAdmin
     * @param login the login the person uses to access their account
     * @returns returns the admin from the map if the login exists
     */
    public Admin getAdmin(String login){
        return allAdmins.get(login);
    }

    /**
     * loadAllAdmins
     * Populates a map with all admins from the db.
     * The key is the login, and the value is the admin
     */
    private void loadAllAdmins(){
        try{
            ResultSet rs = stmt.executeQuery("select * from Admins");
            while(rs.next()){
                allAdmins.put(rs.getString("login"),
                        new Admin(rs.getString("login"),
                                rs.getString("password"),
                                rs.getString("username")));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


}
