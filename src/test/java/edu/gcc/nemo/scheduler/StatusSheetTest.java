package edu.gcc.nemo.scheduler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusSheetTest {
    StatusSheet s = new StatusSheet();

    @Test
    void getGradYear() {
        assertEquals(0, s.getGradYear());
    }

    @Test
    void setGradYear() {
        s.setGradYear(2023);
        assertEquals( 2023, s.getGradYear());
    }

    @Test
    public void testGetSetGradYear() {
        StatusSheet sheet = new StatusSheet();
        sheet.setGradYear(2023);
        assertEquals(2023, sheet.getGradYear());
    }

    @Test
    public void testSerializeEmptySheet() {
        StatusSheet sheet = new StatusSheet();
        assertEquals(null, sheet.serialize());
    }
}