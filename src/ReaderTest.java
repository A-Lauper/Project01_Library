import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Title: ReaderTest.java
 * Abstract: Tests the Reader Class.
 * Author: Arielle Lauper
 * Date: 4 - Nov - 2021
 * References: Class materials
 */

class ReaderTest {

  int cardNumber;
  String name;
  String phone;
  Reader reader;
  Reader constructorTester;

  @BeforeEach
  void setUp() {
    cardNumber = 1;
    name = "Name";
    phone = "000 000 0000";
    reader = new Reader(cardNumber, name, phone);
    constructorTester = null;
  }

  @AfterEach
  void tearDown() {
    cardNumber = 0;
    name = null;
    phone = null;
    reader = null;
    constructorTester = null;
  }

  @Test
  void Reader() {
    assertNull(constructorTester);
    constructorTester = new Reader(0 , "", "");
    assertNotNull((constructorTester));
  }

  @Test
  void addBook() {
    Book book = new Book("", "", "", 0, "", LocalDate.now());
    assertEquals(Code.SUCCESS, reader.addBook(book));
    assertNotEquals(Code.SUCCESS, reader.addBook(book));
    assertEquals(Code.BOOK_ALREADY_CHECKED_OUT_ERROR, reader.addBook(book));
  }

  @Test
  void removerBook() {
    Book book = new Book("", "", "", 0, "", LocalDate.now());
    assertEquals(Code.READER_DOESNT_HAVE_BOOK_ERROR, reader.removeBook(book));
    reader.addBook(book);
    assertEquals(Code.SUCCESS, reader.removeBook(book));
  }

  @Test
  void hasBook() {
    Book book = new Book("", "", "", 0, "", LocalDate.now());
    assertFalse(reader.hasBook(book));
    reader.addBook(book);
    assertTrue(reader.hasBook(book));
  }

  @Test
  void getBookCount() {
    Book book = new Book("", "", "", 0, "", LocalDate.now());
    assertEquals(0, reader.getBookCount());
    reader.addBook(book);
    assertEquals(1, reader.getBookCount());
    reader.removeBook(book);
    assertEquals(0, reader.getBookCount());
  }

  @Test
  void getCardNumber() {
    assertEquals(cardNumber, reader.getCardNumber());
  }

  @Test
  void setCardNumber() {
    int newCardNumber = 2;
    reader.setCardNumber(newCardNumber);
    assertNotEquals(cardNumber, reader.getCardNumber());
    assertEquals(newCardNumber, reader.getCardNumber());
  }

  @Test
  void getName() {
    assertEquals(name, reader.getName());
  }

  @Test
  void setName() {
    String newName = "New Name";
    reader.setName(newName);
    assertNotEquals(name, reader.getName());
    assertEquals(newName, reader.getName());
  }

  @Test
  void getPhone() {
    assertEquals(phone, reader.getPhone());
  }

  @Test
  void setPhone() {
    String newPhone = "New Phone";
    reader.setPhone(newPhone);
    assertNotEquals(phone, reader.getPhone());
    assertEquals(newPhone, reader.getPhone());
  }

  @Test
  void getBooks() {
    List<Book> books = new ArrayList<>(); // expected value upon instantiation
    assertEquals(books, reader.getBooks());
  }

  @Test
  void setBooks() {
    List<Book> books = new ArrayList<>();
    books.add(new Book("", "", "", 0, "", LocalDate.now()));
    assertNotEquals(books, reader.getBooks());
    reader.setBooks(books);
    assertEquals(books, reader.getBooks());
  }

  @Test
  void testEquals() {
    Reader reader1 = new Reader(1, "New Name", "New Phone");
    Reader reader2 = new Reader(1, "New Name", "New Phone");
    assertNotEquals(reader, reader1);
    assertEquals(reader1, reader2);
  }

  @Test
  void testToString() {
    Book book = new Book("123_456_789","The Book Title", "Adventure",
        1, "The Author", LocalDate.now());
    reader.addBook(book);
    assertEquals("Name (#1) has checked out [The Book Title by The Author ISBN: 123_456_789]", reader.toString());
  }
}