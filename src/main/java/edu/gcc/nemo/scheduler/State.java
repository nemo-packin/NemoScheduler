package edu.gcc.nemo.scheduler;

import java.util.ArrayList;
import java.util.List;

public class State {
    private RouteName current = RouteName.SIGNED_OUT;
    private List<RouteName> history = new ArrayList<>();
    public void update(RouteName newState) {
        history.add(current);
        current = newState;
    }
    public void goBack() {
        current = history.remove(history.size() - 1);
    }
    public RouteName getCurrent() {
        return current;
    }
}
