package edu.gcc.nemo.scheduler.DB;

import edu.gcc.nemo.scheduler.Admin;
import edu.gcc.nemo.scheduler.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Admins {

    private static Admins admins = null;
    private Connection conn;
    private Statement stmt;
//    private PreparedStatement loginStatement;
    private Map<String, Admin> allAdmins;
    private ArrayList<Integer> listOfIds;

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
//            loginStatement = conn.prepareStatement("select  * from Admins where login = ?");
            allAdmins = new HashMap<>();
            listOfIds = new ArrayList<>();
            loadAllAdmins();
            conn.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * getAdmin
     * @param username the username the person uses to access their account
     * @returns returns the admin from the map if the login exists
     */
    public Admin getAdmin(String username){
        return allAdmins.get(username);
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean doesAdminIdExist(int id) {
        if(listOfIds.contains(id))
            return true;
        return false;
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
                allAdmins.put(rs.getString("username"),
                        new Admin(rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("name"),
                                rs.getInt("id")));
                listOfIds.add(rs.getInt("id"));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


}
