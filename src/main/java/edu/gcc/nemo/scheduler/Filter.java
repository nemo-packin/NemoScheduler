package edu.gcc.nemo.scheduler;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    protected List<String> values;
    public FilterMatchType matchType = FilterMatchType.EQUALS;
    public Filter() {
        this.values = new ArrayList<>();
    }

    public List<String> getValues() {
        return values;
    }

    public void addValue(String value) {
        this.values.add(value);
    }
}
