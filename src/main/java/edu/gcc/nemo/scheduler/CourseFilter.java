package edu.gcc.nemo.scheduler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CourseFilter extends Filter {
    private CourseFieldNames fieldName;

    public CourseFilter(CourseFieldNames fieldName) {
        super();
        this.fieldName = fieldName;
    }

    public CourseFilter(CourseFieldNames fieldName, List<String> values) {
        super();
        this.fieldName = fieldName;
        this.values = values;
    }

    public CourseFilter(CourseFieldNames fieldName, String ...values) {
        super();
        this.fieldName = fieldName;
        this.values = Arrays.asList(values);
    }



    @Override
    public String toString() {
        switch(matchType) {
            case EQUALS:
                return values.stream().map((value) -> fieldName + " = ?").collect(Collectors.joining(" OR "));
            case CONTAINS:
                return values.stream().map((value) -> fieldName + " LIKE ?").collect(Collectors.joining(" OR "));
            case NEQUALS:
                return values.stream().map((value) -> fieldName + "!= ?").collect(Collectors.joining(" OR "));
            default:
                return "";
        }
    }

    public List<String> getValues(){
        return values;
    }
    public CourseFieldNames getFieldName() {
        return fieldName;
    }
}
