package edu.gcc.nemo.scheduler;

public class Course {
    private String courseCode;
    private String department;
    private String semester;
    private String time;
    private String day;
    private String prof;
    private String name;
    private int creditHours;
    private int capacity;

    // Constructor
    public Course(String courseCode, String department, String semester, String time, String day,
                  String prof, String name, int creditHours, int capacity) {
        this.courseCode = courseCode;
        this.department = department;
        this.semester = semester;
        this.time = time;
        this.day = day;
        this.prof = prof;
        this.name = name;
        this.creditHours = creditHours;
        this.capacity = capacity;
    }

    // toString method for testing
    public String courseInfo(){
        return "Course Code: " + courseCode + "\n" +
                "Name: " + name + "\n" +
                "Department: " + department + "\n" +
                "Semester: " + semester + "\n" +
                "Time: " + time + "\n" +
                "Day: " + day + "\n" +
                "Professor: " + prof + "\n" +
                "Credit Hours: " + creditHours + "\n" +
                "Capacity: " + capacity + "\n" +
                "------------------------ \n\n";
    }

    //Getters and Setters
    public String getCourseCode() {
        return courseCode;
    }

    public String getDepartment() {
        return department;
    }

    public String getSemester() {
        return semester;
    }

    public String getTime() {
        return time;
    }

    public String getDay() {
        return day;
    }

    public String getProf() {
        return prof;
    }

    public String getName() {
        return name;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString(){
        return courseCode + " " + day + " " + prof;
    }

}
