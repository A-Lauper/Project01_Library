import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Title: LibraryTest.java
 * Abstract: Tests the Library class
 * Author: Arielle Lauper
 * Date: 13 - Nov - 2021
 * References: Absolute Java 5th edition
 *             Text Blocks : https://docs.oracle.com/en/java/javase/15/text-blocks/index.html
 */

class LibraryTest {

  String name;
  Book book;
  Book book1;
  Shelf shelf;
  Reader reader;
  Library library;
  Library constructorTester;

  @BeforeEach
  void setUp() {
    name = "library name";
    book = new Book("42-w-87","Hitchhikers Guide To the Galaxy", "sci-fi",
        42, "Douglas Adams", LocalDate.of(1970,1, 1));
    book1 = new Book("1337","Headfirst Java", "education",
        42, "Grady Booch", LocalDate.of(1970,1, 1));
    shelf = new Shelf();
    shelf.setSubject("sci-fi");
    shelf.setShelfNumber(1);
    reader = new Reader(1, "Drew Clinkenbeard", "831-582-4007");
    library = new Library(name);
    constructorTester = null;
  }

  @AfterEach
  void tearDown() {
    name = null;
    book = null;
    shelf = null;
    reader = null;
    library = null;
    constructorTester = null;
  }

  @Test
  void Library() {
    assertNull(constructorTester);
    constructorTester = new Library(name);
    assertNotNull((constructorTester));
  }

  @Test
  void init() {
    // file to test with
    PrintWriter outputStream = null;
    try {
      outputStream = new PrintWriter(new FileOutputStream("initTester.txt"));
      outputStream.println("""
        5
        42-w-87,Hitchhikers Guide To the Galaxy,sci-fi,42,Douglas Adams,0000
        42-w-87,Hitchhikers Guide To the Galaxy,sci-fi,42,Douglas Adams,0000
        42-w-87,Hitchhikers Guide To the Galaxy,sci-fi,42,Douglas Adams,0000
        42-w-87,Hitchhikers Guide To the Galaxy,sci-fi,42,Douglas Adams,0000
        42-w-87,Hitchhikers Guide To the Galaxy,sci-fi,42,Douglas Adams,0000
        1
        1,sci-fi
        1
        1,Drew Clinkenbeard,831-582-4007,2,42-w-87,2020-10-12""");
      if (outputStream != null) {
        outputStream.close();
        outputStream = null;
      }
    } catch (FileNotFoundException e) {
      System.out.println("Could not find the file " + e);
    }

    assertNotEquals(book, library.getBookByISBN("42-w-87"));
    assertNotEquals(shelf, library.getShelf("sci-fi"));
    assertNotEquals(reader, library.getReaderByCard(1));
    library.init("initTester.txt");
    assertEquals(book, library.getBookByISBN("42-w-87"));
    assertEquals(shelf, library.getShelf("sci-fi"));
    assertEquals(reader, library.getReaderByCard(1));
  }

  @Test
  void addBook() {
    library.addShelf("sci-fi");
    assertNull(library.getBookByISBN("42-w-87"));
    assertEquals(Code.SUCCESS, library.addBook(book));
    assertNotNull(library.getBookByISBN("42-w-87"));

    assertNull(library.getBookByISBN("1337"));
    assertEquals(Code.SHELF_EXISTS_ERROR, library.addBook(book1));
    assertNotNull(library.getBookByISBN("1337"));
  }

  @Test
  void testReturnBookParamReaderBook() {
    library.addBook(book);
    library.addShelf(shelf);
    library.addReader(reader);

    // check out book
    assertFalse(reader.hasBook(book));
    library.checkOutBook(reader, book);
    assertTrue(reader.hasBook(book));

    // return book
    assertEquals(0, library.getShelf("sci-fi").getBookCount(book));
    assertEquals(1, reader.getBookCount());
    assertEquals(Code.SUCCESS, library.returnBook(reader, book));
    assertEquals(1, library.getShelf("sci-fi").getBookCount(book));
    assertEquals(0, reader.getBookCount());
    assertFalse(reader.hasBook(book));

    // return book reader doesn't have
    assertEquals(Code.READER_DOESNT_HAVE_BOOK_ERROR, library.returnBook(reader, book));

    // return book not in library
    reader.addBook(book1);
    assertEquals(1, reader.getBookCount());
    assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, library.returnBook(reader, book1));
    assertEquals(1, reader.getBookCount());

    // return book with no shelf
    library.addBook(book1);
    assertEquals(1, reader.getBookCount());
    assertEquals(Code.SHELF_EXISTS_ERROR, library.returnBook(reader, book1));
    assertEquals(1, reader.getBookCount());
  }

  @Test
  void testReturnBookParamBook() {
    library.addBook(book);
    library.addShelf(shelf);

    // return book
    assertEquals(1, library.getShelf("sci-fi").getBookCount(book));
    assertEquals(Code.SUCCESS, library.returnBook(book));
    assertEquals(2, library.getShelf("sci-fi").getBookCount(book));

    // return book with no shelf
    assertEquals(Code.SHELF_EXISTS_ERROR, library.returnBook(book1));
  }

  @Test
  void listBooks() {
    assertEquals(0, library.listBooks());
    library.addBook(book);
    assertEquals(1, library.listBooks());
  }

  @Test
  void checkOutBook() {
    assertEquals(Code.READER_NOT_IN_LIBRARY_ERROR, library.checkOutBook(reader, book));

    library.addReader(reader);

    assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, library.checkOutBook(reader, book));

    library.addBook(book);
    library.addBook(book);

    assertEquals(Code.SHELF_EXISTS_ERROR, library.checkOutBook(reader, book));

    library.addShelf(shelf);

    // check out book
    assertEquals(2, library.getShelf("sci-fi").getBookCount(book));
    assertEquals(Code.SUCCESS, library.checkOutBook(reader, book));
    assertEquals(1, library.getShelf("sci-fi").getBookCount(book));

    // attempt to check out same book
    assertEquals(1, library.getShelf("sci-fi").getBookCount(book));
    assertEquals(Code.BOOK_ALREADY_CHECKED_OUT_ERROR, library.checkOutBook(reader, book));
    assertEquals(1, library.getShelf("sci-fi").getBookCount(book));

    // max out reader lending limit
    reader.addBook(new Book("1", "1", "subject", 100, "1", LocalDate.now()));
    reader.addBook(new Book("2", "2", "subject", 100, "2", LocalDate.now()));
    reader.addBook(new Book("3", "3", "subject", 100, "3", LocalDate.now()));
    reader.addBook(new Book("4", "4", "subject", 100, "4", LocalDate.now()));

    library.addBook(book1);
    library.addShelf("education");

    // attempt to check out more books than allowed
    assertEquals(1, library.getShelf("education").getBookCount(book1));
    assertEquals(Code.BOOK_LIMIT_REACHED_ERROR, library.checkOutBook(reader, book1));
    assertEquals(1, library.getShelf("education").getBookCount(book1));
  }

  @Test
  void getBookByISBN() {
    assertNull(library.getBookByISBN("42-w-87"));
    library.addBook(book);
    assertNotNull(library.getBookByISBN("42-w-87"));
    assertEquals(book, library.getBookByISBN("42-w-87"));
  }

  @Test
  void listShelves() {
    library.addBook(book);
    library.addShelf(shelf);
    assertEquals(Code.SUCCESS, library.listShelves(true));
    assertEquals(Code.SUCCESS, library.listShelves(false));
  }

  @Test
  void testAddShelfParamString() {
    // add shelf
    assertNull(library.getShelf("sci-fi"));
    assertEquals(Code.SUCCESS, library.addShelf("sci-fi"));
    assertNotNull(library.getShelf("sci-fi"));
    assertEquals(shelf, library.getShelf("sci-fi"));

    //attempt to add duplicate shelf
    assertEquals(Code.SHELF_EXISTS_ERROR, library.addShelf("sci-fi"));
  }

  @Test
  void testAddShelfParamShelf() {
    // add shelf
    assertNull(library.getShelf("sci-fi"));
    assertEquals(Code.SUCCESS, library.addShelf(shelf));
    assertNotNull(library.getShelf("sci-fi"));
    assertEquals(shelf, library.getShelf("sci-fi"));

    //attempt to add duplicate shelf
    assertEquals(Code.SHELF_EXISTS_ERROR, library.addShelf(shelf));
  }

  @Test
  void testGetShelfParamInteger() {
    assertNull(library.getShelf(1));
    library.addShelf(shelf);
    assertNotNull(library.getShelf(1));
    assertEquals(shelf, library.getShelf(1));
  }

  @Test
  void testGetShelfParamString() {
    assertNull(library.getShelf("sci-fi"));
    library.addShelf(shelf);
    assertNotNull(library.getShelf("sci-fi"));
    assertEquals(shelf, library.getShelf("sci-fi"));
  }

  @Test
  void testListReadersNoParam() {
    assertEquals(0, library.listReaders());
    library.addReader(reader);
    assertEquals(1, library.listReaders());
  }

  @Test
  void testListReadersParamBoolean() {
    assertEquals(0, library.listReaders(true));
    assertEquals(0, library.listReaders(false));
    library.addReader(reader);
    assertEquals(1, library.listReaders(true));
    assertEquals(1, library.listReaders(false));
  }

  @Test
  void getReaderByCard() {
    assertNull(library.getReaderByCard(1));
    library.addReader(reader);
    assertNotNull(library.getReaderByCard(1));
    assertEquals(reader, library.getReaderByCard(1));
  }

  @Test
  void addReader() {
    // add reader
    assertNotEquals(reader, library.getReaderByCard(1));
    assertEquals(Code.SUCCESS, library.addReader(reader));
    assertEquals(reader, library.getReaderByCard(1));

    // attempt to add same reader
    assertEquals(Code.READER_ALREADY_EXISTS_ERROR, library.addReader(reader));

    // attempt to add reader with same card number
    assertEquals(Code.READER_CARD_NUMBER_ERROR, library.addReader(
        new Reader(1, "", "")));
  }

  @Test
  void removeReader() {
    // attempt to remove reader not on record
    assertEquals(Code.READER_NOT_IN_LIBRARY_ERROR, library.removeReader(reader));

    library.addBook(book);
    library.addShelf(shelf);
    library.addReader(reader);

    library.checkOutBook(reader, book);
    // attempt to remover reader with book checked out
    assertEquals(Code.READER_STILL_HAS_BOOKS_ERROR, library.removeReader(reader));

    library.returnBook(reader, book);
    // remove reader
    assertEquals(Code.SUCCESS, library.removeReader(reader));
  }

  @Test
  void convertInt() {
    int recordCountString = Library.convertInt("1", Code.BOOK_COUNT_ERROR);
    assertEquals(1, recordCountString);
    recordCountString = Library.convertInt("-1", Code.BOOK_COUNT_ERROR);
    assertEquals(Code.BOOK_COUNT_ERROR.getCode(), recordCountString);
    recordCountString = Library.convertInt("a", Code.BOOK_COUNT_ERROR);
    assertEquals(Code.BOOK_COUNT_ERROR.getCode(), recordCountString);

    recordCountString = Library.convertInt("100", Code.PAGE_COUNT_ERROR);
    assertEquals(100, recordCountString);
    recordCountString = Library.convertInt("-100", Code.PAGE_COUNT_ERROR);
    assertEquals(Code.PAGE_COUNT_ERROR.getCode(), recordCountString);
    recordCountString = Library.convertInt("a", Code.PAGE_COUNT_ERROR);
    assertEquals(Code.PAGE_COUNT_ERROR.getCode(), recordCountString);

    recordCountString = Library.convertInt("2000", Code.DATE_CONVERSION_ERROR);
    assertEquals(2000, recordCountString);
    recordCountString = Library.convertInt("-2000", Code.DATE_CONVERSION_ERROR);
    assertEquals(Code.DATE_CONVERSION_ERROR.getCode(), recordCountString);
    recordCountString = Library.convertInt("a", Code.DATE_CONVERSION_ERROR);
    assertEquals(Code.DATE_CONVERSION_ERROR.getCode(), recordCountString);

    recordCountString = Library.convertInt("1", Code.UNKNOWN_ERROR);
    assertEquals(1, recordCountString);
    recordCountString = Library.convertInt("-1", Code.UNKNOWN_ERROR);
    assertEquals(Code.UNKNOWN_ERROR.getCode(), recordCountString);
    recordCountString = Library.convertInt("a", Code.UNKNOWN_ERROR);
    assertEquals(Code.UNKNOWN_ERROR.getCode(), recordCountString);
  }

  @Test
  void convertDate() {
    // convert date
    LocalDate date = LocalDate.of(1982, 2,18);
    assertEquals(date, Library.convertDate("1982-02-18", Code.DATE_CONVERSION_ERROR));

    // default date
    date = LocalDate.of(1970, 1,1);
    assertEquals(date, Library.convertDate("0000", Code.DATE_CONVERSION_ERROR));
    assertEquals(date, Library.convertDate("1982-02", Code.DATE_CONVERSION_ERROR));
  }

  @Test
  void getLibraryCardNumber() {
    Reader reader1 = new Reader(Library.getLibraryCardNumber(), "Reader", "000-000-0000");
    assertEquals(1, reader1.getCardNumber());

    reader1.setCardNumber(Library.getLibraryCardNumber());
    assertEquals(2, reader1.getCardNumber());
  }
}