public class Printed extends Book{
    Printed(int id){
        setBookId(id);
        setCanBookBorrowed(true);
        setTypeOfBook("Printed");
        setCanBookRead(true);
    }
}
