package edu.gcc.nemo.scheduler.util;

import com.google.gson.Gson;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MinorParser
{
    public static void main(String[] args) {
        ClassLoader c = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = c.getResourceAsStream("Minors 2022-23.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String f = br.lines().collect(Collectors.joining("\n"));
        System.out.println(f);
        Pattern minorTitle = Pattern.compile("\\b[A-Z & and -]+\\.", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        System.out.println(minorTitle);


        String regex = "(\\p{Upper}[\\p{Upper}\\p{Lower}\\s&]+)\\.\\s+(\\d+\\s+required\\s+hours,\\s+including\\s+.+?\\.)";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);


        // Create a Matcher object
        Matcher matcher = pattern.matcher(regex);

        // Loop through the matches and print the minor name and course requirements
        while (matcher.find()) {
            String minorName = matcher.group(1);
            String courseRequirements = matcher.group(2);
            System.out.println("Minor Name: " + minorName);
            System.out.println("Course Requirements: " + courseRequirements);
        }
    }
}
