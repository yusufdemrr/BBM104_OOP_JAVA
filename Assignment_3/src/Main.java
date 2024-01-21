import java.util.ArrayList;

public class Main {
    public static String inputFile;
    public static String outFile;
    public static void main(String[] args) {
        inputFile = args[0];
        outFile = args[1];
        LibrarySystem libS = new LibrarySystem();
        libS.commandReader();
    }
}