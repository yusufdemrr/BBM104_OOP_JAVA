import java.util.Comparator;

class IdComparator implements Comparator<Verse> {
    public int compare(Verse v1, Verse v2) {
        return Integer.compare(v1.id, v2.id);
    }
}