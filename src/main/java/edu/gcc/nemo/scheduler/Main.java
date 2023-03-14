package edu.gcc.nemo.scheduler;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello nemo packers!");
        Schedule s = new Schedule("Spring");
        s.addCourse("COMP 342 A");
        System.out.println(s.toString());

    }
    public static void dumb() {
        System.out.println("HI!");
    }

    public static void notDumb(){
        System.out.println("Hello");
    }
}
