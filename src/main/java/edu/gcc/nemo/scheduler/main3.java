package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Admins;
import edu.gcc.nemo.scheduler.DB.Courses;
import edu.gcc.nemo.scheduler.DB.Students;

import java.util.ArrayList;

public class main3 {
    public static void main(String[] args){
        // created singleton objects
        Students students = Students.getStudentsInstance();
        Admins admins = Admins.getAdminsInstance();
        Courses courses = Courses.getCoursesInstance();

        //TESTING SESSION FOR ADMIN
        Session session = new Session(admins, students, courses);
        System.out.println("Session successfully created!");

        Admin admin1 = new Admin("master1", "123", "masterDude", 1);
        System.out.println("Login is: " + admin1.account.login);
        session.authenticate(admin1.account.login, admin1.account.password);
        System.out.println(session.isAuthen());
        Student[] listOfSearchResults = session.searchStudents("jon");
        System.out.println("List of Students is:");
        for(Student stu : listOfSearchResults){
            System.out.println(stu);
        }

        Student stu = session.getStudent(listOfSearchResults[0].name);
        System.out.println("The following is the selected student: ");
        stu.printInfo();



        System.out.println("\n------------------------------------------------------\n");
        //TESTING SESSION FOR STUDENT
        Session session2 = new Session(admins, students, courses);
        System.out.println("Session successfully created!");
        Student student1 = new Student("kool1","123","jonathan", 32,2024);
        session2.authenticate(student1.account.login, student1.account.password);
        System.out.println(session2.isAuthen());
        Student[] listOfSearchResults2 = session2.searchStudents("jon");



        System.out.println("\n------------------------------------------------------\n");
        //TESTING SESSION FOR COURSE SEARCH!!!
        Course[] listCourseS1 = session2.searchCourses("COMP 4");
        for(Course c: listCourseS1){
            System.out.println(c);
        }

    }
}
