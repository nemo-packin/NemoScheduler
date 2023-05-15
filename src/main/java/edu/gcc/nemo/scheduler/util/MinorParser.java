package edu.gcc.nemo.scheduler.util;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import edu.gcc.nemo.scheduler.Course;
import edu.gcc.nemo.scheduler.CourseLike;
import edu.gcc.nemo.scheduler.CourseOptions;
import edu.gcc.nemo.scheduler.DB.Courses;
import static edu.gcc.nemo.scheduler.util.MajorParser.getCourseCode;
import static edu.gcc.nemo.scheduler.util.MajorParser.getMatchingCourses;

public class MinorParser {
    private static List<CourseOptions> minorOptions = new ArrayList<>();
    private static Map<String, String> dptNamesToCodes = MajorParser.parseDeptCodes();
    public static void main(String[] args) {
        ClassLoader c = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = c.getResourceAsStream("Minors 2022-23.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String f = br.lines().collect(Collectors.joining("\n"));
        String pattern = "(^[ +A-Z& and-]+)\\. ([0-9-]+) required [a-z ,:]+(.+?\\.+[^A-Z a-z ,.;:/]\\n)";
        Pattern r = Pattern.compile(pattern, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = r.matcher(f);

        while (m.find()) {
            List<CourseLike> courseCodes = new ArrayList<>();
            String title = m.group(1);
            String hours = m.group(2);
            String courses = m.group(3).trim();
            String sql = "INSERT INTO MINORS (Title, Credit_Hours, Requirements) VALUES(?,?,?)";
            String del = "DELETE FROM MINORS";
            Pattern coursesPattern = Pattern.compile("(?<dept>(?:[A-Z][a-z]+ ?)+)(?<codes>(?:\\d{3}(?:[^A-Za-z]+|and|or)*))(?!;|\\.|and|[A-Z])", Pattern.DOTALL);
            Pattern codePattern = Pattern.compile("\\d{3}");
            Matcher courseMatch = coursesPattern.matcher(courses);
            List<String> CCodesperMinor = new ArrayList<>();
            while (courseMatch.find()){
                String codes = courseMatch.group("codes");
                Matcher codeMatch = codePattern.matcher(codes);
                String s = getCourseCode(dptNamesToCodes, courseMatch.group("dept"));
                while (codeMatch.find()) {
                    CCodesperMinor.add(s + codeMatch.group());
                    getMatchingCourses(Courses.getInstance().getAllCourses(), s + codeMatch.group()).forEach(courseCodes::add);
                }
            }
            hours = hours.substring(0, 2);
            int hrs = Integer.parseInt(hours);
            CourseOptions courseOptions = new CourseOptions(title, hrs);
            courseOptions.options = courseCodes;
            minorOptions.add(courseOptions);

//            try {
//                Connection conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
//                PreparedStatement pstmt = conn.prepareStatement(sql);
//                pstmt.setString(1, title);
//                pstmt.setString(2, hours);
//                pstmt.setString(3, courses);
//                pstmt.executeUpdate();
//                conn.close();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
        }
        for (int i = 0; i < minorOptions.size(); i++){
            System.out.println(minorOptions.get(i).toString());
        }
    }

    public static String minorToString (String name){
        String minorTitle = "";
        String minorHrs = "";
        String minorReq = "";
        String sql = "select * from Minors where Title = ?";
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                minorTitle = "Title: " + rs.getString("Title") + "\n";
                minorHrs =  "Credit Hours: " + rs.getString("Credit_Hours") + "\n";
                minorReq =  "Requirements: " + rs.getString("Requirements") + "\n";
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String minorInfo = minorTitle + minorHrs + minorReq;
        return minorInfo;
    }

    public static List<CourseOptions> parseMinorsAsCourseOptions() {
        ClassLoader c = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = c.getResourceAsStream("Minors 2022-23.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String f = br.lines().collect(Collectors.joining("\n"));
        String pattern = "(^[ +A-Z& and-]+)\\. ([0-9-]+) required [a-z ,:]+(.+?\\.+[^A-Z a-z ,.;:/]\\n)";
        Pattern r = Pattern.compile(pattern, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = r.matcher(f);

        while (m.find()) {
            List<CourseLike> courseCodes = new ArrayList<>();
            String title = m.group(1);
            String hours = m.group(2);
            String courses = m.group(3).trim();
            Pattern coursesPattern = Pattern.compile("(?<dept>(?:[A-Z][a-z]+ ?)+)(?<codes>(?:\\d{3}(?:[^A-Za-z]+|and|or)*))(?!;|\\.|and|[A-Z])", Pattern.DOTALL);
            Pattern codePattern = Pattern.compile("\\d{3}");
            Matcher courseMatch = coursesPattern.matcher(courses);
            List<String> CCodesperMinor = new ArrayList<>();
            while (courseMatch.find()){
                String codes = courseMatch.group("codes");
                Matcher codeMatch = codePattern.matcher(codes);
                String s = getCourseCode(dptNamesToCodes, courseMatch.group("dept"));
                while (codeMatch.find()) {
                    CCodesperMinor.add(s + codeMatch.group());
                    courseCodes.addAll(getMatchingCourses(Courses.getInstance().getAllCourses(), s + codeMatch.group()));
                }
            }
            hours = hours.substring(0, 2);
            int hrs = Integer.parseInt(hours);
            CourseOptions courseOptions = new CourseOptions(title, hrs);
            courseOptions.options = courseCodes;
            minorOptions.add(courseOptions);
        }
        return minorOptions;
    }
}