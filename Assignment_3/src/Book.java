import java.time.LocalDate;

public class Book {
    private int bookId;
    private boolean canBookBorrowed;
    private boolean canBookRead;
    private String  typeOfBook;
    private LocalDate firstTime;
    private LocalDate lastTime;
    private int ownerOfTheBook;
    private boolean extendStatus = true; // if it is true, it means that the deadline may be extended.


    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public boolean isCanBookBorrowed() {
        return canBookBorrowed;
    }

    public void setCanBookBorrowed(boolean canBookBorrowed) {
        this.canBookBorrowed = canBookBorrowed;
    }

    public LocalDate getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(LocalDate firstTime) {
        this.firstTime = firstTime;
    }

    public LocalDate getLastTime() {
        return lastTime;
    }

    public void setLastTime(LocalDate lastTime) {
        this.lastTime = lastTime;
    }

    public int getOwnerOfTheBook() {
        return ownerOfTheBook;
    }

    public void setOwnerOfTheBook(Integer integer) {
        this.ownerOfTheBook = integer;
    }

    public boolean isExtendStatus() {
        return extendStatus;
    }

    public void setExtendStatus(boolean extendStatus) {
        this.extendStatus = extendStatus;
    }

    public String getTypeOfBook() {
        return typeOfBook;
    }

    public void setTypeOfBook(String typeOfBook) {
        this.typeOfBook = typeOfBook;
    }

    public boolean isCanBookRead() {
        return canBookRead;
    }

    public void setCanBookRead(boolean canBookRead) {
        this.canBookRead = canBookRead;
    }
}
