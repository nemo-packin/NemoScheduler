package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Students;
import edu.gcc.nemo.scheduler.DB.Admins;
import edu.gcc.nemo.scheduler.DB.Courses;
import edu.gcc.nemo.scheduler.CourseFieldNames;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // Update with the URL of the frontend application
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

    @PostMapping("/login")
    public ResponseEntity<String> postData(@RequestBody Map<String, Object> data) {
        String username = data.get("username").toString();
        String password = data.get("password").toString();

        authenticate(username, password);
        System.out.println("");
        // Process the data here
        if(authenticated && typeOfUser.equals("student"))
            return ResponseEntity.ok("student");
        else if(authenticated && typeOfUser.equals("admin"))
            return ResponseEntity.ok("admin");
        else
            return ResponseEntity.ok("invalid login!");
    }
    @GetMapping("/auth")
    public boolean getAuth() {
        return authenticated;
    }

    @GetMapping("/userType")
    public String userType(){
        return typeOfUser;
    }

    @PostMapping("/logout")
    public void logout() {
        authenticated = false;
        typeOfUser = "";
    }

    @GetMapping("/accountInfo")
    public List<String> getAccountInfo() {
        if(this.typeOfUser.equals("student")) {
            return List.of(stu.name, stu.username);
        } else return List.of(admin.name, admin.username);
    }

    public boolean authenticate(String username, String password) {
        refStudents.reloadStudents();
        if (refStudents.getStudent(username) != null) {
            stu = refStudents.getStudent(username);
            typeOfUser = "student";
        } else if (refAdmins.getAdmin(username) != null) {
            admin = refAdmins.getAdmin(username);
            typeOfUser = "admin";
        }
        if (admin != null && admin.password.equals(password)) {
            authenticated = true;
        } else if (stu != null && stu.password.equals(password)) {
            stu.loadScheduleFromDB(Courses.getInstance());
            authenticated = true;
        }
        return authenticated;
    }

    public boolean isAuthen() {
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
                pstmt.setString(6, schedule.getName());
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
            pstmt.setString(6, schedule.getName());
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
        if (authenticated && typeOfUser.equals("admin")) {
            ArrayList<String> listOfStuNames = refStudents.getListOfAllStudentUsernames();
            ArrayList<String> resultNameMatches = new ArrayList<>();
            for (String names : listOfStuNames) {
                if (names.contains(stuNameSearchVal))
                    resultNameMatches.add(names);
            }
            Student[] stuArr = new Student[resultNameMatches.size()];
            for (int i = 0; i < stuArr.length; i++) {
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
        String[] filters = courseCodeSearchVal.split(";");
        CourseSearch cs = new CourseSearch();
        for (String f : filters) {
            String[] kv = f.split("_");
            String fieldName = kv[0];
            String vals = kv[1];
            switch (fieldName) {
                case "department":
                    cs.addFilter(CourseFieldNames.courseCode, vals, FilterMatchType.CONTAINS);
                    break;
                case "semester":
                    cs.addFilter(CourseFieldNames.semester, vals, FilterMatchType.CONTAINS);
                    break;
                case "time":
                    cs.addFilter(CourseFieldNames.time, vals, FilterMatchType.CONTAINS);
                    break;
                case "day":
                    cs.addFilter(CourseFieldNames.day, vals, FilterMatchType.CONTAINS);
                    break;
                case "prof":
                    cs.addFilter(CourseFieldNames.prof, vals, FilterMatchType.CONTAINS);
                    break;
                case "name":
                    cs.addFilter(CourseFieldNames.name, vals, FilterMatchType.CONTAINS);
                    break;
                case "credit hours":
                    cs.addFilter(CourseFieldNames.creditHours, vals, FilterMatchType.CONTAINS);
                    break;
                case "course code":
                    cs.addFilter(CourseFieldNames.courseCode, vals, FilterMatchType.CONTAINS);
                    break;
                case "capacity":
                    cs.addFilter(CourseFieldNames.capacity, vals, FilterMatchType.CONTAINS);
                    break;
                default:
                    break;

            }
        }
        String searchVal = courseCodeSearchVal.replace(" ", "");
        return cs.getResults().toArray(new Course[0]);
    }

    public String getSchedule() {
        return null;
    }

    /**
     * this is used for an admin specifically selecting a student
     *
     * @param username
     * @return returns the instance of that student
     */
    public Student getStudent(String username) {
        if (authenticated && typeOfUser.equals("admin")) {
            return refStudents.getStudent(username);
        }
        System.out.println("You do not have proper credentials!");
        return null;
    }

    public String getStatusSheet() {
        if (typeOfUser.equals("student"))
            return stu.statusSheet.toString();
        System.out.println("You do not have a status sheet");
        return null;
    }

    /**
     * @returns the type of user on this session
     */
    public String getTypeOfUser() {
        return typeOfUser;
    }

    public Student getStu() {
        return stu;
    }

    public Admin getAdmin() {
        return admin;
    }
}
