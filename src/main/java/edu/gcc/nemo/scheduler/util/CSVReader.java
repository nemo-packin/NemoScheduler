package edu.gcc.nemo.scheduler.util;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CSVReader {
    /***
     * CSVReader: Helpful little class to parse a CSV file. Reads the header row to get column names, then gives a map of
     *            <ColumnName: String, Value: String> for each row.
     *
     * Changelog:
     * Ken, 3/18: Initial implementation
     */
    public String[] keys;
    private BufferedReader csvReader;


    public CSVReader(String filename) throws IOException {
        if(!filename.matches(".*\\.csv")){
            throw new IllegalArgumentException("Filetype must be CSV");
        }

        csvReader = new BufferedReader(new FileReader(filename));
        String headerLine = csvReader.readLine();
        keys = headerLine.split(",");
    }

    public CSVReader(File file) throws IOException {
        if(!file.getName().matches(".*\\.csv")){
            throw new IllegalArgumentException("Filetype must be CSV");
        }

        csvReader = new BufferedReader(new FileReader(file));
        String headerLine = csvReader.readLine();
        keys = headerLine.split(",");
    }

    public CSVReader(InputStream inputStream) throws IOException {
        csvReader = new BufferedReader(new InputStreamReader(inputStream));
        String headerLine = csvReader.readLine();
        keys = headerLine.split(",");
    }

    public Map<String, String> nextRow() throws IOException {
        return makeMap(csvReader.readLine());
    }

    private Map<String, String> makeMap(String csvLine) {
        String[] vals = csvLine.split(",");
        return IntStream.range(0, keys.length).boxed().collect(Collectors.toMap(i -> keys[i], i ->  {
            try {
                return vals[i];
            } catch(IndexOutOfBoundsException e) {
                return " ";
            }
        }));
    }

    public Stream<Map<String, String>> rows() throws IOException {
        return csvReader.lines().map(this::makeMap);
    }
}
