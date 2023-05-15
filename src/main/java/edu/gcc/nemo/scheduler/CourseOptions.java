package edu.gcc.nemo.scheduler;

import java.util.ArrayList;
import java.util.List;

public class CourseOptions extends CourseLike {
    public String title;
    public List<CourseLike> options;
    public CourseOptions(String title, int creditHours) {
        this.title = title;
        this.creditHours = creditHours;
        this.options = new ArrayList<>();
    }

    public String toString(){
        String s = title + "\n" + creditHours + "\n";
        for (int i = 0; i < options.size(); i++){
            s = s + options.get(i);
        }
        s = s + "\n";
        return s;
    }
}
