package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Students;
import edu.gcc.nemo.scheduler.DB.Admins;
import edu.gcc.nemo.scheduler.DB.Courses;
import org.eclipse.jetty.util.StringUtil;

import java.util.ArrayList;

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

    public boolean authenticate(String login, String password) {
        if(refStudents.getStudent(login) != null) {
            stu = refStudents.getStudent(login);
            typeOfUser = "student";
        }
        else if(refAdmins.getAdmin(login) != null) {
            admin = refAdmins.getAdmin(login);
            typeOfUser = "admin";
        }else{
            System.out.println("Failed to authenticate!");
        }
        if(admin != null && admin.account.password.equals(password)) {
            authenticated = true;
        }else if(stu != null && stu.account.password.equals(password)){
            authenticated = true;
        }
        return authenticated;
    }

    public boolean isAuthen(){
        return authenticated;
    }

    void saveSchedule() {

    }

    void editSchedule() {

    }

    /**
     * @param stuNameSearchVal is the name the user is searching for
     * @return returns an arraylist of all names that contains the search value
     */
    public Student[] searchStudents(String stuNameSearchVal) {
        if(authenticated && typeOfUser.equals("admin")){
            ArrayList<String> listOfStuNames = refStudents.getListOfAllStudentUsernames();
            ArrayList<String> resultNameMatches = new ArrayList<>();
            for(String names : listOfStuNames){
                if(names.contains(stuNameSearchVal))
                    resultNameMatches.add(names);
            }
            Student[] stuArr = new Student[resultNameMatches.size()];
            for(int i = 0; i < stuArr.length; i++){
                stuArr[i] = refStudents.getStudent(refStudents.getLogin(resultNameMatches.get(i)));
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
        String courseCodeSearchVal2 = courseCodeSearchVal.replace(" ","");
        ArrayList<String> listOfCourseCodes = refCourses.getAllCourseCodes();
        ArrayList<String> resultCourseCodeMatches = new ArrayList<>();
        for(String cc: listOfCourseCodes){
            if(cc.contains(courseCodeSearchVal2))
                resultCourseCodeMatches.add(cc);
        }
        Course[] courseArr = new Course[resultCourseCodeMatches.size()];
        for(int i = 0; i < courseArr.length; i++){
            courseArr[i] = refCourses.getCourse(resultCourseCodeMatches.get(i));
        }
        return courseArr;
    }

    public String getSchedule() {
        return null;
    }

    /**
     * this is used for an admin specifically selecting a student
     * @param username
     * @return returns the instance of that student
     */
    public Student getStudent(String username){
        if(authenticated && typeOfUser.equals("admin")){
            return refStudents.getStudent(refStudents.getLogin(username));
        }
        System.out.println("You do not have proper credentials!");
        return null;
    }

    public String getStatusSheet() {
        if(typeOfUser.equals("student"))
            return stu.account.statusSheet.toString();
        System.out.println("You do not have a status sheet");
        return null;
    }

    /**
     * @returns the type of user on this session
     */
    public String getTypeOfUser(){
        return typeOfUser;
    }

}
