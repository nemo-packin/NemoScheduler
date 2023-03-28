package edu.gcc.nemo.scheduler;

import org.eclipse.jetty.util.MultiMap;

import java.io.StringWriter;
import java.util.*;

public class Schedule extends CourseList {
    private Boolean isApproved;
    private String semester;

    // Constructor
    public Schedule(String semester){
        isApproved = false;
        this.semester = semester;
    }

    //Methods
    public String serialize() {
        return null;
    }

    public void approve() {
        isApproved = true;
        System.out.println("You're class was approved!");
    }

    // TEMPORARY toString method to check classes added to schedule
    public String toString(){
        String listOfCoursesInSchedule = "Courses in you're schedule include: \n";
        for(Course c: courses){
            listOfCoursesInSchedule += c.toString();
        }
        return listOfCoursesInSchedule;
    }

    public String calendarView() {
        //Set up the format for storing once the data is parsed
        ArrayList<Course> Monday = new ArrayList<Course>();
        ArrayList<Course> Tuesday = new ArrayList<Course>();
        ArrayList<Course> Wednesday = new ArrayList<Course>();
        ArrayList<Course> Thursday = new ArrayList<Course>();
        ArrayList<Course> Friday = new ArrayList<Course>();
        Map<String, ArrayList<Course>> days = new LinkedHashMap<String, ArrayList<Course>>();
        days.put("M", Monday);
        days.put("T", Tuesday);
        days.put("W", Wednesday);
        days.put("Th", Thursday);
        days.put("F", Friday);
        Map<String, String> prefixes = new LinkedHashMap<String, String>();
        prefixes.put("M", "Monday");
        prefixes.put("T", "Tuesday");
        prefixes.put("W", "Wednesday");
        prefixes.put("Th", "Thursday");
        prefixes.put("F", "Friday");
        String[] times = {"8:00am", "8:15am", "8:30am", "8:45am", "9:00am", "9:15am", "9:30am", "9:45am",
                "10:00am", "10:15am", "10:30am", "10:45am", "11:00am", "11:15am", "11:30am", "11:45am",
                "12:00pm", "12:15pm", "12:30pm", "12:45pm", "1:00pm", "1:15pm", "1:30pm", "1:45pm",
                "2:00pm", "2:15pm", "2:30pm", "2:45pm", "3:00pm", "3:15pm", "3:30pm", "3:45pm",
                "4:00pm", "4:15pm", "4:30pm", "4:45pm", "5:00pm", "5:15pm", "5:30pm", "5:45pm",
                "6:00pm", "6:15pm", "6:30pm", "6:45pm", "7:00pm", "7:15pm", "7:30pm", "7:45pm"};

        //Add the courses to their correct days map
        for(Course c : courses) {
            String d = c.getDay();
            String[] parsed = d.split("(?=\\p{Upper})");
            for(String p : parsed) {
                switch (p) {
                    case "M": Monday.add(c); break;
                    case "T": Tuesday.add(c); break;
                    case "W": Wednesday.add(c); break;
                    case "Th": Thursday.add(c); break;
                    case "F": Friday.add(c); break;
                    default:
                }
            }
        }

        //Create the string to return
        Course holdCourse = null;
        int repeatsLeft = 0;
        String finalString = "";
        finalString += ("============================================\n");
        finalString += ("Schedule View\n");
        finalString += ("============================================\n");
        //For each day of the week
        for(String key : days.keySet()) {
            finalString += "++++" + prefixes.get(key) + "++++" + "\n";
            //For every 15 minute increment
            for(String time : times) {
                finalString += (time + " - ");
                //For every course that day
                for(Course it : days.get(key)) {
                    //If the course is at that time
                    if(it.getTime().equals(time)) {
                        if(key.equals("T") || key.equals("Th")) {
                            repeatsLeft = 5;
                            
                        } else {
                            repeatsLeft = 4;
                        }
                        holdCourse = it;
                    }
                }
                if(repeatsLeft > 0) {
                    finalString += holdCourse.getCourseCode();
                    repeatsLeft --;
                }
                finalString += "\n";
            }
        }
        finalString += ("============================================\n");;
        return finalString;
    }

    //Getters and Setters
    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
