package edu.gcc.nemo.scheduler;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CourseFilterTest {

    private CourseFilter courseFilter;

    @Test
    public void testConstructor() {
        courseFilter = new CourseFilter(CourseFieldNames.department);
        Assertions.assertEquals(CourseFieldNames.department, courseFilter.getFieldName());
        Assertions.assertTrue(courseFilter.getValues().isEmpty());
    }

    @Test
    public void testConstructorWithList() {
        List<String> values = Arrays.asList("CSC110", "CSC120");
        CourseFilter courseFilter = new CourseFilter(CourseFieldNames.department, values);
        Assertions.assertEquals(CourseFieldNames.department, courseFilter.getFieldName());
        Assertions.assertEquals(values, courseFilter.getValues());
    }

    @Test
    public void testConstructorWithVarargs() {
        CourseFilter courseFilter = new CourseFilter(CourseFieldNames.department, "MATH262", "COMP435");
        Assertions.assertEquals(CourseFieldNames.department, courseFilter.getFieldName());
        Assertions.assertEquals(Arrays.asList("MATH262", "COMP435"), courseFilter.getValues());
    }

    @Test
    public void testToStringEqualsMatchType() {
        Filter courseFilter = new CourseFilter(CourseFieldNames.courseCode);
        courseFilter.addValue("COMP350A");
        courseFilter.addValue("COMP342");
        courseFilter.matchType = FilterMatchType.EQUALS;
        Assertions.assertEquals("course_code = ? OR course_code = ?", courseFilter.toString());
    }

    @Test
    public void testToStringContainsMatchType() {
        Filter courseFilter = new CourseFilter(CourseFieldNames.courseCode);
        courseFilter.addValue("COMP");
        courseFilter.addValue("MATH");
        courseFilter.matchType = FilterMatchType.CONTAINS;
        Assertions.assertEquals("course_code LIKE ? OR course_code LIKE ?", courseFilter.toString());
    }

    @Test
    public void testToStringNEqualsMatchType() {
        Filter courseFilter = new CourseFilter(CourseFieldNames.courseCode);
        courseFilter.addValue("161");
        courseFilter.addValue("155");
        courseFilter.matchType = FilterMatchType.NEQUALS;
        Assertions.assertEquals("course_code!= ? OR course_code!= ?", courseFilter.toString());
    }

    @Test
    public void testAddValue() {
        Filter courseFilter = new CourseFilter(CourseFieldNames.courseCode);
        courseFilter.addValue("PSYC101");
        courseFilter.addValue("HUMA303");
        Assertions.assertEquals(Arrays.asList("PSYC101", "HUMA303"), courseFilter.getValues());
    }
}