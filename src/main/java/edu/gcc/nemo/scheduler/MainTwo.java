package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Admins;
import edu.gcc.nemo.scheduler.DB.Courses;
import edu.gcc.nemo.scheduler.DB.Students;
import spark.Route;

import static spark.Spark.*;

public class MainTwo {

    public static void main(String[] args) {
        Students students = Students.getStudentsInstance();
        Admins admins = Admins.getAdminsInstance();
        Courses courses = Courses.getCoursesInstance();

        before((request, response) -> {
            if (request.session().isNew()) {
                request.session().attribute("session", new Session(admins, students, courses));
            }
        });

        before("/protected/*", (request, response) -> {
            if(getSession(request).getAdmin() == null && getSession(request).getStu() == null)
                halt(401, "Not authenticated");
        });

        post("/authenticate/", (request, response) -> {
            getSession(request).authenticate(request.);
            return 200;
        });
    }

    private static Session getSession(spark.Request request) {
        return (Session)request.session().attribute("session");
    }
}
