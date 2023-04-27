package edu.gcc.nemo.scheduler.util;

import java.util.List;

public class Degree {
    String name;
    int totalHours;
    List<DegSection> sections;
    String source;

    @Override
    public String toString() {
        return "Degree{" +
                "name='" + name + '\'' +
                ", totalHours=" + totalHours +
                ", sections=" + sections +
                '}';
    }
}
