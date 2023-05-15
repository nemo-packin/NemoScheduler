package edu.gcc.nemo.scheduler;

import com.google.gson.Gson;
import edu.gcc.nemo.scheduler.DB.Majors;
import edu.gcc.nemo.scheduler.DB.Minors;

import java.util.*;
import java.util.stream.Collectors;

public class StatusSheet {
    private boolean isScienceMajor;
    private ArrayList<String> majors;
    private ArrayList<String> minors;
    private int gradYear;
    private CourseList courses;

    public static void main(String[] args) {
        StatusSheet s = new StatusSheet(true, List.of("Bachelor of Science Degree in Computer Science"), null, 2024);
        s.getRecommendations();
        Gson g = new Gson();
        System.out.println(g.toJson(s.getRequirements()));
    }

    public StatusSheet(boolean isScienceMajor, List<String> majors, List<String> minors, int gradYear) {
        this.isScienceMajor = isScienceMajor;
        this.majors = new ArrayList<>();
        if (majors != null)
            this.majors.addAll(majors);
        this.minors = new ArrayList<>();
        if (minors != null)
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
        outList.add(isScienceMajor ?
                m.getMajorRequirements("BM, BS, BSEE, and BSME (science) General Education") :
                m.getMajorRequirements("BA and BS (non-science) General Education"));
        majors.forEach(title -> outList.add(m.getMajorRequirements(title)));
        CourseOptions mins = new CourseOptions("Minors", 0);
        minors.forEach(title -> {
            mins.options.add(Minors.getInstance().getMinorRequirements(title));
            mins.creditHours += Minors.getInstance().getMinorRequirements(title).creditHours;
        });
        outList.add(mins);
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

    public ArrayList<String> getMinors() {
        return majors;
    }

    public void addMinor(String minor) {
        majors.add(minor);
    }

    public void removeMinor(String minor) {
        majors.removeIf(x -> x.equals(minor));
    }

    public List<String> getRecommendations() {
        Set<CourseCode> uncompletedCourses = new TreeSet<>();
        uncompletedCourses.forEach(System.out::println);
        for (CourseOptions major : getRequirements()) {
            for (CourseLike sectionL : major.options) {
                CourseOptions section = (CourseOptions) sectionL;
                Set<CourseCode> candidateCourses = new HashSet<>();
                int completedHours = 0;
                for (CourseLike courseL : section.options) {
                    CourseCode course = (CourseCode) courseL;
                    if (completedHours < section.creditHours && !courses.courses.stream().anyMatch(x -> x.getCourseCode().startsWith(course.code))) {
                        candidateCourses.add(course);
                    } else if(courses.courses.stream().anyMatch(x -> x.getCourseCode().startsWith(course.code))) {
                        completedHours += course.creditHours;
                    }
                }
                if(completedHours < section.creditHours) {
                    uncompletedCourses.addAll(candidateCourses);
                }
            }
        }
        return uncompletedCourses.stream().map(courseCode -> courseCode.code).collect(Collectors.toList());
    }
}
