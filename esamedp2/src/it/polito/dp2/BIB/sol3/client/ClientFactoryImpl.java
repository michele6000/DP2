package it.polito.dp2.BIB.sol3.client;

import it.polito.dp2.BIB.ass3.Bookshelf;
import it.polito.dp2.BIB.ass3.Client;
import it.polito.dp2.BIB.ass3.ItemReader;
import it.polito.dp2.BIB.ass3.ServiceException;
import java.math.BigInteger;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ClientFactoryImpl implements Client {
  static String uri = "http://localhost:8080/BiblioSystem/rest";
  static String urlProperty = "it.polito.dp2.BIB.ass3.URL";
  static String portProperty = "it.polito.dp2.BIB.ass3.PORT";
  javax.ws.rs.client.Client client;
  WebTarget target;

  public ClientFactoryImpl(URI uri) {
    ClientFactoryImpl.uri = uri.toString();

    client = ClientBuilder.newClient();
    target = client.target(uri).path("biblio");
  }

  public static void main(String[] args) {
    System.setProperty(
      "it.polito.dp2.BIB.BibReaderFactory",
      "it.polito.dp2.BIB.Random.BibReaderFactoryImpl"
    );
    System.getProperty(portProperty);
    String myCustomUri = System.getProperty(urlProperty);
    if (myCustomUri != null) uri = myCustomUri;
  }

  @Override
  public Set<Bookshelf> getBookshelfs(String name) throws ServiceException {
    Set<Bookshelf> bookshelfSet = new HashSet<>();

    Response r = target
      .path("bookshelves")
      .queryParam("prefix", name)
      .request(MediaType.APPLICATION_JSON)
      .get();

    if (!(r.getStatus() == 200)) throw new ServiceException();

    Bookshelves bookshelves = r.readEntity(Bookshelves.class);

    for (it.polito.dp2.BIB.sol3.client.Bookshelves.Bookshelf b : bookshelves.getBookshelf()) {
      bookshelfSet.add(new BookshelfImpl(b, target));
    }

    return bookshelfSet;
  }

  @Override
  public Bookshelf createBookshelf(String name) throws ServiceException {
    it.polito.dp2.BIB.sol3.client.Bookshelf bookshelf = new it.polito.dp2.BIB.sol3.client.Bookshelf();
    bookshelf.setName(name);
    bookshelf.setNumberOfReads(BigInteger.ZERO);

    Response r = target
      .path("bookshelves")
      .request(MediaType.APPLICATION_JSON)
      .post(Entity.entity(bookshelf, MediaType.APPLICATION_JSON));

    if (r.getStatus() != 201) throw new ServiceException(
      "Error in remote operation: " + r.getStatus() + " " + r.getStatusInfo()
    );

    bookshelf = r.readEntity(it.polito.dp2.BIB.sol3.client.Bookshelf.class);
    return (new BookshelfImpl(bookshelf, target));
  }

  @Override
  public Set<ItemReader> getItems(String keyword, int since, int to)
    throws ServiceException {
    Set<ItemReader> itemSet = new HashSet<>();
    Response r = target
      .path("items")
      .queryParam("keyword", keyword)
      .queryParam("beforeInclusive", to)
      .queryParam("afterInclusive", since)
      .request(MediaType.APPLICATION_JSON)
      .get();

    if (r.getStatus() != 200) throw new ServiceException(
      "Error in remote operation: " + r.getStatus() + " " + r.getStatusInfo()
    );

    Items items = r.readEntity(Items.class);

    for (it.polito.dp2.BIB.sol3.client.Items.Item i : items.getItem()) {
      itemSet.add(new ItemReaderImpl(i));
    }

    return itemSet;
  }
}
