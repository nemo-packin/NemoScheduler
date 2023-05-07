package edu.gcc.nemo.scheduler;
import edu.gcc.nemo.scheduler.util.MinorParser;
import org.junit.jupiter.api.Test;

public class MinorParserTest
{
    @Test
    void testRetreiveMinor(){
        String s = MinorParser.minorToString("ROBOTICS");
        System.out.println(s);
    }
}
