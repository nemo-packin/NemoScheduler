package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "http://172.29.48.1:3000"}, allowCredentials = "true", allowedHeaders = "*", maxAge = 3600) // Update with the URL of the frontend application
public class SessionApi {

    public SessionApi() {
    }

    @GetMapping("/approveStudent")
    public boolean approveStu(HttpServletRequest r) {
        Session session = getSession(r);
        return session.approveStu();
    }

    @PostMapping("/createPseudoStudent")
    public String createPseudoStudent(@RequestBody Map<String, String> data, HttpServletRequest r) {
        Session session = getSession(r);
        return session.createPseudoStudent(data);
    }

    @PostMapping("/login")
    public String postData(@RequestBody Map<String, String> data, HttpServletRequest r) {
        Session session = getSession(r);
        return session.postData(data);

    }

    @GetMapping("/auth")
    public boolean getAuth(HttpServletRequest r) {
        Session session = getSession(r);
        return session.getAuth();
    }

    @GetMapping("/userType")
    public String userType(HttpServletRequest r) {
        Session session = getSession(r);
        return session.userType();
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest r) {
        Session session = getSession(r);
        session.logout();
    }

    @GetMapping("/accountInfoStu")
    public List<String> getStuAccInfo(HttpServletRequest r) {
        Session session = getSession(r);
        return session.getStuAccInfo();
    }

    @GetMapping("/accountInfoPseudoStu")
    public List<String> getPseudoStuAccInfo(HttpServletRequest r) {
        Session session = getSession(r);
        return session.getPseudoStuAccInfo();
    }

    @PostMapping("/changeName")
    public void changeName(@RequestBody Map<String, String> data, HttpServletRequest r) {
        Session session = getSession(r);
        session.changeName(data);
    }

    @PostMapping("/changeMajor")
    public void changeMajor(@RequestBody Map<String, String> data, HttpServletRequest r) {
        Session session = getSession(r);
        session.changeMajor(data);

    }

    @PostMapping("/changeMinor")
    public void changeMinor(@RequestBody Map<String, String> data, HttpServletRequest r) {
        Session session = getSession(r);
        session.changeMinor(data);

    }

    @PostMapping("/newCalendarStu")
    public boolean newCalendarStu(@RequestBody Map<String, String> data, HttpServletRequest r) {
        Session session = getSession(r);
        return session.newCalendarStu(data);
    }

    @PostMapping("/newCalendarPseudoStu")
    public boolean newCalendarPseudo(@RequestBody Map<String, String> data, HttpServletRequest r) {
        Session session = getSession(r);
        return session.newCalendarPseudo(data);

    }

    @GetMapping("/majorOptions")
    public List<String> majorOptionsGet(HttpServletRequest r) {
        Session session = getSession(r);
        return session.majorOptionsGet();
    }

    @GetMapping("/minorOptions")
    public List<String> minorOptionsGet(HttpServletRequest r) {
        Session session = getSession(r);
        return session.minorOptionsGet();
    }

    @GetMapping("/statusSheet")
    @ResponseBody
    public HashMap<String, Object> statusSheetGet(HttpServletRequest r) {
        Session session = getSession(r);
        return session.statusSheetGet();
    }

    @GetMapping("/recommendations")
    @ResponseBody
    public List<String> recommendationsGet(HttpServletRequest r) {
        Session session = getSession(r);
        return session.recommendationsGet();
    }

    @PostMapping("/statusSheet")
    public boolean statusSheetPost(@RequestBody Map<String, String> data, HttpServletRequest r) {
        Session session = getSession(r);
        return session.statusSheetPost(data);
    }

    @GetMapping("/pseudoStatus")
    public boolean getPseudoStatus(HttpServletRequest r) {
        Session session = getSession(r);
        return session.getPseudoStatus();
    }

    @GetMapping("/calendarPseudoStu")
    public List<List<String>> getCalendarTwo(HttpServletRequest r) {
        Session session = getSession(r);
        return session.getCalendarTwo();
    }

    @GetMapping("/calendarStu")
    public List<List<String>> getCalendar(HttpServletRequest r) {
        Session session = getSession(r);
        return session.getCalendar();
    }


    @PostMapping("/SearchStudents")
    public List<Student> studentSearch(@RequestBody Map<String, String> data, HttpServletRequest r) {
        Session session = getSession(r);
        return session.studentSearch(data);
    }


    @PostMapping("/SearchResults")
    public Course[] searchResults(@RequestBody Map<String, String> data, HttpServletRequest r) {
        Session session = getSession(r);
        return session.searchResults(data);
    }

    /**
     * @param data -> Map of input from the user (Str, Str)
     * @return if course successfully added (or type of user isn't a student) return empty array
     * else return an array of other courses that the user could take
     */
    @PostMapping("/addCourseStu")
    public Course[] addToSchedule(@RequestBody Map<String, String> data, HttpServletRequest r) {
        Session session = getSession(r);
        return session.addToSchedule(data);
    }

    @PostMapping("/addCoursePseudoStu")
    public Course[] addToSchedulePseudoStu(@RequestBody Map<String, String> data, HttpServletRequest r) {
        Session session = getSession(r);
        return session.addToSchedulePseudoStu(data);
    }

    @PostMapping("/removeCourse")
    public boolean removeToSchedule(@RequestBody Map<String, String> data, HttpServletRequest r) {
        Session session = getSession(r);
        return session.removeToSchedule(data);
    }

    private static Session getSession(HttpServletRequest r) {
        HttpSession s = r.getSession();
        Session session = (Session) s.getAttribute("session");
        if(session == null) {
            System.out.println("MAKING NEW SESSION!");
            session = new Session(Admins.getAdminsInstance(), Students.getInstance(), Courses.getInstance());
            s.setAttribute("session", session);
        }
        return session;
    }
}
