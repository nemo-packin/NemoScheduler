package edu.gcc.nemo.scheduler;

import edu.gcc.nemo.scheduler.DB.Courses;

import java.util.*;
import java.util.stream.Collectors;

public class CourseSearch {
    private List<Course> courses;
    private List<CourseFilter> filters;
    private boolean changed = false;
    public CourseSearch() {
        this.courses = Courses.getInstance().getAllCourses();
        this.filters = new ArrayList<>();
    }

    public CourseSearch(List<CourseFilter> filters) {
        List<String> conditions = filters.stream().map(CourseFilter::toString).collect(Collectors.toList());
        List<List<String>> values = filters.stream().map(CourseFilter::getValues).collect((Collectors.toList()));
        this.filters = filters;
        this.courses = Courses.getInstance().runQuery(conditions, values);
    }

    public CourseSearch(CourseFilter ...filters) {
        this.filters = Arrays.stream(filters).collect(Collectors.toList());
        List<String> conditions = this.filters.stream().map(CourseFilter::toString).collect(Collectors.toList());
        List<List<String>> values = this.filters.stream().map(CourseFilter::getValues).collect((Collectors.toList()));
        this.courses = Courses.getInstance().runQuery(conditions, values);
    }

    public void addFilter(CourseFilter filter) {
        changed = true;
        for (int i = filters.size() - 1; i >= 0 ; i--) {
            if(filters.get(i).getFieldName() == filter.getFieldName() && filters.get(i).matchType == filter.matchType) {
                filters.remove(i);
            }
        }
        filters.add(filter);
    }

    public void addFilter(CourseFieldNames fieldName, String value) {
        changed = true;
        CourseFilter f = new CourseFilter(fieldName, value);
        for (int i = filters.size() - 1; i >= 0 ; i--) {
            if(filters.get(i).getFieldName() == f.getFieldName() && filters.get(i).matchType == f.matchType) {
                filters.get(i).addValue(value);
                return;
            }
        }
        filters.add(f);
    }

    public void addFilter(CourseFieldNames fieldName, String value, FilterMatchType matchType) {
        changed = true;
        CourseFilter f = new CourseFilter(fieldName, value);
        f.matchType = matchType;
        for (int i = filters.size() - 1; i >= 0 ; i--) {
            if(filters.get(i).getFieldName() == f.getFieldName() && filters.get(i).matchType == f.matchType) {
                filters.get(i).addValue(value);
                return;
            }
        }
        filters.add(f);
    }

    public void removeFilters(CourseFieldNames fieldName) {
        changed = true;
        for (int i = filters.size() - 1; i >= 0 ; i--) {
            if(filters.get(i).getFieldName() == fieldName) {
                filters.remove(i);
            }
        }
    }

    public List<Course> getResults() {
        if(changed) {
            List<String> conditions = filters.stream().map(CourseFilter::toString).collect(Collectors.toList());
            List<List<String>> values = filters.stream().map(CourseFilter::getValues).collect((Collectors.toList()));
            this.courses = Courses.getInstance().runQuery(conditions, values);
            changed = false;
        }
        return this.courses;
    }

    public List<CourseFilter> getFilter() {
        return filters;
    }
}
