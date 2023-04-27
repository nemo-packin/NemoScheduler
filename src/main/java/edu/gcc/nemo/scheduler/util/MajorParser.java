package edu.gcc.nemo.scheduler.util;


import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MajorParser {
    public static void main(String[] args) throws IOException {
        ClassLoader c = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = c.getResourceAsStream("2022-23-Catalog.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String f = br.lines().collect(Collectors.joining("\n"));
        Pattern pageTitle = Pattern.compile("^\\f.*$", Pattern.MULTILINE);
        f = pageTitle.matcher(f).replaceAll("");
        Pattern degPattern = Pattern.compile("Course\\s+Requirements\\s+for\\s+(a\\s+)?(?<degtype>Bachelor\\s+of\\s+[\\w/\\-\\s+]+)—.*?(?<hours>\\d+)(?<details>.*?)(?=Courses\\s+that\\s+count\\s+in|FOUR-YEAR\\s+PLAN|Course\\s+Requirements\\s+for\\s+a)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Pattern sectionPattern = Pattern.compile("^(?<title>[\\w ]+) \\((?<hrs>\\d+) hours\\)(?<courses>.*?\\.)", Pattern.MULTILINE | Pattern.DOTALL);
        Pattern coursesPattern = Pattern.compile("(?<dept>(?:[A-Z][a-z]+ ?)+)(?<codes>(?:\\d{3}(?:[^A-Za-z]|and|or)*))(?:;|\\.|and)", Pattern.DOTALL);
        Pattern codePattern = Pattern.compile("\\d{3}");
        Matcher degMatch = degPattern.matcher(f);
        List<Degree> degreeList = new ArrayList<>();
        List<String> sourceList = new ArrayList<>();
        while(degMatch.find()){
            Degree curDegree = new Degree();
            curDegree.name = degMatch.group("degtype");
            curDegree.totalHours = Integer.parseInt(degMatch.group("hours"));
            curDegree.sections = new ArrayList<>();
            String courseDet = degMatch.group("details");
            Matcher sectionMatch = sectionPattern.matcher(courseDet);
            while(sectionMatch.find()) {
                DegSection section = new DegSection();
                section.name = sectionMatch.group("title");
                section.hours = Integer.parseInt(sectionMatch.group("hrs"));
                section.courses = new HashMap<>();
                String courses = sectionMatch.group("courses");
                Matcher courseMatch = coursesPattern.matcher(courses);
                while(courseMatch.find()) {
                    List<String> codesList = new ArrayList<>();
                    String codes = courseMatch.group("codes");
                    Matcher codeMatch = codePattern.matcher(codes);
                    while(codeMatch.find()) {
                        codesList.add(codeMatch.group());
                    }
                    section.courses.put(courseMatch.group("dept"), codesList);
                }
                curDegree.sections.add(section);
            }
            curDegree.source = courseDet;
            degreeList.add(curDegree);
        }
//        System.out.println(degreeList.size());
        Gson gson = new Gson();
        System.out.println(gson.toJson(degreeList));
    }

}
