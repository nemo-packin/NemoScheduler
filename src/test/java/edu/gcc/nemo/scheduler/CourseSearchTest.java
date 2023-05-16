package edu.gcc.nemo.scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.List;

class CourseSearchTest {
    private CourseSearch courseSearch;

    @Test
    void testAddFilter() {
        // add a filter and verify it is in the list
        courseSearch = new CourseSearch();
        CourseFilter filter = new CourseFilter(CourseFieldNames.name, "Computer Science I");
        courseSearch.addFilter(filter);
        assertEquals(1, courseSearch.getFilter().size());
        assertEquals(filter, courseSearch.getFilter().get(0));
    }

    @Test
    void testAddFilterDuplicate() {
        // add a filter twice and verify only one is in the list
        courseSearch = new CourseSearch();
        CourseFilter filter = new CourseFilter(CourseFieldNames.name, "Computer Science I");
        courseSearch.addFilter(filter);
        courseSearch.addFilter(filter);
        assertEquals(1, courseSearch.getFilter().size());
    }

    @Test
    void testAddFilterByFieldNameAndValue() {
        // add a filter by field name and value and verify it is in the list
        courseSearch = new CourseSearch();
        courseSearch.addFilter(CourseFieldNames.name, "Computer Science I");
        assertEquals(1, courseSearch.getFilter().size());
        assertEquals(CourseFieldNames.name, courseSearch.getFilter().get(0).getFieldName());
        assertEquals("[Computer Science I]", courseSearch.getFilter().get(0).getValues().toString());
    }

    @Test
    void testAddFilterByFieldNameValueAndMatchType() {
        // add a filter by field name, value, and match type and verify it is in the list
        courseSearch = new CourseSearch();
        courseSearch.addFilter(CourseFieldNames.department, "COMP", FilterMatchType.CONTAINS);
        assertEquals(1, courseSearch.getFilter().size());
        assertEquals(CourseFieldNames.department, courseSearch.getFilter().get(0).getFieldName());
        assertEquals("[COMP]", courseSearch.getFilter().get(0).getValues().toString());
        assertEquals(FilterMatchType.CONTAINS, courseSearch.getFilter().get(0).matchType);
    }

    @Test
    void testRemoveFilters() {
        // add two filters and remove one
        courseSearch = new CourseSearch();
        courseSearch.addFilter(CourseFieldNames.name, "Computer Science I");
        courseSearch.addFilter(CourseFieldNames.department, "COMP");
        courseSearch.removeFilters(CourseFieldNames.name);
        assertEquals(1, courseSearch.getFilter().size());
        assertEquals(CourseFieldNames.department, courseSearch.getFilter().get(0).getFieldName());
    }

    @Test
    void testGetResults() {
        courseSearch = new CourseSearch();
        List<Course> expectedCourses = courseSearch.getResults();
        assertEquals(expectedCourses, courseSearch.getResults());
    }

    @Test
    void testAddFilter2() {
        courseSearch = new CourseSearch();
        CourseFilter filter = new CourseFilter(CourseFieldNames.name, "Mathematics");
        courseSearch.addFilter(filter);
        Assertions.assertTrue(courseSearch.getResults().stream().allMatch(course -> course.getName().equals("Mathematics")));
    }

    @Test
    void testAddMultipleFilters() {
        courseSearch = new CourseSearch();
        CourseFilter filter1 = new CourseFilter(CourseFieldNames.name, "Mathematics");
        CourseFilter filter2 = new CourseFilter(CourseFieldNames.creditHours, "4");
        courseSearch.addFilter(filter1);
        courseSearch.addFilter(filter2);
        Assertions.assertTrue(courseSearch.getResults().stream().allMatch(course -> course.getName().equals("Mathematics") && course.getCreditHours() == 4));
    }

    @Test
    void testRemoveFilters2() {
        courseSearch = new CourseSearch();
        CourseFilter filter1 = new CourseFilter(CourseFieldNames.name, "Mathematics");
        CourseFilter filter2 = new CourseFilter(CourseFieldNames.creditHours, "4");
        courseSearch.addFilter(filter1);
        courseSearch.addFilter(filter2);
        courseSearch.removeFilters(CourseFieldNames.creditHours);
        Assertions.assertTrue(courseSearch.getResults().stream().allMatch(course -> course.getName().equals("Mathematics")));
    }
}