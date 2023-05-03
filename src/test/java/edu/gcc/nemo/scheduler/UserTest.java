package edu.gcc.nemo.scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testCheckPassword() {
        User user = new Admin("testuser", "testpass", "Test User", 12345);
        Assertions.assertTrue(user.checkPassword("testpass"));
        Assertions.assertFalse(user.checkPassword("wrongpass"));
    }

    @Test
    public void testGetId() {
        User user = new Admin("testuser", "testpass", "Test User", 5988);
        Assertions.assertEquals(5988, user.getId());
    }

    @Test
    public void testGetPassword() {
        User user = new Admin("testuser", "testpass", "Test User", -345);
        Assertions.assertEquals("testpass", user.getPassword());
    }
}