import java.util.*;

public class Main {
    public static void main(String[] args) {
        String input = args[0];

        String[] inputFile = FileInput.readFile(input, true, true);
        List<Verse> verses = new ArrayList<>();
        for(String line : inputFile) {
            String[] modifiedLine = line.split("\t");
            int id = Integer.parseInt(modifiedLine[0]);
            verses.add(new Verse(id, modifiedLine[1]));
        }

        // poemArrayList.txt
        FileOutput.writeToFile("poemArrayList.txt","",false,false);
        for (Verse line : verses){
            FileOutput.writeToFile("poemArrayList.txt", String.valueOf(line),true,true);
        }

        // poemArrayListOrderByID.txt
        Collections.sort(verses, new IdComparator());
        FileOutput.writeToFile("poemArrayListOrderByID.txt","",false,false);
        for (Verse line : verses){
            FileOutput.writeToFile("poemArrayListOrderByID.txt", String.valueOf(line),true,true);
        }

        //poemHashMap.txt
        Map<Integer, String> verseMap = new HashMap<>();
        FileOutput.writeToFile("poemHashMap.txt","",false,false);
        for (Verse verse : verses) {
            verseMap.put(verse.id, verse.text);
        }
        for (int idKey : verseMap.keySet()) {
            String line = idKey + "\t" + verseMap.get(idKey);
            FileOutput.writeToFile("poemHashMap.txt",line,true,true);
        }


        //poemHashSet.txt
        Set<Verse> verseSet = new HashSet<>(verses);
        FileOutput.writeToFile("poemHashSet.txt","",false,false);
        for (Verse verse : verseSet) {
            FileOutput.writeToFile("poemHashSet.txt", String.valueOf(verse),true,true);
        }

        //poemTreeSet.txt
        Set<Verse> treeSet = new TreeSet<>(verses);
        FileOutput.writeToFile("poemTreeSet.txt","",false,false);
        for (Verse verse : treeSet) {
            String line = verse.id + " " + verse.text;
            FileOutput.writeToFile("poemTreeSet.txt",line,true,true);
        }

        //poemTreeSetOrderByID.txt
        Set<Verse> treeSetById = new TreeSet<>(new IdComparator());
        FileOutput.writeToFile("poemTreeSetOrderByID.txt","",false,false);
        treeSetById.addAll(verses);
        for (Verse verse : treeSetById) {
            FileOutput.writeToFile("poemTreeSetOrderByID.txt", String.valueOf(verse),true,true);
        }

    }
}

// Yusuf Demir b2210356074