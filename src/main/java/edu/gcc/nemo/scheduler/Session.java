package edu.gcc.nemo.scheduler;

import java.sql.*;

public class Session {
    private User user;
    private boolean authenticated = false;

    public Session() {

    }

    void authenticate() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            PreparedStatement stuStmt = conn.prepareStatement("Select * from Students");

            ResultSet rs = stuStmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getAuthen(){
        return authenticated;
    }

    void saveSchedule() {}

    void editSchedule() {}

    String searchStudents() {return null;}

    String searchCourses() {return null;}

    String getSchedule() {return null;}

    String getStatusSheet() {return null;}

}
