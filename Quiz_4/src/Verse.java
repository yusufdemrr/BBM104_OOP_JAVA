public class Verse implements Comparable<Verse> {
    int id;
    String text;

    public Verse(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public String toString() {
        return id + "\t" + text;
    }

    public int compareTo(Verse other) {
        return Integer.compare(this.id, other.id);
    }

}