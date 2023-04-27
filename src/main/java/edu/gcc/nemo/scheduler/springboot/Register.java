package edu.gcc.nemo.scheduler.springboot;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // Update with the URL of the frontend application
public class Register {
    @PostMapping("/register")
    public ResponseEntity<String> postData(@RequestBody Map<String, Object> data) {
        String username = data.get("username").toString();
        String password = data.get("password").toString();
        String name = data.get("name").toString();
        int id = Integer.parseInt(data.get("id").toString());
        int gradYear = Integer.parseInt(data.get("year").toString());
        String major = data.get("major").toString();
        String minor = data.get("minor").toString();
        addStudentToDB(username, password, name, id, gradYear, major, minor);
        return ResponseEntity.ok("Successfully added to database!");
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
}
