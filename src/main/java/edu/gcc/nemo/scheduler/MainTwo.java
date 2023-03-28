package edu.gcc.nemo.scheduler;

import spark.Route;

import static spark.Spark.*;

public class MainTwo {

    public static void main(String[] args) {

        before((request, response) -> {
            if (request.session().isNew()) {
                request.session().attribute("session", new Session());
            }
        });

        before("/protected/*", (request, response) -> {
            if(getSession(request).user == null)
                halt(401, "Not authenticated");
        });

        post("/authenticate/", (request, response) -> {
            getSession(request).authenticate();
            return 200;
        });
    }

    private static Session getSession(spark.Request request) {
        return (Session)request.session().attribute("session");
    }
}
