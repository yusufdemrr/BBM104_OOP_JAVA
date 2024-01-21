import java.util.Arrays;

public class Main {
    public static String output;
    public static String input;
    public static void main(String[] args) {
        SmartHomeSystem home = new SmartHomeSystem();
        input = args[0];
        output = args[1];
        home.commandReader();


    }


}