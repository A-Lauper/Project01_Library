import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Title: Reader.java
 * Abstract: Project 01 Part 02/04:
 *           Holds information on a reader and can add or remove books from their inventory.
 * Author: Arielle Lauper
 * Date: 3 - Nov - 2021
 * References: Class materials
 */

public class Reader {

  public static final int CARD_NUMBER_ = 0;
  public static final int NAME_ = 1;
  public static final int PHONE_ = 2;
  public static final int BOOK_COUNT_ = 3;
  public static final int BOOK_START_ = 4;

  private int cardNumber;
  private String name;
  private String phone;
  private List<Book> books;

  public Reader(int cardNumber, String name, String phone) {
    this.cardNumber = cardNumber;
    this.name = name;
    this.phone = phone;
    this.books = new ArrayList<>();
  }

  /**
   * Adds the book to the ArrayList books if not already present.
   * @param book the book to be added
   * @return SUCCESS if the book was added or return the corresponding error if not
   */
  public Code addBook(Book book) {
    if (hasBook(book)) {
      return Code.BOOK_ALREADY_CHECKED_OUT_ERROR;
    }
    this.books.add(book);
    return Code.SUCCESS;
  }

  /**
   * Removes the book from the ArrayList books if not already present.
   * @param book the book to be removed
   * @return SUCCESS if the book was removed or return the corresponding error if not
   */
  public Code removeBook(Book book) {
    if (!hasBook(book)) {
      return Code.READER_DOESNT_HAVE_BOOK_ERROR;
    }
    if (this.books.remove(book)) {
      this.books.remove(book);
      return Code.SUCCESS;
    }
    return Code.READER_COULD_NOT_REMOVE_BOOK_ERROR;
  }

  /**
   * Checks if the Reader has a particular book.
   * @param book the book being checked for
   * @return true if the book is in the ArrayList books, otherwise return false
   */
  public boolean hasBook(Book book) {
    return this.books.contains(book);
  }

  public int getBookCount(){
    return this.books.size();
  }

  public int getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(int cardNumber) {
    this.cardNumber = cardNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public List<Book> getBooks() {
    return this.books;
  }

  public void setBooks(List<Book> books) {
    this.books = books;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Reader reader = (Reader) o;
    return getCardNumber() == reader.getCardNumber() && Objects.equals(getName(), reader.getName()) && Objects.equals(getPhone(), reader.getPhone());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCardNumber(), getName(), getPhone());
  }

  @Override
  public String toString() {
    return this.name + " (#" + cardNumber + ") has checked out " + this.books;
  }
}