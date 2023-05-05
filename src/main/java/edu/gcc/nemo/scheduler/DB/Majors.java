package edu.gcc.nemo.scheduler.DB;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import edu.gcc.nemo.scheduler.CourseCode;
import edu.gcc.nemo.scheduler.CourseLike;
import edu.gcc.nemo.scheduler.CourseOptions;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class Majors {
    private static Majors instance = null;
    private List<CourseOptions> allMajors = null;
    public static Majors getInstance() {
        if(instance == null) {
            try {
                instance = new Majors();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        Majors m = getInstance();
        m.getMajorTitles().forEach(System.out::println);
        m.getMajorRequirements("Bachelor of Science Degree in Computer Science").options.forEach(x -> ((CourseOptions)x).options.forEach(y -> System.out.println(((CourseCode)y).code)));
    }
    private Majors() throws FileNotFoundException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CourseLike.class, new CourseLikeDeserializer())
                .create();
        Type courseOptionsListType = new TypeToken<List<CourseOptions>>(){}.getType();
        ClassLoader c = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = c.getResourceAsStream("co_22-23.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        allMajors = gson.fromJson(br, courseOptionsListType);
    }
    public List<String> getMajorTitles() {
        return allMajors.stream().map(cl -> cl.title).collect(Collectors.toList());
    }

    public CourseOptions getMajorRequirements(String title) {
        return allMajors.stream().filter(courseOptions -> courseOptions.title.equals(title)).findFirst().orElse(null);
    }
}

class CourseLikeDeserializer implements JsonDeserializer<CourseLike> {
    @Override
    public CourseLike deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        if(jsonObject.has("code")) {
            return context.deserialize(jsonObject, CourseCode.class);
        }
        return context.deserialize(jsonObject, CourseOptions.class);
    }
}