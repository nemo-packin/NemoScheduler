package edu.gcc.nemo.scheduler;

public enum CourseFieldNames {
    courseCode {
        @Override
        public String toString() {
            return "course_code";
        }
    },
    department,
    semester,
    time,
    day,
    prof,
    name,
    creditHours {
        @Override
        public String toString() {
            return "credit_hours";
        }
    },
    capacity
}
