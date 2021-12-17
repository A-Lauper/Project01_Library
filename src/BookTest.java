import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Title: BookTest.java
 * Abstract: Tests the Book Class.
 * Author: Arielle Lauper
 * Date: 3 - Nov - 2021
 * References: Class materials
 */

class BookTest {

  String isbn;
  String title;
  String subject;
  int pageCount;
  String author;
  LocalDate dueDate;
  Book book;
  Book constructorTester;

  @BeforeEach
  void setUp() {

    isbn = "isbn";
    title = "Title";
    subject = "Subject";
    pageCount = 100;
    author = "Author";
    dueDate = LocalDate.now();
    book = new Book(isbn, title, subject, pageCount, author, dueDate);
    constructorTester = null;
  }

  @AfterEach
  void tearDown() {
    constructorTester = null;
    isbn = null;
    title = null;
    subject = null;
    pageCount = 0;
    author = null;
    dueDate = null;
  }

  @Test
  void Book() {
    assertNull(constructorTester);
    constructorTester = new Book("","","",0,"", LocalDate.now());
    assertNotNull((constructorTester));
  }

  @Test
  void getIsbn() {
    assertEquals(isbn, book.getIsbn());
  }

  @Test
  void setIsbn() {
    String newIsbn = "New isbn";
    book.setIsbn("New isbn");
    assertNotEquals(isbn, book.getIsbn());
    assertEquals(newIsbn, book.getIsbn());
  }

  @Test
  void getTitle() {
    assertEquals(title, book.getTitle());
  }

  @Test
  void setTitle() {
    String newTitle = "New Title";
    book.setTitle("New Title");
    assertNotEquals(title, book.getTitle());
    assertEquals(newTitle, book.getTitle());
  }

  @Test
  void getSubject() {
    assertEquals(subject, book.getSubject());
  }

  @Test
  void setSubject() {
    String newSubject = "New Subject";
    book.setSubject(newSubject);
    assertNotEquals(subject, book.getSubject());
    assertEquals(newSubject, book.getSubject());
  }

  @Test
  void getPageCount() {
    assertEquals(pageCount, book.getPageCount());
  }

  @Test
  void setPageCount() {
    int newPageCount = 10;
    book.setPageCount(newPageCount);
    assertNotEquals(pageCount, book.getPageCount());
    assertEquals(newPageCount, book.getPageCount());
  }

  @Test
  void getAuthor() {
    assertEquals(author, book.getAuthor());
  }

  @Test
  void setAuthor() {
    String newAuthor = "New Author";
    book.setAuthor(newAuthor);
    assertNotEquals(author, book.getAuthor());
    assertEquals(newAuthor, book.getAuthor());
  }

  @Test
  void getDueDate() {
    assertEquals(dueDate, book.getDueDate());
  }

  @Test
  void setDueDate() {
    LocalDate newDueDate = LocalDate.of(1,1,1);
    book.setDueDate(newDueDate);
    assertNotEquals(dueDate, book.getDueDate());
    assertEquals(newDueDate, book.getDueDate());
  }

  @Test
  void testEquals() {
    Book book1 = new Book("New isbn", "New Title", "New Subject",
        10, "New Author", LocalDate.of(1,1,1));
    Book book2 = new Book("New isbn", "New Title", "New Subject",
        10, "New Author", LocalDate.of(1,1,1));
    assertNotEquals(book, book1);
    assertEquals(book1, book2);
  }

  @Test
  void testToString() {
    assertEquals("Title by Author ISBN: isbn", book.toString());
  }
}