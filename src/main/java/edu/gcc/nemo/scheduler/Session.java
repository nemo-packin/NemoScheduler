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
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // Update with the URL of the frontend application
public class Session {
    private Student pseudoStu;
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
        pseudoStu = null;
        stu = null;
        admin = null;
        typeOfUser = "";
    }

//    @GetMapping("/pseudoAccountInfo")
//    public List<String> getPseudoAccountInfo() {
//        if(this.typeOfUser.equals("admin")) {
//            if(psudoStu.schedule.getApproved()) {
//                return List.of(psudoStu.name, psudoStu.username, "Approved", psudoStu.getMajor(), psudoStu.getMinor());
//            }
//            return List.of(psudoStu.name, psudoStu.username, "Not Approved", psudoStu.getMajor(), psudoStu.getMinor());
//        } else{
//            return List.of("","","","","");
//        }
//    }

    @PostMapping("/createPseudoStudent")
    public String createPseudoStudent(@RequestBody Map<String, String> data){
        if(admin != null && typeOfUser.equals("admin")){
            String stuUsername = data.get("username");
            pseudoStu = refStudents.getStudent(stuUsername);
            pseudoStu.loadScheduleFromDB(Courses.getInstance());
            return "success";
        }
        return "failure";
    }

    @PostMapping("/login")
    public String postData(@RequestBody Map<String, String> data) {
        if(!authenticated){
            String username = data.get("username").toString();
            String password = data.get("password").toString();

            authenticate(username, password);
            // Process the data here
            if(authenticated && typeOfUser.equals("student"))
                return "student";
            else if(authenticated && typeOfUser.equals("admin"))
                return "admin";
            else
                return "invalid login!";
        }else return "already logged in!";

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
        stu = null;
        admin = null;
    }

    @GetMapping("/accountInfoStu")
    public List<String> getStuAccInfo(){
        return getAccountInfo(stu);
    }

    @GetMapping("/accountInfoPseudoStu")
    public List<String> getPseudoStuAccInfo(){
        return getAccountInfo(pseudoStu);
    }

    public List<String> getAccountInfo(Student s) {
        if(s != null) {
            if(s.schedule != null && s.schedule.getApproved()) {
                return List.of(s.name, s.username, "Approved", s.getMajor(), s.getMinor());
            }
            return List.of(s.name, s.username, "Not Approved", s.getMajor(), s.getMinor());
        } else{
            return List.of("","","", "", "");
        }
    }

    @PostMapping("/changeName")
    public void changeName(@RequestBody Map<String, String> data) {
        String newName = data.get("name");
        String use = data.get("type");
        if(use.equals("Stu")){
            stu.setName(newName);
        }else{
            pseudoStu.setName(newName);
        }

    }

    @PostMapping("/changeMajor")
    public void changeMajor(@RequestBody Map<String, String> data) {
        String newMajor = data.get("major");
        String use = data.get("type");
        if(use.equals("Stu")){
            stu.setMajor(newMajor);
            saveMajor(newMajor, stu);
        }else{
            pseudoStu.setMajor(newMajor);
            saveMajor(newMajor, pseudoStu);
        }

    }

    @PostMapping("/changeMinor")
    public void changeMinor(@RequestBody Map<String, String> data) {
        String newMinor = data.get("minor");
        String use = data.get("type");
        if(use.equals("Stu")){
            stu.setMinor(newMinor);
            saveMinor(newMinor, stu);
        }else{
            pseudoStu.setMinor(newMinor);
            saveMinor(newMinor, pseudoStu);
        }

    }

    @PostMapping("/newCalendarStu")
    public boolean newCalendarStu(@RequestBody Map<String, String> data) {
        String name4Schedule = data.get("nameForSchedule");
        String semester = data.get("semester");
        stu.createNewSchedule(name4Schedule, semester, refCourses);
        saveSchedule("student");
        return true;
    }

    @PostMapping("/newCalendarPseudoStu")
    public boolean newCalendarPseudo(@RequestBody Map<String, String> data) {
        String name4Schedule = data.get("nameForSchedule");
        String semester = data.get("semester");
        if(admin != null && typeOfUser.equals("admin") && pseudoStu != null){
            pseudoStu.createNewSchedule(name4Schedule, semester, refCourses);
            System.out.println("4");
            saveSchedule("pseudoStudent");
            return true;
        }
        return false;

    }

    @GetMapping("/statusSheet")
    public List<String> statusSheetGet() {
        List<String> c = new ArrayList<String>();
        if(stu != null)
        if(typeOfUser.equals("student") && stu.statusSheet != null)
            for(Course x : stu.statusSheet.getCourses().courses) {
                c.add(x.getCourseCode());
            }
        else{
            List<String> useless = new ArrayList<>();
            return useless;
        }
        return c;
    }

    @PostMapping("/statusSheet")
    public boolean statusSheetPost(@RequestBody Map<String, String> data) {
        String courseCode = data.get("code");
        if(stu != null) {
            if (stu.statusSheet != null) {
                return stu.statusSheet.addCourse(courseCode);
            } else {
                List ma = new ArrayList<>();
                List mi = new ArrayList<>();
                ma.add((stu.getMajor()));
                mi.add((stu.getMinor()));
                stu.statusSheet = new StatusSheet(ma, mi, stu.getGradYear());
                return stu.statusSheet.addCourse(courseCode);
            }
        }
        return false;
    }

    @GetMapping("/pseudoStatus")
    public boolean getPseudoStatus(){
        if(pseudoStu == null) return false;
        return true;
    }

    @GetMapping("/calendarPseudoStu")
    public List<List<String>> getCalendarTwo() {
        List<Course> c;
        if(pseudoStu != null)
            System.out.println(pseudoStu.schedule);
        if(typeOfUser.equals("admin") && pseudoStu.schedule != null)
            c = pseudoStu.schedule.getCourseList().courses;
        else{
            List<List<String>> useless = new ArrayList<>();
            return useless;
        }
        if(pseudoStu.schedule.getCourseList().courses != null) {
            c = pseudoStu.schedule.getCourseList().courses;
        } else {
            c = Collections.<Course>emptyList();
        }
        List<String> courses = new ArrayList<>();
        List<String> days = new ArrayList<>();
        List<String> times = new ArrayList<>();
        for(Course x : c) {
            courses.add(x.getCourseCode());
            days.add(x.getDay());
            times.add(x.getTime());
        }
        List<List<String>> codeAndTime = new ArrayList<>();
        codeAndTime.add(courses);
        codeAndTime.add(days);
        codeAndTime.add(times);
        return codeAndTime;
    }

    @GetMapping("/calendarStu")
    public List<List<String>> getCalendar() {
        List<Course> c;
        if(stu != null)
            System.out.println(stu.schedule);
        if(typeOfUser.equals("student") && stu.schedule != null)
            c = stu.schedule.getCourseList().courses;
        else{
            List<List<String>> useless = new ArrayList<>();
            return useless;
        }
        if(stu.schedule.getCourseList().courses != null) {
            c = stu.schedule.getCourseList().courses;
        } else {
            c = Collections.<Course>emptyList();
        }
        List<String> courses = new ArrayList<>();
        List<String> days = new ArrayList<>();
        List<String> times = new ArrayList<>();
        for(Course x : c) {
            courses.add(x.getCourseCode());
            days.add(x.getDay());
            times.add(x.getTime());
        }
        List<List<String>> codeAndTime = new ArrayList<>();
        codeAndTime.add(courses);
        codeAndTime.add(days);
        codeAndTime.add(times);
        return codeAndTime;
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
            System.out.println("RIGHT HERE!");
            stu.loadScheduleFromDB(Courses.getInstance());
            System.out.println("IT WORKED!");
            authenticated = true;
        }
        return authenticated;
    }

    public boolean isAuthen() {
        return authenticated;
    }

    private void saveSchedule(String user) {
        Schedule schedule;
        if(user.equals("student"))
           schedule = stu.schedule;
        else
            schedule = pseudoStu.schedule;
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

    /**
     *
     * @param major
     * @param s major is the reference (student or pseudoStudent, depending on the frontend)
     */
    void saveMajor(String major, Student s) {
        String sql = "update Students set majors = ? where id = ?";
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, major);
            pstmt.setInt(2, s.getId());
            pstmt.executeUpdate();
            conn.close();
            System.out.println("Updated Major");
        } catch (SQLException e) {
            System.out.println("Failed to update major..." + e.getMessage());
        }
    }

    /**
     *
     * @param minor
     * @param s major is the reference (student or pseudoStudent, depending on the frontend)
     */
    void saveMinor(String minor, Student s) {
        String sql = "update Students set minors = ? where id = ?";
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:NemoDB.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, minor);
            pstmt.setInt(2, s.getId());
            pstmt.executeUpdate();
            conn.close();
            System.out.println("Updated Minor");
        } catch (SQLException e) {
            System.out.println("Failed to update minor..." + e.getMessage());
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

    @PostMapping("/SearchStudents")
    public List<Student> studentSearch(@RequestBody Map<String, String> data){
        String input = data.get("usernameSearch").strip();
        Student[] students = searchStudents(input);
        List<Student> searchResults = Arrays.asList(students);
        return searchResults.stream().distinct().collect(Collectors.toList());
    }


    @PostMapping("/SearchResults")
    public Course[] searchResults(@RequestBody Map<String, String> data){
        String input = data.get("content").strip();
        String count = data.get("numFilters");
        if(count.equals("0")){
            return new Course[0];
        }
        return searchCourses(input);
    }

    /**
     *
     * @param data -> Map of input from the user (Str, Str)
     * @return if course successfully added (or type of user isn't a student) return empty array
     *          else return an array of other courses that the user could take
     */
    @PostMapping("/addCourseStu")
    public Course[] addToSchedule(@RequestBody Map<String, String> data){
        String courseCode = data.get("courseCode");
        if(typeOfUser.equals("student")) {
            boolean validResult = stu.schedule.addCourse(courseCode);
            if(validResult)
                saveSchedule("student");
            else{
                Course[] suggested1 = searchCourses("course code_" + courseCode.substring(0,courseCode.length() - 1));
                int numSameCourseCodes = 0;
                for(Course c: suggested1){
                    if(c.getCourseCode().equals(courseCode))
                        numSameCourseCodes++;
                }
                Course[] suggested2 = new Course[suggested1.length - numSameCourseCodes];
                int j = 0;
                for(int i = 0; i < suggested1.length; i++){
                    if(!courseCode.equals(suggested1[i].getCourseCode())){
                        suggested2[j] = suggested1[i];
                        j++;
                    }
                }
                return suggested2;
            }
            return null;
        }
        return new Course[0];
    }
    @PostMapping("/addCoursePseudoStu")
    public Course[] addToSchedulePseudoStu(@RequestBody Map<String, String> data){
        String courseCode = data.get("courseCode");
        if(typeOfUser.equals("admin") && pseudoStu != null) {
            boolean validResult = pseudoStu.schedule.addCourse(courseCode);
            if(validResult)
                saveSchedule("pseudoStudent");
            else{
                Course[] suggested1 = searchCourses("course code_" + courseCode.substring(0,courseCode.length() - 1));
                int numSameCourseCodes = 0;
                for(Course c: suggested1){
                    if(c.getCourseCode().equals(courseCode))
                        numSameCourseCodes++;
                }
                Course[] suggested2 = new Course[suggested1.length - numSameCourseCodes];
                int j = 0;
                for(int i = 0; i < suggested1.length; i++){
                    if(!courseCode.equals(suggested1[i].getCourseCode())){
                        suggested2[j] = suggested1[i];
                        j++;
                    }
                }
                return suggested2;
            }
            return null;
        }
        return new Course[0];
    }

    @PostMapping("/removeCourse")
    public boolean removeToSchedule(@RequestBody Map<String, String> data){
        String courseCode = data.get("code");
        if(typeOfUser.equals("student")) {
            stu.schedule.removeCourse(courseCode);
            saveSchedule("student");
            return true;
        }else if(typeOfUser.equals("admin") && pseudoStu != null){
            pseudoStu.schedule.removeCourse(courseCode);
            saveSchedule("pseudoStudent");
            return true;
        }
        return false;
    }

    /**
     * @param courseCodeSearchVal is the name of the course the user is searching for
     * @return an array of courses with course codes that contains the search value
     */
    public Course[] searchCourses(String courseCodeSearchVal) {
        String[] filters = courseCodeSearchVal.split(";");
        System.out.println("Filters: " + Arrays.deepToString(filters));
        CourseSearch cs = new CourseSearch();
        for (String f : filters) {
            System.out.println("f: " + f);
            String[] kv = f.split("_");
            if (kv.length > 1) {
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
