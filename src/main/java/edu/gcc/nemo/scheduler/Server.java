package edu.gcc.nemo.scheduler;


import edu.gcc.nemo.scheduler.DB.Admins;
import edu.gcc.nemo.scheduler.DB.Courses;
import edu.gcc.nemo.scheduler.DB.Students;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;

@SpringBootApplication
public class Server {
    private static Student stuSignedIn = null;
    private static Admin adminSignedIn = null;
    private static Connection conn;
    //singleton objects
    private static Students students;
    private static Admins admins;
    private static Courses courses;
    private static Session session;

    public static void main(String[] args){
        // created singleton objects
        students = Students.getInstance();
        admins = Admins.getAdminsInstance();
        courses = Courses.getInstance();
        SpringApplication.run(Server.class, args);

        session = new Session(admins, students, courses);
        State state = new State();
        boolean cont = true;
        while (cont) {

        }
        System.out.println("Goodbye nemo-er!");
    }
}
