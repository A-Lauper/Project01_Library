import java.util.HashMap;
import java.util.Objects;

/**
 * Title: Shelf.java
 * Abstract: Project 01 Part 03/04:
 *           Holds information on a shelf and can add or remove books from it.
 * Author: Arielle Lauper
 * Date: 4 - Nov - 2021
 * References: Class materials
 *             HashMap Methods: https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
 */

public class Shelf {
  public static final int SHELF_NUMBER = 0;
  public static final int SUBJECT_ = 1;

  private int shelfNumber;
  private String subject;
  private HashMap<Book, Integer> books;

  public Shelf() {
    this.shelfNumber = 0;
    this.subject = "";
    this.books = new HashMap<>();
  }

  public int getShelfNumber() {
    return shelfNumber;
  }

  public void setShelfNumber(int shelfNumber) {
    this.shelfNumber = shelfNumber;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public HashMap<Book, Integer> getBooks() {
    return this.books;
  }

  public void setBooks(HashMap<Book, Integer> books) {
    this.books = books;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Shelf shelf = (Shelf) o;
    return getShelfNumber() == shelf.getShelfNumber() && Objects.equals(getSubject(), shelf.getSubject());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getShelfNumber(), getSubject());
  }

  @Override
  public String toString() {
    return this.shelfNumber + " : " + this.subject;
  }

  /**
   * Returns the number of a particular book on the shelf.
   * @param book book to be counted
   * @return count of given book in HashMap books or return -1 if not found
   */
  public int getBookCount(Book book) {
    if (this.books.containsKey(book)) {
      return this.books.get(book);
    }
    return -1;    // if book is not on shelf
  }

  /**
   * Adds the given book to the shelf
   * @param book book to be added
   * @return SUCCESS if the book was added or return the corresponding error if not
   */
  public Code addBook(Book book) {
    if (this.books.containsKey(book)) {
      this.books.replace(book, this.books.get(book) + 1);
      System.out.println(book + " added to shelf " + this);
      return Code.SUCCESS;
    } else if (this.subject.equals(book.getSubject())) {
      books.put(book, 1);
      System.out.println(book + " added to shelf " + this);
      return Code.SUCCESS;
    }
    return Code.SHELF_SUBJECT_MISMATCH_ERROR;
  }

  /**
   * Removes the given book to the shelf
   * @param book book to be removed
   * @return SUCCESS if the book was removed or return the corresponding error if not
   */
  public Code removeBook(Book book) {
    if (!this.books.containsKey(book)) {
      System.out.println(book.getTitle() + " is not on shelf " + this.subject);
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    } else if (this.books.get(book).equals(0)) {
      System.out.println("No copies of " + book.getTitle() + " remain on shelf " + this.subject);
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    }
    this.books.replace(book, this.books.get(book) - 1);
    System.out.println(book.getTitle() + " successfully removed from shelf " + this.subject);
    return Code.SUCCESS;
  }

  /**
   * Returns the number of books and the shelf and each of their listings
   * @return a String of the shelf and all books on the shelf
   */
  public String listBooks() {
    int totalBooks = 0;
    StringBuilder bookListings = new StringBuilder();
    for (Book book : books.keySet()) {
      totalBooks += this.getBookCount(book);
      bookListings.append(book).append(" ").append(this.getBookCount(book)).append("\n");
    }
    if (totalBooks == 1) { // not plural if one book
      return totalBooks + " book on shelf: " + this + "\n" + bookListings;
    }
    return totalBooks + " books on shelf: " + this + "\n" + bookListings;
  }
}
