public class Member {
    private int memberId;
    private int bookLimit;
    private int timeLimit;
    private final int readTimeLimit = 99999999;
    private int numberofBooksOwned;
    // ceza durumu , ceza günü


    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
    public int getBookLimit() {
        return bookLimit;
    }

    public void setBookLimit(int bookLimit) {
        this.bookLimit = bookLimit;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getNumberofBooksOwned() {
        return numberofBooksOwned;
    }

    public void setNumberofBooksOwned(int numberofBooksOwned) {
        this.numberofBooksOwned += numberofBooksOwned;
    }

    public int getReadTimeLimit() {
        return readTimeLimit;
    }
}
