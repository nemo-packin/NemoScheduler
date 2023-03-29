package edu.gcc.nemo.scheduler;

import java.util.HashMap;
import java.util.Map;

public class tempAllCourseList {
    // THIS IS A TEMPORARY CLASS FOR A LIST OF CLASSES
    protected Course[] allCourseList;
    protected Map<String, Integer> codeIndex;

    // Constructor
    public tempAllCourseList(){
        allCourseList = new Course[5];
        codeIndex = new HashMap();
        allCourseList[0] = new Course("COMM 245 B", "Communication","Semester",
                "2:00pm","MWF","Tim Hawkins", "Digital Photography", 3, 25);
        codeIndex.put("COMM 245 B", 0);
        allCourseList[1] = new Course("COMP 342 A", "Computer Science","Spring",
                "9:00am","MWF","Zhang", "Data Communication and Networking", 3, 102);
        codeIndex.put("COMP 342 A", 1);
        allCourseList[2] = new Course("COMP 444 A", "Computer Science","Fall",
                "9:30am","TTh","Tim Hawkins", "IOS Development", 3, 5);
        codeIndex.put("COMP 444 A", 2);
        allCourseList[3] = new Course("COMP 435 B", "Computer Science","Semester",
                "12:00pm","MWF","Hutchins", "Intro to Machine Learning", 3, 18);
        codeIndex.put("COMP 435 B", 3);
        allCourseList[4] = new Course("PSYC A", "Psychology","Spring",
                "6:30pm","T","Who knows?", "Psyc transfer class", 1, 30);
        codeIndex.put("PSYC A", 4);
    }

    public Course getCourse(String code) {
        return allCourseList[codeIndex.get(code)];
    }

}
