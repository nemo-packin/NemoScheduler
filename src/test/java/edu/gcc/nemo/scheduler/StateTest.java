package edu.gcc.nemo.scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StateTest {

    @Test
    public void testInitialState() {
        State state = new State();
        Assertions.assertEquals(RouteName.SIGNED_OUT, state.getCurrent());
    }

    @Test
    public void testUpdate() {
        State state = new State();
        state.update(RouteName.AUTHENTICATE);
        Assertions.assertEquals(RouteName.AUTHENTICATE, state.getCurrent());
    }

    @Test
    public void testGoBack() {
        State state = new State();
        state.update(RouteName.HOME);
        state.update(RouteName.VIEW_SCHEDULE);
        state.goBack();
        Assertions.assertEquals(RouteName.HOME, state.getCurrent());
        state.goBack();
        Assertions.assertEquals(RouteName.SIGNED_OUT, state.getCurrent());
    }

    @Test
    public void testGoBackFromInitialState() {
        State state = new State();
        state.goBack();
        Assertions.assertEquals(RouteName.SIGNED_OUT, state.getCurrent());
    }
}