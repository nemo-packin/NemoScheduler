package edu.gcc.nemo.scheduler.DB;

import edu.gcc.nemo.scheduler.CourseCode;
import edu.gcc.nemo.scheduler.CourseOptions;
import edu.gcc.nemo.scheduler.util.MinorParser;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class Minors {
    private static Minors instance;
    private List<CourseOptions> allMinors;

    public static Minors getInstance() {
        if(instance == null) {
            instance = new Minors();
        }
        return instance;
    }

    private Minors() {
        this.allMinors = MinorParser.parseMinorsAsCourseOptions();
    }
    public static void main(String[] args) {
        Minors m = getInstance();
//        m.getMinorTitles().forEach(System.out::println);
        m.getMinorRequirements("SOCIAL WORK").options.forEach(y -> System.out.println(((CourseCode)y).code));
    }
    public List<String> getMinorTitles() {
        return allMinors.stream().map(cl -> cl.title).filter(s -> !s.equals("BA and BS (non-science) General Education") && !s.equals("BM, BS, BSEE, and BSME (science) General Education")).collect(Collectors.toList());
    }

    public CourseOptions getMinorRequirements(String title) {
        return allMinors.stream().filter(courseOptions -> courseOptions.title.equals(title)).findFirst().orElse(null);
    }
}
