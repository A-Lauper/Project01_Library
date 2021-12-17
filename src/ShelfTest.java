import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Title: ShelfTest.java
 * Abstract: Tests the Shelf Class.
 * Author: Arielle Lauper
 * Date: 4 - Nov - 2021
 * References: Class materials,
 *             Text Blocks : https://docs.oracle.com/en/java/javase/15/text-blocks/index.html
 */

class ShelfTest {

  int number;
  String subject;
  HashMap<Book, Integer> books;
  Shelf shelf;
  Shelf constructorTester;

  @BeforeEach
  void setUp() {
    number = 0;
    subject = "";
    books = new HashMap<>();
    shelf = new Shelf();
    constructorTester =null;
  }

  @AfterEach
  void tearDown() {
    number = 0;
    subject = null;
    books = null;
    shelf = null;
    constructorTester = null;
  }

  @Test
  void Shelf() {
    assertNull(constructorTester);
    constructorTester = new Shelf();
    assertNotNull((constructorTester));
  }

  @Test
  void getShelfNumber() {
    assertEquals(number, shelf.getShelfNumber());
  }

  @Test
  void setShelfNumber() {
    int newShelfNumber = 1;
    shelf.setShelfNumber(newShelfNumber);
    assertNotEquals(number, shelf.getShelfNumber());
    assertEquals(newShelfNumber, shelf.getShelfNumber());
  }

  @Test
  void getSubject() {
    assertEquals(subject, shelf.getSubject());

  }

  @Test
  void setSubject() {
    String newSubject = "New Subject";
    shelf.setSubject(newSubject);
    assertNotEquals(subject, shelf.getSubject());
    assertEquals(newSubject, shelf.getSubject());
  }

  @Test
  void getBooks() {
    assertEquals(books, shelf.getBooks());
  }

  @Test
  void setBooks() {
    HashMap<Book, Integer> newBooks = new HashMap<>();
    Book book = new Book("", "", "", 0, "", LocalDate.now());
    newBooks.put(book, 1);
    shelf.setBooks(newBooks);
    assertNotEquals(books, shelf.getBooks());
    assertEquals(newBooks, shelf.getBooks());
  }

  @Test
  void testEquals() {
    Shelf shelf1 = new Shelf();
    Shelf shelf2 = new Shelf();

    HashMap<Book, Integer> newBooks = new HashMap<>();
    Book book = new Book("", "", "", 0, "", LocalDate.now());
    newBooks.put(book, 1);
    shelf1.setBooks(newBooks);
    shelf2.setBooks(newBooks);
    shelf1.setShelfNumber(1);
    shelf2.setShelfNumber(1);
    shelf1.setSubject("subject");
    shelf2.setSubject("subject");

    assertNotEquals(shelf, shelf1);
    assertEquals(shelf1, shelf2);
  }

  @Test
  void testToString() {
    shelf.setShelfNumber(2);
    shelf.setSubject("education");
    assertEquals("2 : education", shelf.toString());
  }

  @Test
  void getBookCount() {
    Random random = new Random();
    int numBooks = random.nextInt(20) + 1;
    Book book = new Book("", "", "", 0, "", LocalDate.now());
    for (int i = 0; i < numBooks; i++) {
      shelf.addBook(book);
    }
    assertEquals(Code.SUCCESS, shelf.removeBook(book));
    assertEquals(numBooks - 1, shelf.getBookCount(book));
    for (int i = numBooks - 2; i >= 0; i--) {
      assertEquals(Code.SUCCESS, shelf.removeBook(book));
      assertEquals(i, shelf.getBookCount(book));
    }
    assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, shelf.removeBook(book));
    assertEquals(0, shelf.getBookCount(book));

    Book book1 = new Book("", "", "", 1, "", LocalDate.now());

    assertEquals(-1, shelf.getBookCount(book1));
  }

  @Test
  void addBook() {
    Book book = new Book("", "", "", 0, "", LocalDate.now());
    assertEquals(Code.SUCCESS, shelf.addBook(book));
    assertEquals(1, shelf.getBookCount(book));
    assertEquals(Code.SUCCESS, shelf.addBook(book));
    assertEquals(2, shelf.getBookCount(book));
    Book book1 = new Book("", "", "_", 0, "", LocalDate.now());
    assertEquals(Code.SHELF_SUBJECT_MISMATCH_ERROR, shelf.addBook(book1));
    assertEquals(2, shelf.getBookCount(book));
  }

  @Test
  void removeBook() {
    Book book = new Book("", "", "", 0, "", LocalDate.now());
    assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, shelf.removeBook(book));
    assertEquals(Code.SUCCESS, shelf.addBook(book));
    assertEquals(1, shelf.getBookCount(book));
    assertEquals(Code.SUCCESS, shelf.removeBook(book));
    assertEquals(0, shelf.getBookCount(book));
    assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, shelf.removeBook(book));
    assertEquals(0, shelf.getBookCount(book));
  }

  @Test
  void listBooks() {
    shelf.setSubject("Adventure");
    Book book = new Book("123_456_789","The Book Title", "Adventure",
        1, "The Author", LocalDate.now());

    shelf.addBook(book);

    assertEquals(
        "1 book on shelf: 0 : Adventure\n" +
            "The Book Title by The Author ISBN: 123_456_789 1\n",
        shelf.listBooks());

    shelf.addBook(book);

    assertEquals(
        "2 books on shelf: 0 : Adventure\n" +
            "The Book Title by The Author ISBN: 123_456_789 2\n"
        , shelf.listBooks());
  }
}