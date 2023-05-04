package edu.gcc.nemo.scheduler.util;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MinorParser {
    public static void main(String[] args) {
        ClassLoader c = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = c.getResourceAsStream("Minors 2022-23.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String f = br.lines().collect(Collectors.joining("\n"));
        String pattern = "(^[ +A-Z& and-]+)\\. ([0-9-]+) required [a-z ,:]+(.+?\\.+[^A-Z a-z ,.;:/]\\n)";
        Pattern r = Pattern.compile(pattern, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = r.matcher(f);

        while (m.find()) {
            String title = m.group(1);
            String hours = m.group(2);
            String courses = m.group(3).trim();
            System.out.println("Title: " + title);
            System.out.println("Hours: " + hours);
            System.out.println("Courses: " + courses);
            System.out.println();
        }
    }
}