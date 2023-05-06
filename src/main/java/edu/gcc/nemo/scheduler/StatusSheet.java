package edu.gcc.nemo.scheduler;

import com.google.gson.Gson;
import edu.gcc.nemo.scheduler.DB.Majors;

import java.util.ArrayList;
import java.util.List;

public class StatusSheet {
    private boolean isScienceMajor;
    private ArrayList<String> majors;
    private ArrayList<String> minors;
    private int gradYear;
    private CourseList courses;

    public static void main(String[] args) {
        StatusSheet s = new StatusSheet(true, List.of("Bachelor of Science Degree in Computer Science"), null, 2024);
        Gson g = new Gson();
        System.out.println(g.toJson(s.getRequirements()));
    }

    public StatusSheet(boolean isScienceMajor, List<String> majors, List<String> minors, int gradYear) {
        this.isScienceMajor = isScienceMajor;
        this.majors = new ArrayList<>();
        this.majors.addAll(majors);
        this.minors = new ArrayList<>();
        this.minors.addAll(minors);
        this.gradYear = gradYear;
        this.courses = new CourseList();
    }

    public boolean addCourse(String code) {
        return this.courses.addCourse(code);
    }

    public boolean removeCourse(String code) {
        return this.courses.removeCourse(code);
    }

    public int getGradYear() {
        return gradYear;
    }

    public void setGradYear(int gradYear) {
        this.gradYear = gradYear;
    }


    public CourseList getCourses() {
        return courses;
    }

    public List<CourseOptions> getRequirements() {
        Majors m = Majors.getInstance();
        ArrayList<CourseOptions> outList = new ArrayList<>();
        outList.add(isScienceMajor?
                m.getMajorRequirements("BM, BS, BSEE, and BSME (science) General Education") :
                m.getMajorRequirements("BA and BS (non-science) General Education"));
        majors.forEach(title -> outList.add(m.getMajorRequirements(title)));
        return outList;
    }

    public ArrayList<String> getMajors() {
        return majors;
    }

    public void addMajor(String major) {
        majors.add(major);
    }

    public void removeMajor(String major) {
        majors.removeIf(x -> x.equals(major));
    }
}
