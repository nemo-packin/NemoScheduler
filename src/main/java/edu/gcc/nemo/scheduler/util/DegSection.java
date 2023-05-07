package edu.gcc.nemo.scheduler.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DegSection {
    String name;
    int hours;
    Map<String, List<String>> courses;

    @Override
    public String toString() {
        return "DegSection{" +
                "name='" + name + '\'' +
                ", hours=" + hours +
                ", courses=" + coursesToString() +
                '}';
    }

    private String coursesToString() {
        String map = courses.entrySet().stream().map(
                (entry) -> {
                    String listString = "[" + entry.getValue().stream().map(Object::toString).collect(Collectors.joining(", ")) + "]";
                    return entry.getKey() + " : " + listString;
                }).collect(Collectors.joining(", "));
        return "{" + map + "}";
    }
}
