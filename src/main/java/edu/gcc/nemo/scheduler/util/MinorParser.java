package edu.gcc.nemo.scheduler.util;

import java.io.*;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MinorParser {
    public static void main(String[] args) {
        ClassLoader c = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = c.getResourceAsStream("Minors 2022-23.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String f = br.lines().collect(Collectors.joining("\n"));
        String pattern = "(^[ +A-Z& and-]+)\\. ([0-9-]+) required [a-z ,:]+(.+?\\.+[^A-Z a-z ,.;:/]\\n)";
        Pattern r = Pattern.compile(pattern, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = r.matcher(f);

        while (m.find()) {
            String title = m.group(1);
            String hours = m.group(2);
            String courses = m.group(3).trim();
            System.out.println("Title: " + title);
            System.out.println("Hours: " + hours);
            System.out.println("Courses: " + courses);
            System.out.println();
            String sql = "INSERT INTO MINORS (Title, Credit_Hours, Requirements) VALUES(?,?,?)";
            String del = "DELETE FROM MINORS";
            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, title);
                pstmt.setString(2, hours);
                pstmt.setString(3, courses);
                pstmt.executeUpdate();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String minorToString (String name){
        String minorInfo = "";
        String sql = "select * from Minors where Title = ?";
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                minorInfo = rs.getString("Credit_Hours");
//                        rs.getString("department"),
//                        rs.getString("semester"),

                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return minorInfo;
    }
}