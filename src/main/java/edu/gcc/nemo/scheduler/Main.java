package edu.gcc.nemo.scheduler;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello nemo packers!");

        Schedule s = new Schedule("Spring 2023");
        s.addCourseToSchedule("COMP 342 A");
        s.addCourseToSchedule("COMP 435 B");
//        s.addCourseToSchedule("PSYC B");
        s.saveSchedule(s);
        String str = s.toString();
//        System.out.println(str);
        s.checkOverlap(s);

    }
    public static void dumb() {
        System.out.println("HI!");
    }

    public static void notDumb(){
        System.out.println("Hello");
    }
}
