package edu.gcc.nemo.scheduler;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatusSheetTest {
    StatusSheet s = new StatusSheet(List.of(""), List.of(""),0);

    @Test
    void getGradYear() {
        assertEquals(0, s.getGradYear());
    }

    @Test
    void setGradYear() {
        s.setGradYear(2023);
        assertEquals( 2023, s.getGradYear());
    }
}