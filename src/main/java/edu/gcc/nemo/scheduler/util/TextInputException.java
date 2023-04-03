package edu.gcc.nemo.scheduler.util;

public class TextInputException extends Exception{
    public TextInputException() {
        super("Unrecognized input");
    }
    public TextInputException(String s) {
        super(s);
    }
}
