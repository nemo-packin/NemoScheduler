package edu.gcc.nemo.scheduler;

import java.util.List;

public enum StudentFieldNames {
    id,
    gradYear {
        @Override
        public String toString() {
            return "grad_year";
        }
    },
    major,
    minor
}
