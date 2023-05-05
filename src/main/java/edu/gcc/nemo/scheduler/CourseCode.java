package edu.gcc.nemo.scheduler;

import java.util.Objects;

public class CourseCode extends CourseLike {
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
}
