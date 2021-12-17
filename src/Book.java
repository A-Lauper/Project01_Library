import java.time.LocalDate;
import java.util.Objects;

/**
 * Title: Book.java
 * Abstract: Project 01 Part 01/04 - Holds all the information for a given book.
 * Author: Arielle Lauper
 * Date: 3 - Nov - 2021
 * References: Class materials
 */

public class Book {

  public static final int ISBN_ = 0;
  public  static final  int TITLE_ = 1;
  public static final int SUBJECT_ = 2;
  public  static final  int PAGE_COUNT_ = 3;
  public static final int AUTHOR_ = 4;
  public  static final  int DUE_DATE_ = 5;

  private String isbn;
  private String title;
  private String subject;
  private int pageCount;
  private String author;
  private LocalDate dueDate;

  public Book(String isbn, String title, String subject,
              int pageCount, String author, LocalDate dueDate) {
    this.isbn = isbn;
    this.title = title;
    this.subject = subject;
    this.pageCount = pageCount;
    this.author = author;
    this.dueDate = dueDate;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public int getPageCount() {
    return pageCount;
  }

  public void setPageCount(int pageCount) {
    this.pageCount = pageCount;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Book book = (Book) o;
    return getPageCount() == book.getPageCount() && Objects.equals(getIsbn(),
        book.getIsbn()) && Objects.equals(getTitle(),
        book.getTitle()) && Objects.equals(getSubject(),
        book.getSubject()) && Objects.equals(getAuthor(), book.getAuthor());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getIsbn(), getTitle(), getSubject(), getPageCount(), getAuthor());
  }

  /**
   * Overrides the toString to display the book's title, author and isbn.
   *
   * @return the custom string for a book object
   */
  @Override
  public String toString() {
    return this.title + " by " + this.author + " ISBN: " + this.isbn;
  }
}
