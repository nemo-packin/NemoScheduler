package edu.gcc.nemo.scheduler;

import java.util.List;
import java.util.stream.Collectors;

public class StudentFilter extends Filter{
    private StudentFieldNames fieldName;
    StudentFilter(StudentFieldNames fieldName) {
        this.fieldName = fieldName;
    }

    public StudentFilter(StudentFieldNames fieldName, List<String> values) {
        super();
        this.fieldName = fieldName;
        this.values = values;
    }

    @Override
    public String toString() {
        return values.stream().map((value) -> fieldName + value).collect(Collectors.joining(" OR "));
    }

    public StudentFieldNames getFieldName() {
        return fieldName;
    }
}
