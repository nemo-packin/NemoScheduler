package edu.gcc.nemo.scheduler;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FilterTest {

    @Test
    public void testConstructor() {
        Filter filter = new Filter();
        Assertions.assertTrue(filter.getValues().isEmpty());
    }

    @Test
    public void testAddValue() {
        Filter filter = new Filter();
        filter.addValue("COMP155");
        filter.addValue("MATH161");
        Assertions.assertEquals(Arrays.asList("COMP155", "MATH161"), filter.getValues());
    }
}
