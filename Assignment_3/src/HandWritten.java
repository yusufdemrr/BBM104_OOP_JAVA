public class HandWritten extends Book{
    HandWritten(int id){
        setBookId(id);
        setCanBookBorrowed(false);
        setTypeOfBook("HandWritten");
        setCanBookRead(true);
    }
}
