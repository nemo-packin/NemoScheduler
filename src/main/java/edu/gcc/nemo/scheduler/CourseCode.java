package edu.gcc.nemo.scheduler;

import java.util.Comparator;
import java.util.Objects;

public class CourseCode extends CourseLike implements Comparable {
    public String code;
    public CourseCode(String code, int creditHours) {
        this.code = code;
        this.creditHours = creditHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseCode that = (CourseCode) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public int compareTo(Object o) {
        CourseCode c = (CourseCode) o;
        if(c.code.length() < 6 || code.length() < 6) return 0;
        int n1 = Integer.parseInt(code.substring(code.length()-3));
        int n2 = Integer.parseInt(c.code.substring(c.code.length()-3));
        return n1 - n2;
    }
}
