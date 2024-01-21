import java.util.EmptyStackException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        try {
            String input = args[0];
            String[] inputFile = FileInput.readFile(input,true,true);
            if (args.length != 1){
                FileOutput.writeToFile("output.txt","There should be only 1 paremeter",true,true);
                System.exit(1);
            }
            if (inputFile == null){
                System.exit(1);
            }
            if (inputFile.length == 0) {
                throw new EmptyStackException();
            }
            for(String line : inputFile){
                String[] modifiedLine = line.split("\t");
                Pattern pattern = Pattern.compile("[^a-zA-Z ]"); // pattern to match non-alphabetic and non-space characters
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    throw new IllegalArgumentException();
                }
                FileOutput.writeToFile("output.txt",line,true,true);
            }
        } catch(EmptyStackException e){
            FileOutput.writeToFile("output.txt","The input file should not be empty",true,true);
        } catch (IllegalArgumentException e){
            FileOutput.writeToFile("output.txt","The input file should not contains unexpected characters",true,true);
        }
    }
}