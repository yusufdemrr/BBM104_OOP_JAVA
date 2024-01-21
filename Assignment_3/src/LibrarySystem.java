import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


public class LibrarySystem {

    public void commandReader() {
        String[] inputFile1 = FileInput.readFile(Main.inputFile, true, true);

        int memberID = 1;
        int bookID = 1;
        int numberofStudents = 0;
        int numberofAcademics = 0;
        int numberofPrintedbooks = 0;
        int numberofHandwrittenbooks = 0;

        LinkedHashMap<Integer, Member> members = new LinkedHashMap<Integer, Member>();
        LinkedHashMap<Integer, Book> books = new LinkedHashMap<Integer, Book>();
        LinkedHashMap<Integer, Book> borrowedBooks = new LinkedHashMap<Integer, Book>();
        LinkedHashMap<Integer, Book> readBooks = new LinkedHashMap<Integer, Book>();

        FileOutput.writeToFile(Main.outFile, "", false, false);
        assert inputFile1 != null;
        for (String line : inputFile1) {
            String[] modifiedLine = line.split("\t");
            switch (modifiedLine[0]) {
                case "addBook":
                    if (modifiedLine[1].equals("P")) {
                        Printed printed = new Printed(bookID);
                        books.put(bookID, printed);
                        FileOutput.writeToFile(Main.outFile, "Created new book: Printed [id: " + bookID + "]", true, true);
                        bookID++;
                        numberofPrintedbooks++;
                    } else if (modifiedLine[1].equals("H")) {
                        HandWritten handwritten = new HandWritten(bookID);
                        books.put(bookID, handwritten);
                        FileOutput.writeToFile(Main.outFile, "Created new book: Handwritten [id: " + bookID + "]", true, true);
                        bookID++;
                        numberofHandwrittenbooks++;
                    }
                    break;
                case "addMember":
                    if (modifiedLine[1].equals("S")) {
                        Student student = new Student(memberID);
                        members.put(memberID, student);
                        FileOutput.writeToFile(Main.outFile, "Created new member: Student [id: " + memberID + "]", true, true);
                        memberID++;
                        numberofStudents++;
                    } else if (modifiedLine[1].equals("A")) {
                        Academic academic = new Academic(memberID);
                        members.put(memberID, academic);
                        FileOutput.writeToFile(Main.outFile, "Created new member: Academic [id: " + memberID + "]", true, true);
                        memberID++;
                        numberofAcademics++;
                    }
                    break;
                case "borrowBook":
                    int IDofBook = Integer.parseInt(modifiedLine[1]);
                    int IDofMember = Integer.parseInt(modifiedLine[2]);
                    LocalDate time = LocalDate.parse(modifiedLine[3]);
                    Book book = books.get(IDofBook);
                    Member member = members.get(IDofMember);
                    if (!book.isCanBookBorrowed()) {
                        FileOutput.writeToFile(Main.outFile, "You cannot borrow this book!", true, true);
                    } else if (member.getBookLimit() == member.getNumberofBooksOwned()) {
                        FileOutput.writeToFile(Main.outFile, "You have exceeded the borrowing limit!", true, true);
                    } else {
                        book.setFirstTime(time);
                        book.setLastTime(time.plusDays(7));
                        book.setOwnerOfTheBook(IDofMember);
                        member.setNumberofBooksOwned(1);
                        book.setCanBookBorrowed(false);
                        book.setCanBookRead(false);
                        borrowedBooks.put(book.getBookId(), book);
                        FileOutput.writeToFile(Main.outFile, "The book ["+IDofBook+"] was borrowed by member ["+IDofMember+"] at "+modifiedLine[3], true, true);
                    }
                    break;
                case "readInLibrary":
                    int IDofBook2 = Integer.parseInt(modifiedLine[1]);
                    int IDofMember2 = Integer.parseInt(modifiedLine[2]);
                    LocalDate time2 = LocalDate.parse(modifiedLine[3]);
                    Book book2 = books.get(IDofBook2);
                    Member member2 = members.get(IDofMember2);
                    if(member2 instanceof Student && book2 instanceof HandWritten){
                        FileOutput.writeToFile(Main.outFile, "Students can not read handwritten books!", true, true);
                    } else if (!book2.isCanBookRead()){
                        FileOutput.writeToFile(Main.outFile, "You can not read this book!", true, true);
                    } else {
                        book2.setFirstTime(time2);
                        book2.setOwnerOfTheBook(IDofMember2);
                        book2.setLastTime(time2.plusDays(member2.getReadTimeLimit()));
                        book2.setCanBookBorrowed(false);
                        readBooks.put(book2.getBookId(), book2);
                        FileOutput.writeToFile(Main.outFile, "The book ["+IDofBook2+"] was read in library by member ["+IDofMember2+"] at "+modifiedLine[3], true, true);
                    }
                    break;
                case "returnBook":
                    int IDofBook3 = Integer.parseInt(modifiedLine[1]);
                    int IDofMember3 = Integer.parseInt(modifiedLine[2]);
                    LocalDate returntime = LocalDate.parse(modifiedLine[3]);
                    Book book3 = books.get(IDofBook3);
                    Member member3 = members.get(IDofMember3);
                    long daysbetween = ChronoUnit.DAYS.between(book3.getLastTime(), returntime);
                    book3.setOwnerOfTheBook(0);
                    book3.setCanBookBorrowed(true);
                    book3.setCanBookRead(true);
                    book3.setFirstTime(null);
                    book3.setExtendStatus(true);
                    member3.setNumberofBooksOwned(-1);
                    if(borrowedBooks.containsKey(book3.getBookId())){
                        borrowedBooks.remove(book3.getBookId());
                    } else {
                        readBooks.remove(book3.getBookId());
                    }
                    if(returntime.isAfter(book3.getLastTime())){
                        FileOutput.writeToFile(Main.outFile, "The book ["+IDofBook3+"] was returned by member ["+IDofMember3+"] at "+modifiedLine[3]+" Fee: "+daysbetween, true, true);
                    } else {
                        FileOutput.writeToFile(Main.outFile, "The book ["+IDofBook3+"] was returned by member ["+IDofMember3+"] at "+modifiedLine[3]+" Fee: 0", true, true);
                    }
                    break;
                case "extendBook":
                    int IDofBook4 = Integer.parseInt(modifiedLine[1]);
                    int IDofMember4 = Integer.parseInt(modifiedLine[2]);
                    LocalDate time4 = LocalDate.parse(modifiedLine[3]);
                    Book book4 = books.get(IDofBook4);
                    Member member4 = members.get(IDofMember4);
                    if(book4.isExtendStatus()) {
                        LocalDate newLastTime = book4.getLastTime().plusDays(member4.getTimeLimit());
                        book4.setLastTime(newLastTime);
                        book4.setExtendStatus(false);
                        FileOutput.writeToFile(Main.outFile, "The deadline of book ["+book4.getBookId()+"] was extended by member ["+member4.getMemberId()+"] at "+modifiedLine[3], true, true);
                        FileOutput.writeToFile(Main.outFile, "New deadline of book ["+book4.getBookId()+"] is "+book4.getLastTime(), true, true);
                    } else {
                        FileOutput.writeToFile(Main.outFile, "You cannot extend the deadline!", true, true);
                    }
                    break;
                case "getTheHistory":
                    FileOutput.writeToFile(Main.outFile, "History of library:", true, true);
                    FileOutput.writeToFile(Main.outFile, "", true, true);
                    FileOutput.writeToFile(Main.outFile, "Number of students: "+numberofStudents, true, true);
                    for (Map.Entry<Integer, Member> entry : members.entrySet()) {
                        Integer key = entry.getKey();
                        Member value = entry.getValue();
                        if(value.getBookLimit() == 2){
                            FileOutput.writeToFile(Main.outFile, "Student [id: "+key+"]", true, true);
                        }
                    }
                    FileOutput.writeToFile(Main.outFile, "", true, true);
                    FileOutput.writeToFile(Main.outFile, "Number of academics: "+numberofAcademics, true, true);
                    for (Map.Entry<Integer, Member> entry : members.entrySet()) {
                        Integer key = entry.getKey();
                        Member value = entry.getValue();
                        if(value.getBookLimit() == 4){
                            FileOutput.writeToFile(Main.outFile, "Academic [id: "+key+"]", true, true);
                        }
                    }
                    FileOutput.writeToFile(Main.outFile, "", true, true);
                    FileOutput.writeToFile(Main.outFile, "Number of printed books: "+numberofPrintedbooks, true, true);
                    for (Map.Entry<Integer, Book> entry : books.entrySet()) {
                        Integer key = entry.getKey();
                        Book value = entry.getValue();
                        if(value.getTypeOfBook().equals("Printed")){
                            FileOutput.writeToFile(Main.outFile, "Printed [id: "+key+"]", true, true);
                        }
                    }
                    FileOutput.writeToFile(Main.outFile, "", true, true);
                    FileOutput.writeToFile(Main.outFile, "Number of handwritten books: "+numberofHandwrittenbooks, true, true);
                    for (Map.Entry<Integer, Book> entry : books.entrySet()) {
                        Integer key = entry.getKey();
                        Book value = entry.getValue();
                        if(value.getTypeOfBook().equals("HandWritten")){
                            FileOutput.writeToFile(Main.outFile, "Handwritten [id: "+key+"]", true, true);
                        }
                    }
                    FileOutput.writeToFile(Main.outFile, "", true, true);
                    FileOutput.writeToFile(Main.outFile, "Number of borrowed books: "+borrowedBooks.size(), true, true);
                    for (Map.Entry<Integer, Book> entry : borrowedBooks.entrySet()) {
                        Integer key = entry.getKey();
                        Book value = entry.getValue();
                        FileOutput.writeToFile(Main.outFile, "The book ["+key+"] was borrowed by member ["+value.getOwnerOfTheBook()+"] at "+value.getFirstTime(), true, true);
                    }
                    FileOutput.writeToFile(Main.outFile, "", true, true);
                    FileOutput.writeToFile(Main.outFile, "Number of books read in library: "+readBooks.size(), true, true);
                    for (Map.Entry<Integer, Book> entry : readBooks.entrySet()) {
                        Integer key = entry.getKey();
                        Book value = entry.getValue();
                        FileOutput.writeToFile(Main.outFile, "The book ["+key+"] was read in library by member ["+value.getOwnerOfTheBook()+"] at "+value.getFirstTime(), true, true);
                    }
                    break;
            }
        }
    }
}


