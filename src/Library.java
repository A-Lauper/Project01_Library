import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Title: Library.java
 * Abstract: Project 01 Part 04/04:
 *           Holds information on a library and its functionality.
 * Author: Arielle Lauper
 * Date: 9 - Nov - 2021
 * References: https://stackoverflow.com/questions/5585779/how-do-i-convert-a-string-to-an-int-in-java
 *             https://www.baeldung.com/java-creating-localdate-with-values
 */

public class Library {

  public static final int LENDING_LIMIT = 5;

  private String name;
  private static int libraryCard = 0;
  private List<Reader> readers;
  private HashMap<String, Shelf> shelves;
  private HashMap<Book, Integer> books;

  /**
   * Constructs library object with given name.
   *
   * @param name name of the library to be constructed
   */
  public Library(String name) {
    this.name = name;
    this.readers = new ArrayList<>();
    this.shelves = new HashMap<>();
    this.books = new HashMap<>();
  }

  /**
   * Populates library with data from a CSV file
   *
   * @param filename name of file to be read
   * @return success or appropriate error code
   */
  public Code init(String filename) {
    File file = new File(filename);

    Scanner scan = null;

    try {
      scan = new Scanner(file);
    } catch (FileNotFoundException e) {
      System.out.println("could not find the file " + e);
      return Code.FILE_NOT_FOUND_ERROR;
    }

    // get number of books to be parsed
    int numToParse = 0;
    if (scan != null && scan.hasNext()) {
      numToParse = convertInt(scan.nextLine(), Code.BOOK_COUNT_ERROR);
    }

    if (numToParse < 0) { // if error when converting, return error
      return errorCode(numToParse);
    }

    if (scan != null && scan.hasNext()) {
      initBooks(numToParse, scan);
    }

    listBooks();

    // get number of shelves to be parsed
    if (scan != null && scan.hasNext()) {
      numToParse = convertInt(scan.nextLine(), Code.SHELF_COUNT_ERROR);
    }

    if (numToParse < 0) { // if error when converting, return error
      return errorCode(numToParse);
    }

    if (scan != null && scan.hasNext()) {
      initShelves(numToParse, scan);
    }

    listShelves(true);

    // get number of readers to be parsed
    if (scan != null && scan.hasNext()) {
      numToParse = convertInt(scan.nextLine(), Code.READER_COUNT_ERROR);
    }

    if (numToParse < 0) { // if error when converting, return error
      return errorCode(numToParse);
    }

    if (scan != null && scan.hasNext()) {
      initReader(numToParse, scan);
    }

    listReaders(true);

    return Code.SUCCESS;
  }

  /**
   * Parse all books from a CSV file and initialize them in the library
   *
   * @param bookCount number of books to be parsed
   * @param scan current position in CSV file
   * @return success or appropriate error code
   */
  private Code initBooks(int bookCount, Scanner scan) {
    if (bookCount < 1) {            // ensure there are books to parse
      return Code.LIBRARY_ERROR;
    }

    System.out.println("parsing " + bookCount + " books");
    String[] bookToParse;
    for (int i = 0; i < bookCount && scan != null && scan.hasNext(); i++) { // for each book
      bookToParse = scan.nextLine().split(","); // parse data to be processed

      // convert page count
      int pageCount = convertInt(bookToParse[Book.PAGE_COUNT_], Code.PAGE_COUNT_ERROR);
      if (pageCount < 0) {
        return Code.PAGE_COUNT_ERROR;
      }

      // convert due date
      LocalDate dueDate = convertDate(bookToParse[Book.DUE_DATE_], Code.DATE_CONVERSION_ERROR);
      if (dueDate == null) {
        return Code.DATE_CONVERSION_ERROR;
      }

      // make a new book with the data from the CSV file
      Book book = new Book(bookToParse[Book.ISBN_], bookToParse[Book.TITLE_],
          bookToParse[Book.SUBJECT_], pageCount,
          bookToParse[Book.AUTHOR_], dueDate);

      // add newly created book to library
      addBook(book);
    }
    return Code.SUCCESS;
  }

  /**
   * Parse all shelves from a CSV file and initialize them in the library
   *
   * @param shelfCount number of shelves to be parsed
   * @param scan current position in CSV file
   * @return success or appropriate error code
   */
  private Code initShelves(int shelfCount, Scanner scan) {
    if (shelfCount < 1) {             // ensure there are shelves to parse
      return Code.SHELF_COUNT_ERROR;
    }

    System.out.println("parsing " + shelfCount + " shelves");
    for (int i = 0; i < shelfCount && scan != null && scan.hasNext(); i++) { // for each shelf
      String[] shelfToParse = scan.nextLine().split(",");

      addShelf(shelfToParse[Shelf.SUBJECT_]); // add shelf from CSV by subject
    }
    if (shelves.size() != shelfCount) {
      System.out.println("Number of shelves doesn't match expected");
      return Code.SHELF_NUMBER_PARSE_ERROR;
    }
    return Code.SUCCESS;
  }

  /**
   * Parse all readers from a CSV file and initialize them in the library
   *
   * @param readerCount number of readers to be parsed
   * @param scan current position in CSV file
   * @return success or appropriate error code
   */
  private Code initReader(int readerCount, Scanner scan) {
    if (readerCount < 1) {             // ensure there are readers to parse
      return Code.READER_COUNT_ERROR;
    }

    System.out.println("parsing " + readerCount + " readers");
    for (int i = 0; i < readerCount && scan != null && scan.hasNext(); i++) { // for each reader
      String[] readerToParse = scan.nextLine().split(",");

      // convert reader's card number to int
      int cardNumber = convertInt(readerToParse[Reader.CARD_NUMBER_],
          Code.READER_CARD_NUMBER_ERROR);
      if (cardNumber < 0) {
        return Code.READER_CARD_NUMBER_ERROR;
      }

      // create new reader with data from CSV file
      Reader reader = new Reader(cardNumber, readerToParse[Reader.NAME_],
          readerToParse[Reader.PHONE_]);

      // add newly created reader to library
      addReader(reader);

      // add books to readers inventory
      for (int j = Reader.BOOK_START_; j < readerToParse.length; j++) {
        Book book = getBookByISBN(readerToParse[j]);
        j++; // move to book's date
        if (book == null) {
          System.out.println("ERROR");
          continue;
        }
        LocalDate dueDate = convertDate(readerToParse[j], Code.DATE_CONVERSION_ERROR);
        book.setDueDate(dueDate);
        checkOutBook(reader, book);
      }

    }
    return Code.SUCCESS;
  }

  /**
   * Add given book to library and appropriate shelf if possible
   *
   * @param newBook book to be added
   * @return success or appropriate error code
   */
  public Code addBook(Book newBook) {
    if (books.containsKey(newBook)) { // increment value in HashMap books if available...
      books.replace(newBook, books.get(newBook) + 1);
      System.out.println( books.get(newBook) + " copies of "
          + newBook.getTitle() + " in the stacks");
    } else {                          // otherwise, put newBook as a new key in HashMap books
      books.put(newBook, 1);
      System.out.println(newBook.getTitle() + " added to the stacks.");
    }
    // add newBook to appropriate shelf if available
    if (addBookToShelf(newBook, shelves.get(newBook.getSubject())) == Code.SUCCESS) {
      return Code.SUCCESS;
    }
    System.out.println("No shelf for " + newBook.getSubject() + " books");
    return Code.SHELF_EXISTS_ERROR;
  }

  /**
   * Take book from reader and return it to the library
   *
   * @param reader reader returning the book
   * @param book book to be returned
   * @return success or appropriate error code
   */
  public Code returnBook(Reader reader, Book book) {
    if (!reader.hasBook(book)) { // ensure the reader has the book
      System.out.println(reader.getName() + " doesn't have " + book.getTitle() + " checked out");
      return Code.READER_DOESNT_HAVE_BOOK_ERROR;
    }
    if (!books.containsKey(book)) {
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    }
    System.out.println(reader.getName() + " is returning " + book);
    Code code = reader.removeBook(book);
    if (code == Code.SUCCESS) {
      code = returnBook(book);
      if (code == Code.SUCCESS) {
        return code;
      }
      reader.addBook(book); // give book back to reader if unable to return
    }
    System.out.println("Could not return " + book);
    return code;
  }

  /**
   * Add given book to appropriate shelf
   *
   * @param book book to be returned
   * @return success or appropriate error code
   */
  public Code returnBook(Book book) {
    if (shelves.containsKey(book.getSubject())) { // add book to appropriate shelf if possible
      return shelves.get(book.getSubject()).addBook(book);
    }
    System.out.println("No shelf for " + book);
    return Code.SHELF_EXISTS_ERROR;
  }

  /**
   * Adds the given book to a shelf in the library.
   *
   * @param book book to be added to the shelf
   * @param shelf shelf that the book is being returned to
   * @return success or appropriate error code
   */
  private Code addBookToShelf(Book book, Shelf shelf) {
    if (returnBook(book).equals(Code.SUCCESS)) {
      return Code.SUCCESS;
    }
    if (shelf == null) {
      return Code.SHELF_EXISTS_ERROR;
    }
    if (!shelf.getSubject().equals(book.getSubject())) {
      return Code.SHELF_SUBJECT_MISMATCH_ERROR;
    }
    Code code = shelf.addBook(book);
    if (code.equals(Code.SUCCESS)) {
      System.out.println(book + " added to shelf");
      return Code.SUCCESS;
    }
    System.out.println("Could not add " + book + " to shelf");
    return code;
  }

  /**
   * Print all books the library owns to the console
   *
   * @return total number of books owned by the library
   */
  public int listBooks() {
    int bookCount = 0;
    for (Book book : books.keySet()) {
      System.out.println(books.get(book) + " copies of "
          + book.getTitle() + " by " + book.getAuthor() + " ISBN:" + book.getIsbn());
      bookCount += books.get(book);
    }
    return bookCount;
  }

  /**
   * Add given book to the reader and remove it from the shelf
   *
   * @param reader reader checking out the book
   * @param book book to be checked out
   * @return success or appropriate error code
   */
  public Code checkOutBook(Reader reader, Book book) {
    // ensure the reader is on record
    if (!readers.contains(reader)) {
      System.out.println(reader.getName() + " doesn't have an account here");
      return Code.READER_NOT_IN_LIBRARY_ERROR;
    }
    // ensure the reader has not exceeded the lending limit
    if (reader.getBookCount() >= LENDING_LIMIT) {
      System.out.println(reader.getName() +
          " has reached the lending limit, (" + LENDING_LIMIT + ")");
      return  Code.BOOK_LIMIT_REACHED_ERROR;
    }
    // ensure the library has the book
    if (!books.containsKey(book)) {
      System.out.println("ERROR: could not find " + book);
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    }
    // ensure there is a shelf for the book
    if (!shelves.containsKey(book.getSubject())) {
      System.out.println("no shelf for " + book.getSubject() + " books!");
      return Code.SHELF_EXISTS_ERROR;
    }
    // ensure the book is available on the shelf
    if (shelves.get(book.getSubject()).getBookCount(book) < 1) {
      System.out.println("ERROR: no copies of " + book + " remain");
      return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    }
    // add book to reader
    Code code = reader.addBook(book);
    if (code != Code.SUCCESS) {
      System.out.println("Couldn't checkout " + book);
      return code;
    }
    // remove book from shelf
    code = shelves.get(book.getSubject()).removeBook(book);
    if (code != Code.SUCCESS) {
      reader.removeBook(book); // take book back from reader if unable to remove from library
      System.out.println("Couldn't checkout " + book);
      return code;
    }
    System.out.println(book + " checked out successfully");
    return Code.SUCCESS;
  }

  /**
   * Find book with given ISBN
   *
   * @param isbn ISBN to be searched for
   * @return book with given ISBN or null if not present
   */
  public Book getBookByISBN(String isbn) {
    for (Book book : books.keySet()) {
      if (book.getIsbn().equals(isbn)) {
        return book;
      }
    }
    System.out.println("ERROR: Could not find a book with isbn: " + isbn);
    return null;
  }

  /**
   * Print all shelves at the library to the console
   *
   * @param showBooks should the books be listed?
   * @return success or appropriate error code
   */
  public Code listShelves(boolean showBooks) {
    if (showBooks) {
      for (Shelf shelf : shelves.values()) {
        System.out.println(shelf.listBooks());
      }
    } else {
      for (Shelf shelf : shelves.values()) {
        System.out.println(shelf.toString());
      }
    }
    return Code.SUCCESS;
  }

  /**
   * Add a shelf with the given subject to the library
   *
   * @param shelfSubject subject of the shelf to be added
   * @return success or appropriate error code
   */
  public Code addShelf(String shelfSubject) {
    if (shelves.containsKey(shelfSubject)) { // ensure shelf does not already exist
      System.out.println("ERROR: Shelf already exists " + shelves.get(shelfSubject));
      return Code.SHELF_EXISTS_ERROR;
    }
    Shelf shelf = new Shelf();
    shelf.setSubject(shelfSubject);
    return addShelf(shelf);
  }

  /**
   * Add a given shelf to the library
   *
   * @param shelf shelf to be added
   * @return success or appropriate error code
   */
  public Code addShelf(Shelf shelf) {
    if (shelves.containsValue(shelf)) { // ensure shelf does not already exist
      System.out.println("ERROR: Shelf already exists " + shelf);
      return Code.SHELF_EXISTS_ERROR;
    }

    // find next shelf number
    int shelfNumber = 0;
    for (Shelf s : shelves.values()) {
      if (s.getShelfNumber() > shelfNumber) {
        shelfNumber = s.getShelfNumber();
      }
    }
    shelf.setShelfNumber(shelfNumber + 1);

    // add shelf to library
    shelves.put(shelf.getSubject(), shelf);

    // Add all the books with matching subjects to the new shelf
    for (Book book : books.keySet()) {
      if (book.getSubject().equals(shelf.getSubject())) {
        for (int i = 0; i < books.get(book); i++) {
          shelf.addBook(book);
        }
      }
    }
    return Code.SUCCESS;
  }

  /**
   * Find the shelf with the given shelf number
   *
   * @param shelfNumber shelf's number to be searched for
   * @return shelf with given shelf number or null if not present
   */
  public Shelf getShelf(Integer shelfNumber) {
    for (Shelf shelf : shelves.values()) {
      if (shelf.getShelfNumber() == shelfNumber) {
        return shelf;
      }
    }
    System.out.println("No shelf number " + shelfNumber + " found");
    return null;
  }

  /**
   * Find the shelf with the given subject
   *
   * @param subject subject of shelf to be searched for
   * @return shelf with given ISBN or null if not present
   */
  public Shelf getShelf(String subject) {
    if (shelves.containsKey(subject)) {
      return shelves.get(subject);
    }
    System.out.println("No shelf for " + subject + " books");
    return null;
  }

  /**
   * Print all readers at the library to the console
   *
   * @return total number of readers
   */
  public int listReaders() {
    int numReaders = 0;
    for (Reader reader : readers) {
      System.out.println(reader.toString());
      numReaders++;
    }
    return  numReaders;
  }

  /**
   * Print all readers at the library to the console
   * and optionally display their checked out books
   *
   * @param showBooks should books be listed?
   * @return total number of readers
   */
  public int listReaders(boolean showBooks) {
    int numReaders = 0;
    if (showBooks) {
      for (Reader reader : readers) {
        System.out.println(reader.getName() + "(#"
            + reader.getCardNumber() + ") has the following books:");
        System.out.println(reader.getBooks());
        numReaders++;
      }
      return numReaders;
    }
    return listReaders(); // print toStings if showBooks is false
  }

  /**
   * Find reader with given card number
   *
   * @param cardNumber card number to be searched for
   * @return reader with given card number or null if not present
   */
  public Reader getReaderByCard(int cardNumber) {
    for (Reader reader : readers) {
      if (reader.getCardNumber() == cardNumber) {
        return reader;
      }
    }
    System.out.println("Could not find a reader with card #" + cardNumber);
    return null;
  }

  /**
   * Adds given reader to the library
   *
   * @param reader reader to be added
   * @return success or appropriate error code
   */
  public Code addReader(Reader reader) {
    // check if reader already exists
    if (readers.contains(reader)) {
      System.out.println(reader.getName() + " already has an account!");
      return Code.READER_ALREADY_EXISTS_ERROR;
    }
    // check for existing reader with card number
    Reader existingReader = getReaderByCard(reader.getCardNumber());
    if (existingReader != null) {
      System.out.println(existingReader.getName() + " and "
          + reader.getName() + " have the same card number!");
      return Code.READER_CARD_NUMBER_ERROR;
    }
    // add reader to library
    readers.add(reader);
    System.out.println(reader.getName() + " added to the library!");
    if (reader.getCardNumber() > libraryCard) {
      libraryCard = reader.getCardNumber();
    }
    return Code.SUCCESS;
  }

  /**
   * Removes given reader to the library
   *
   * @param reader reader to be removed
   * @return success or appropriate error code
   */
  public Code removeReader(Reader reader) {
    if (reader.getBookCount() > 0) { // ensure the reader does not have books checked out
      System.out.println(reader.getName() + " must return all books!");
      return Code.READER_STILL_HAS_BOOKS_ERROR;
    }
    if (!readers.contains(reader)) { // ensure the library has the reader on record
      System.out.println(reader.getName() + " is not part of this Library");
      return Code.READER_NOT_IN_LIBRARY_ERROR;
    }
    readers.remove(reader);
    return Code.SUCCESS;
  }

  /**
   * Converts given string to an integer.
   *
   * @param recordCountString string to be converted
   * @param code error code
   * @return integer conversion of given string or appropriate error code number
   */
  public static int convertInt(String recordCountString, Code code) {
    int conversion;
    try {
      conversion = Integer.parseInt(recordCountString); // set conversion to appropriate integer
    } catch (NumberFormatException e) {
      conversion = code.getCode(); // upon error, set conversion to given error code
      System.out.println("Value which caused the error: " + recordCountString);
      System.out.println("Error message: " + code.getMessage());
    }
    if (conversion < 0) {
      if (code == Code.BOOK_COUNT_ERROR) {
        System.out.println("Error: Could not read number of books");
      } else if (code == Code.PAGE_COUNT_ERROR) {
        System.out.println("Error: could not parse page count");
      } else if (code == Code.DATE_CONVERSION_ERROR) {
        System.out.println("Error: Could not parse date component");
      } else {
        System.out.println("Error: Unknown conversion error");
      }
      return code.getCode();
    }
    return conversion;
  }

  /**
   * Converts given string to a LocalDate value.
   *
   * @param date string to be converted
   * @param errorCode error code
   * @return LocalDate conversion of given string or appropriate error code number
   */
  public static LocalDate convertDate(String date, Code errorCode) {
    if (date.equals("0000")) { // return default date 01-Jan-1970 for "0000"
      return LocalDate.of(1970, 1,1);
    }
    String[] dateToParse = date.split("-");
    if (dateToParse.length != 3) {
      System.out.println("ERROR: " + errorCode.getMessage() + ", could not parse " + date);
      System.out.println("Using default date (01-jan-1970)");
      return LocalDate.of(1970, 1,1);
    }
    // ensure no negative dates
    if (Integer.parseInt(dateToParse[0]) < 0) {
      System.out.println("Error converting date: Year " + dateToParse[0]);
      System.out.println("Using default date (01-jan-1970)");
      return LocalDate.of(1970, 1,1);
    }
    if (Integer.parseInt(dateToParse[1]) < 0) {
      System.out.println("Error converting date: Month " + dateToParse[1]);
      System.out.println("Using default date (01-jan-1970)");
      return LocalDate.of(1970, 1,1);
    }
    if (Integer.parseInt(dateToParse[2]) < 0) {
      System.out.println("Error converting date: Day " + dateToParse[2]);
      System.out.println("Using default date (01-jan-1970)");
      return LocalDate.of(1970, 1,1);
    }
    // catch any other errors
    try {
      return LocalDate.parse(date);
    } catch (DateTimeParseException e) {
      System.out.println("Error parsing date");
      System.out.println("Using default date (01-jan-1970)");
      return LocalDate.of(1970, 1,1);
    }
  }

  /**
   * Calculates next library card number
   *
   * @return next card number
   */
  public static int getLibraryCardNumber() {
    return ++libraryCard;
  }

  /**
   * Finds corresponding Code object wit given code number
   *
   * @param codeNumber number associated with desired Code object
   * @return appropriate Code object
   */
  private Code errorCode(int codeNumber) {
    for (Code code : Code.values()) {
      if (code.getCode() == codeNumber) {
        return code;
      }
    }
    return Code.UNKNOWN_ERROR;
  }
}
