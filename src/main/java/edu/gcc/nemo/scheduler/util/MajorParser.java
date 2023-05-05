package edu.gcc.nemo.scheduler.util;


import com.google.gson.Gson;
import edu.gcc.nemo.scheduler.Course;
import edu.gcc.nemo.scheduler.CourseCode;
import edu.gcc.nemo.scheduler.CourseOptions;
import edu.gcc.nemo.scheduler.DB.Courses;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MajorParser {
    public static void main(String[] args) throws IOException {
        List<Degree> degreeList = parseBulletin();
//        degreeList.forEach(System.out::println);
        List<CourseOptions> courseOptions = toCourseOptions(degreeList, true);
        FileWriter fw = new FileWriter("co_22-23.json");
        Gson gson = new Gson();
        fw.write(gson.toJson(courseOptions));
        fw.close();
    }

    public static List<Degree> parseBulletin() {
        ClassLoader c = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = c.getResourceAsStream("2022-23-Catalog.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String f = br.lines().collect(Collectors.joining("\n"));
        Pattern pageTitle = Pattern.compile("^\\f.*$", Pattern.MULTILINE);
        f = pageTitle.matcher(f).replaceAll("");
        Pattern degPattern = Pattern.compile("Course\\s+Requirements\\s+for\\s+(a\\s+)?(?<degtype>Bachelor\\s+of\\s+[\\w/\\-\\s+]+)—.*?(?<hours>\\d+)(?<details>.*?)(?=Courses\\s+that\\s+count\\s+in|FOUR-YEAR\\s+PLAN|Course\\s+Requirements\\s+for\\s+a)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Pattern sectionPattern = Pattern.compile("^(?<title>[\\w \\-/&]+):? :?\\((?<hrs>\\d+)(?:-\\d+)? hours\\)(?<courses>.*?)(?:\\.|([\\w \\-/]+:? :?\\(\\d+(-\\d+)? hours\\)))", Pattern.MULTILINE | Pattern.DOTALL);
        Pattern coursesPattern = Pattern.compile("(?<dept>(?:[A-Z][a-z]+ ?)+)(?<codes>(?:\\d{3}(?:[^A-Za-z]+|and|or)*))(?!;|\\.|and|[A-Z])", Pattern.DOTALL);
        Pattern codePattern = Pattern.compile("\\d{3}");
        Matcher degMatch = degPattern.matcher(f);
        List<Degree> degreeList = new ArrayList<>();
        List<String> sourceList = new ArrayList<>();
        while (degMatch.find()) {
            Degree curDegree = new Degree();
            curDegree.name = degMatch.group("degtype").replaceAll("\\s+", " ");
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

    public static List<CourseOptions> toCourseOptions(List<Degree> degreeList, boolean interactive) throws FileNotFoundException {
        Courses s = Courses.getInstance();
        List<Course> all = s.getAllCourses();
        Map<String, String> courseCodes = parseDeptCodes();
        Scanner sc = new Scanner(new File("instructions.txt"));
        return degreeList.stream().map(degree -> {
            int sec_hrs = degree.sections.stream().map(sec -> sec.hours).reduce((x, y) -> x + y).orElse(0);

            if (sec_hrs < degree.totalHours) {
//                System.out.println("Degree " + degree.name + " has a mismatch in hours between total: " + degree.totalHours + " and section " +
//                        sec_hrs);
//                System.out.println("Degree: "+ degree);
                boolean stop = !interactive;
                while (!stop) {
                    sec_hrs = degree.sections.stream().map(sec -> sec.hours).reduce((x, y) -> x + y).orElse(0);
//                    System.out.println(degree.source);
                    System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
                    System.out.println("Degree " + degree.name + " has a mismatch in hours between total: " + degree.totalHours + " and section " +
                            sec_hrs);
//                    System.out.println("Degree: " + degree);
                    System.out.println("add section(add_<sectionName>_<hours>)/fix total(fix)/ignore(ignore)");
                    String[] input = sc.nextLine().split("_");
                    if (input[0].equals("add")) {
                        DegSection n = new DegSection();
                        n.name = input[1];
                        n.hours = Integer.parseInt(input[2]);
                        n.courses = new HashMap<>();
                        degree.sections.add(n);
                    } else if (input[0].equals("fix")) {
                        degree.totalHours = sec_hrs;
                    } else if (input[0].equals("ignore")) {
                        stop = true;
                    } else {
                        System.out.println("Unrecognized input");
                    }
                    stop |= sec_hrs < degree.totalHours;
                }
//                System.out.println(degree);
//                System.out.println(degree.source);

            }
            CourseOptions c = new CourseOptions(degree.name, degree.totalHours);
            degree.sections.forEach(degSection -> {
                CourseOptions c2 = new CourseOptions(degSection.name, degSection.hours);
                c.options.add(c2);
                if (degSection.courses.size() == 0) {
                    boolean stop = !interactive;
                    System.out.println("=============================================================================");
                    System.out.println("No courses in degree/section " + degree.name + "/" + degSection.name);
//                    System.out.println(degree.source);
                    System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
                    while (!stop) {
//                        System.out.println(degree.source);
                        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
                        System.out.println("No courses in section " + degSection.name);
//                        System.out.println("Degree: " + degree);
                        System.out.println("course(<department(s)> <codes>/stop(stop)");
                        String input = sc.nextLine();
                        System.out.println("INPUT: " + input);
                        if (input.equals("stop")) {
                            stop = true;
                        } else {
                            input = input.replaceAll("(or ?)|(and ?)", "");
                            System.out.println("INPUT: " + input);
                            Pattern pppppp = Pattern.compile("(?<dept>(?:[A-Za-z][A-Za-z ]+ ?,? ?)+)(?<codes>(?:[0-9\\-\\*\\*]+,? ?)+)");
                            Matcher m = pppppp.matcher(input);
                            while (m.find()) {
                                String[] depts = m.group("dept").split(",");
                                String[] codes = m.group("codes").split(",");
                                for (String dept : depts) {
                                    dept = dept.strip();
                                    System.out.println("Department " + dept);
                                    if (degSection.courses.containsKey(dept)) {
                                        ArrayList<String> existingCodes = new ArrayList<>(degSection.courses.get(dept));
                                        existingCodes.addAll(List.of(codes));
                                        degSection.courses.put(dept, existingCodes);
                                    } else {
                                        degSection.courses.put(dept, List.of(codes));
                                    }
                                    degSection.courses.get(dept).forEach(System.out::println);
                                }

                            }
                        }
                    }
                }
                degSection.courses.entrySet().stream().forEach(entry -> {
                    String dept = entry.getKey().strip();
                    List<String> codes = entry.getValue();
                    String pfix = getCourseCode(courseCodes, dept);
                    if (pfix != null) {
                        codes.forEach(code -> {
                            code = code.strip();
                            if (code.equals("**")) {
                                getMatchingCourses(all, pfix).forEach(c2.options::add);
                                getMatchingCourses(all, pfix).forEach(x -> System.out.println("Added " + x.code));
                            } else if (code.length() < 3) {
                                return;
                            } else if (code.charAt(code.length() - 1) == '-') {
                                char firstChar = code.charAt(0);
                                String beginCode = pfix + firstChar;
                                getMatchingCourses(all, beginCode).forEach(c2.options::add);
                                getMatchingCourses(all, beginCode).forEach(x -> System.out.println("Added " + x.code));
                            } else {
                                try {
                                    int num = Integer.parseInt(code);
                                    getMatchingCourses(all, pfix + code).forEach(c2.options::add);
                                    getMatchingCourses(all, pfix + code).forEach(x -> System.out.println("Added " + x.code));
                                } catch (NumberFormatException n) {
                                    System.out.println("Could not parse code as number");
                                    System.out.println("Number was " + code);
                                }
                            }
                        });
                    } else {
                        System.out.println("Could not find prefix for department: " + dept);
                    }
                });
            });
            return c;
        }).collect(Collectors.toList());
    }

    public static String getCourseCode(Map<String, String> courseCodes, String department) {
        String finalDepartment = department.strip();
//        System.out.println("Getting course code for department: " + finalDepartment);
//        System.out.println(courseCodes.getOrDefault(finalDepartment,
//                courseCodes.getOrDefault(finalDepartment.split(" ")[0],
//                        courseCodes.getOrDefault(courseCodes.keySet().stream().filter(key -> key.split(" ")[0].equals(finalDepartment.split(" ")[0])).findFirst().orElse(""),
//                                courseCodes.getOrDefault(courseCodes.keySet().stream().filter(key -> key.substring(0, key.length() < 4 ? key.length() : 4).equals(finalDepartment.substring(0, finalDepartment.length() < 4 ? finalDepartment.length() : 4))).findFirst().orElse(""), null)))));
        return courseCodes.getOrDefault(finalDepartment,
                courseCodes.getOrDefault(finalDepartment.split(" ")[0],
                        courseCodes.getOrDefault(courseCodes.keySet().stream().filter(key -> key.split(" ")[0].equals(finalDepartment.split(" ")[0])).findFirst().orElse(""),
                                courseCodes.getOrDefault(courseCodes.keySet().stream().filter(key -> key.substring(0, key.length() < 4 ? key.length() : 4).equals(finalDepartment.substring(0, finalDepartment.length() < 4 ? finalDepartment.length() : 4))).findFirst().orElse(""), null))));
    }

    public static List<CourseCode> getMatchingCourses(List<Course> courses, String code) {
//        System.out.println("CODEEEEEE: " + code);
        return new ArrayList<>(courses.stream().filter(course -> course.getCourseCode().startsWith(code)).map(course -> new CourseCode(course.getCourseCode().substring(0, course.getCourseCode().length() - 1), course.getCreditHours())).collect(Collectors.toSet()));
    }

    public static Map<String, String> parseDeptCodes() {
        TreeMap<String, String> deptCourseCodes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        ClassLoader c = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = c.getResourceAsStream("2022-23-Catalog.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String f = br.lines().collect(Collectors.joining("\n"));
        Pattern p = Pattern.compile("^((?:[A-Z][a-z]+ )+)\\(([A-Z]{3,4})\\)\\s*$", Pattern.MULTILINE);
        Matcher degMatch = p.matcher(f);
        while (degMatch.find()) {
            String department = degMatch.group(1);
            String code = degMatch.group(2);
            deptCourseCodes.put(department.strip(), code.strip());
        }
        deptCourseCodes.put("Political Science", "POLS");
        deptCourseCodes.put("Science", "SCIC");
        deptCourseCodes.put("Art", "ART");
        return deptCourseCodes;
    }

}
