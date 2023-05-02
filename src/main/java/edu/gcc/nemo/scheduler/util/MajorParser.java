package edu.gcc.nemo.scheduler.util;


import com.google.gson.Gson;
import edu.gcc.nemo.scheduler.Course;
import edu.gcc.nemo.scheduler.CourseOptions;
import edu.gcc.nemo.scheduler.DB.Courses;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MajorParser {
    public static void main(String[] args) throws IOException {
        List<Degree> degreeList = parseBuleltin();
//        degreeList.forEach(System.out::println);
        List<CourseOptions> courseOptions = toCourseOptions(degreeList, false);
    }

    public static List<Degree> parseBuleltin() {
        ClassLoader c = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = c.getResourceAsStream("2022-23-Catalog.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String f = br.lines().collect(Collectors.joining("\n"));
        Pattern pageTitle = Pattern.compile("^\\f.*$", Pattern.MULTILINE);
        f = pageTitle.matcher(f).replaceAll("");
        Pattern degPattern = Pattern.compile("Course\\s+Requirements\\s+for\\s+(a\\s+)?(?<degtype>Bachelor\\s+of\\s+[\\w/\\-\\s+]+)—.*?(?<hours>\\d+)(?<details>.*?)(?=Courses\\s+that\\s+count\\s+in|FOUR-YEAR\\s+PLAN|Course\\s+Requirements\\s+for\\s+a)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Pattern sectionPattern = Pattern.compile("^(?<title>[\\w \\-/]+):? :?\\((?<hrs>\\d+)(?:-\\d+)? hours\\)(?<courses>.*?)(?:\\.|([\\w \\-/]+:? :?\\(\\d+(-\\d+)? hours\\)))", Pattern.MULTILINE | Pattern.DOTALL);
        Pattern coursesPattern = Pattern.compile("(?<dept>(?:[A-Z][a-z]+ ?)+)(?<codes>(?:\\d{3}(?:[^A-Za-z]|and|or)*))(?:;|\\.|and)", Pattern.DOTALL);
        Pattern codePattern = Pattern.compile("\\d{3}");
        Matcher degMatch = degPattern.matcher(f);
        List<Degree> degreeList = new ArrayList<>();
        List<String> sourceList = new ArrayList<>();
        while (degMatch.find()) {
            Degree curDegree = new Degree();
            curDegree.name = degMatch.group("degtype");
            curDegree.totalHours = Integer.parseInt(degMatch.group("hours"));
            curDegree.sections = new ArrayList<>();
            String courseDet = degMatch.group("details");
            Matcher sectionMatch = sectionPattern.matcher(courseDet);
            while (sectionMatch.find()) {
                DegSection section = new DegSection();
                section.name = sectionMatch.group("title");
                section.hours = Integer.parseInt(sectionMatch.group("hrs"));
                section.courses = new HashMap<>();
                String courses = sectionMatch.group("courses");
                Matcher courseMatch = coursesPattern.matcher(courses);
                while (courseMatch.find()) {
                    List<String> codesList = new ArrayList<>();
                    String codes = courseMatch.group("codes");
                    Matcher codeMatch = codePattern.matcher(codes);
                    while (codeMatch.find()) {
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
        return degreeList;
    }

    public static List<CourseOptions> toCourseOptions(List<Degree> degreeList, boolean interactive) {
        Courses s = Courses.getInstance();
        List<Course> all = s.getAllCourses();
        HashMap<String, String> courseCodes = parseDeptCodes();
        Scanner sc = new Scanner(System.in);
//        courseCodes.forEach((x, y) -> {
//            System.out.println(x + ": " + y);
//        });
        degreeList.stream().map(degree -> {
            int sec_hrs = degree.sections.stream().map(sec -> sec.hours).reduce((x, y) -> x + y).orElse(0);
            if (sec_hrs < degree.totalHours) {
                System.out.println("Degree " + degree.name + " has a mismatch in hours between total: " + degree.totalHours + " and section " +
                        sec_hrs);
                System.out.println(degree);
                System.out.println(degree.source);

            }

            return null;
        }).collect(Collectors.toList());
//        degreeList.stream().map(degree -> {
//            CourseOptions c = new CourseOptions(degree.name, degree.totalHours);
////            System.out.println(degree);
//            degree.sections.forEach(degSection -> {
//                CourseOptions c2 = new CourseOptions(degSection.name, degSection.hours);
//                if (degSection.courses.size() == 0) {
//                    System.out.println("No courses in section " + degSection.name);
//                }
//                degSection.courses.entrySet().stream().forEach(entry -> {
//                    String dept = entry.getKey().strip();
//                    List<String> codes = entry.getValue();
//                    String pfix =
//                    if (pfix != null) {
//                        codes.forEach(code -> {
//                            if(code.equals("**")) {
//
//                            } else if(code.charAt(code.length() - 1) == '-') {
//                                code = code.substring(0,code.length() - 2);
//
//                            } else {
//                                try{
//                                    int num = Integer.parseInt(code);
//                                    System.out.println(pfix + code);
//                                } catch(NumberFormatException n) {
//                                    System.out.println("Could not parse code as number");
//                                }
//                            }
//                        });
//                    } else {
////                        System.out.println("Could not find prefix for department: " + dept);
//                    }
//                });
//            });
//            return null;
//        }).collect(Collectors.toList());
        return null;
    }

    public static String getCourseCode(Map<String, String> courseCodes, String department) {
        return courseCodes.getOrDefault(department,
                courseCodes.getOrDefault(department.split(" ")[0],
                        courseCodes.getOrDefault(courseCodes.keySet().stream().filter(key -> key.split(" ")[0].equals(department.split(" ")[0])).findFirst().orElse(""),
                                courseCodes.getOrDefault(courseCodes.keySet().stream().filter(key -> key.substring(0, 4).equals(department.substring(0, 4))).findFirst().orElse(""), null))));
    }

    public static HashMap<String, String> parseDeptCodes() {
        HashMap<String, String> deptCourseCodes = new HashMap<>();
        ClassLoader c = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = c.getResourceAsStream("2022-23-Catalog.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String f = br.lines().collect(Collectors.joining("\n"));
        Pattern p = Pattern.compile("^((?:[A-Z][a-z]+ )+)\\(([A-Z]{4})\\)\\s*$", Pattern.MULTILINE);
        Matcher degMatch = p.matcher(f);
        while (degMatch.find()) {
            String department = degMatch.group(1);
            String code = degMatch.group(2);
            deptCourseCodes.put(department.strip(), code.strip());
        }
        deptCourseCodes.put("Political Science", "POLI");
        deptCourseCodes.put("Science", "SCIC");
        return deptCourseCodes;
    }

}
