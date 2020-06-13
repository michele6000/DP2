package it.polito.dp2.BIB.sol3.client;

import static org.junit.Assert.assertTrue;

import it.polito.dp2.BIB.ass3.Bookshelf;
import it.polito.dp2.BIB.ass3.Client;
import it.polito.dp2.BIB.ass3.ItemReader;
import it.polito.dp2.BIB.ass3.ServiceException;
import it.polito.dp2.BIB.ass3.admClient.BookType;
import it.polito.dp2.BIB.ass3.admClient.Items.Item;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class ClientFactoryImpl implements Client {
  protected javax.ws.rs.client.Client client;
  protected WebTarget target;
  ObjectFactoryImpl of;
  protected static String uri = "http://localhost:8080/BiblioSystem/rest";
  static String urlProperty = "it.polito.dp2.BIB.ass3.URL";
  static String portProperty = "it.polito.dp2.BIB.ass3.PORT";

  protected ClientFactoryImpl(URI uri, String role) {
    client = ClientBuilder.newClient();
    target = client.target(uri).path("biblio");
    this.of = new ObjectFactoryImpl();
  }

  public ClientFactoryImpl(URI uri) {
    client = ClientBuilder.newClient();
    target = client.target(uri).path("biblio");
    this.of = new ObjectFactoryImpl();
  }

  @Override
  public Bookshelf createBookshelf(String name) throws ServiceException {
    Bookshelves.Bookshelf b = this.of.createBookshelf(name);

    Response r = target
      .path("/bookshelves")
      .request(MediaType.APPLICATION_JSON_TYPE)
      .post(Entity.entity(b, MediaType.APPLICATION_JSON_TYPE));

    if (r.getStatus() != 201) {
      throw new ServiceException();
    }

    BookshelfImpl b2 = new BookshelfImpl(
      r.readEntity(Bookshelves.Bookshelf.class)
    );
    return b2;
  }

  @Override
  public Set<Bookshelf> getBookshelfs(String name) throws ServiceException {
    Response r = target
      .path("/bookshelves")
      .queryParam("keyword", name)
      .request(MediaType.APPLICATION_JSON_TYPE)
      .get();

    int status = r.getStatus();

    if (status != 200) {
      throw new ServiceException();
    }

    Bookshelves bookshelves = r.readEntity(Bookshelves.class);

    Set<Bookshelf> set = new HashSet<Bookshelf>();
    for (Bookshelves.Bookshelf b : bookshelves.getBookshelf()) {
      set.add(new BookshelfImpl(b));
    }

    return set;
  }

  @Override
  public Set<ItemReader> getItems(String keyword, int since, int to)
    throws ServiceException {
    Set<ItemReader> itemSet = new HashSet<>();
    Response r = target
      .path("/items")
      .queryParam("keyword", keyword)
      .queryParam("beforeInclusive", to)
      .queryParam("afterInclusive", since)
      .request(MediaType.APPLICATION_JSON_TYPE)
      .get();

    int status = r.getStatus();

    if (status != 200) {
      throw new ServiceException();
    }

    Items items = r.readEntity(Items.class);

    for (it.polito.dp2.BIB.sol3.client.Items.Item i : items.getItem()) {
      itemSet.add(new ItemReaderImpl(i));
    }

    return itemSet;
  }

  @SuppressWarnings("unused")
  private static void printItems() throws ServiceException {
    Set<ItemReader> set = mainClient.getItems("", 0, 3000);
    System.out.println("Items returned: " + set.size());

    // For each Item print related data
    for (ItemReader item : set) {
      System.out.println("Title: " + item.getTitle());
      if (item.getSubtitle() != null) System.out.println(
        "Subtitle: " + item.getSubtitle()
      );
      System.out.print("Authors: ");
      String[] authors = item.getAuthors();
      System.out.print(authors[0]);
      for (int i = 1; i < authors.length; i++) System.out.print(
        ", " + authors[i]
      );
      System.out.println(";");

      Set<ItemReader> citingItems = item.getCitingItems();
      System.out.println("Cited by " + citingItems.size() + " items:");
      for (ItemReader citing : citingItems) {
        System.out.println("- " + citing.getTitle());
      }
      printLine('-');
    }
    printBlankLine();
  }

  private static void printBlankLine() {
    System.out.println(" ");
  }

  private static void printLine(char c) {
    System.out.println(makeLine(c));
  }

  private static StringBuffer makeLine(char c) {
    StringBuffer line = new StringBuffer(132);

    for (int i = 0; i < 132; ++i) {
      line.append(c);
    }
    return line;
  }

  static ClientFactoryImpl mainClient;

  @SuppressWarnings("unused")
  private static Item buildItem() {
    Item itemToSend = new Item();
    itemToSend.setTitle("The Way of Z");
    itemToSend.setSubtitle("Practica1 Programming with Formal Methods");
    itemToSend.getAuthor().add("J. Yusupov");
    BookType book = new BookType();
    book.setISBN("0131411553");
    book.setPublisher("Addison Wesley");
    itemToSend.setBook(book);
    try {
      XMLGregorianCalendar calendar = DatatypeFactory
        .newInstance()
        .newXMLGregorianCalendar();
      calendar.setYear(1996);
      book.setYear(calendar);
    } catch (DatatypeConfigurationException e) {
      assertTrue("Unexpected internal error", false);
    }
    return itemToSend;
  }

  @SuppressWarnings("unused")
  public static void main(String[] args) {
    System.setProperty(
      "it.polito.dp2.BIB.BibReaderFactory",
      "it.polito.dp2.BIB.Random.BibReaderFactoryImpl"
    );
    String customUri = System.getProperty(urlProperty);
    String customPort = System.getProperty(portProperty);

    if (customUri != null) uri = customUri;
    try {} catch (Exception e) {
      e.printStackTrace();
    }
  }
}
