//package edu.gcc.nemo.scheduler;
//
//import spark.Route;
//
//import java.lang.reflect.Method;
//import java.lang.reflect.Parameter;
//import java.util.Set;
//
//import static spark.Spark.*;
//
//public class MainTwo {
//
//    public static void main(String[] args) {
//
//        before((request, response) -> {
//            if (request.session().isNew()) {
//                request.session().attribute("session", new Session());
//            }
//        });
//
//        before("/protected/*", (request, response) -> {
//            if(getSession(request).user == null)
//                halt(401, "Not authenticated");
//        });
//
//        post("/authenticate/", (request, response) -> {
//            getSession(request).authenticate();
//            return 200;
//        });
//    }
//
//    private static Session getSession(spark.Request request) {
//        return (Session)request.session().attribute("session");
//    }
//
//    private static Route makeRoute(String methodName) {
//        return ((request, response) -> {
//            Session session = getSession(request);
//            Method toRun = session.getClass().getMethod(methodName);
//            Parameter[] parameters = toRun.getParameters();
//            Set<String> passedParams = request.queryParams();
//            for(Parameter parameter : parameters) {
//                String name = parameter.getName();
//                if(!passedParams.contains(name))
//                    halt(300);
//                String val = request.queryParams(name);
//                Class c = parameter.getType();
//            }
//            return null;
//        });
//    }
//}
