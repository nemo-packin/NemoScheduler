package edu.gcc.nemo.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class Schedule extends CourseList {
    List<Course> courses = new ArrayList<>();
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

    public void addCourseToSchedule(String courseCode){
        for (int i = 0; i < allCourseList.length; i++){
            if (allCourseList[i].getCourseCode().equals(courseCode)){
                courses.add(allCourseList[i]);
            }
        }
    }

    public void checkOverlap(Schedule schedule){
        int conflicts = 0;
        List<Course> overlappingCourses = new ArrayList<>();
        for (int i = 0; i < schedule.courses.size(); i++){
            Course temp = courses.get(i);
            String day = schedule.courses.get(i).getDay();
            String time = schedule.courses.get(i).getTime();
            for (int j = 0; j < courses.size(); j++){
                if (courses.get(j).getDay().equals(day) && courses.get(j).getTime().equals(time)){
                    conflicts++;
                }
                if (conflicts > 1){
                    overlappingCourses.add(temp);
                    overlappingCourses.add(courses.get(j));
                } else {
                    overlappingCourses.remove(temp);
                }
            }
        }
        System.out.println("your schedule has " + overlappingCourses.size() + " conflicts.");
        if (overlappingCourses.size() > 0){
            System.out.print("the overlapping courses are: ");
            for (int i = 0; i < overlappingCourses.size(); i++){
                System.out.print(overlappingCourses.get(i).getCourseCode());
            }
            System.out.print("\n");
        }
    }

    public void saveSchedule(Schedule schedule){
        try {
            File savedSchedule = new File("saved_schedule.txt");
            savedSchedule.createNewFile();
            FileWriter myWriter = new FileWriter("saved_schedule.txt");
            StringBuilder scheduleString = new StringBuilder("");
            System.out.println(schedule.courses.size());
            for (int i = 0; i < schedule.courses.size(); i++){
                scheduleString.append(schedule.courses.toString());
            }
            myWriter.write(String.valueOf(scheduleString));
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
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
