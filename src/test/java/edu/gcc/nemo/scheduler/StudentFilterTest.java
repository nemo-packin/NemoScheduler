package edu.gcc.nemo.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

public class StudentFilterTest {

    private StudentFilter filter;
    private final List<String> testValues = Arrays.asList("CS", "EE");

    @BeforeEach
    public void setUp() {
        filter = new StudentFilter(StudentFieldNames.major, testValues);
    }

    @Test
    public void testToString() {
        assertEquals("majorCS OR majorEE", filter.toString());
    }

    @Test
    public void testGetFieldName() {
        assertEquals(StudentFieldNames.major, filter.getFieldName());
    }

    @Test
    public void testValues() {
        assertEquals(testValues, filter.getValues());
    }

//    @Test
//    public void testAddValue() {
//        filter.addValue("MATH");
//        assertTrue(filter.getValues().contains("MATH"));
//    }
}