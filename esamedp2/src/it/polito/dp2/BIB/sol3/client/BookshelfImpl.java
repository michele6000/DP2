package it.polito.dp2.BIB.sol3.client;

import it.polito.dp2.BIB.ass3.DestroyedBookshelfException;
import it.polito.dp2.BIB.ass3.ItemReader;
import it.polito.dp2.BIB.ass3.ServiceException;
import it.polito.dp2.BIB.ass3.TooManyItemsException;
import it.polito.dp2.BIB.ass3.UnknownItemException;
import it.polito.dp2.BIB.sol3.client.Ownerships.Ownership;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BookshelfImpl
  extends it.polito.dp2.BIB.sol3.client.Bookshelf
  implements it.polito.dp2.BIB.ass3.Bookshelf {
  javax.ws.rs.client.Client client;
  protected WebTarget target;
  static String uri = "http://localhost:8080/BiblioSystem/rest";
  static String urlProperty = "it.polito.dp2.BIB.ass3.URL";
  static String portProperty = "it.polito.dp2.BIB.ass3.PORT";
  ObjectFactoryImpl of;
  protected String id;
  private String role;

  public BookshelfImpl(Bookshelves.Bookshelf b) {
    super();
    this.name = b.getName();
    this.readCounter = b.getReadCounter();

    if (b.getOwnerships() != null) {
      this.ownerships = b.getOwnerships();
    }

    if (b.getSelf() != null) {
      this.self = b.getSelf();
    }

    if (b.getTargets() != null) {
      this.targets = b.getTargets();
    }

    client = ClientBuilder.newClient();
    target = client.target(uri).path("biblio");
    this.of = new ObjectFactoryImpl();

    this.id = Paths.get(this.getSelf()).getFileName().toString();
  }

  public BookshelfImpl(String name) {
    super();
    this.name = name;
    this.readCounter = BigInteger.ZERO;

    client = ClientBuilder.newClient();
    target = client.target(uri).path("biblio");
    this.of = new ObjectFactoryImpl();
  }

  @Override
  public void addItem(ItemReader item)
    throws DestroyedBookshelfException, UnknownItemException, TooManyItemsException, ServiceException {
    String itemId = ((ItemReaderImpl) item).getId();
    String itemPath = ((ItemReaderImpl) item).getSelf();

    String bookshelfId = Paths.get(this.getSelf()).getFileName().toString();
    String bookshelfPath = this.getSelf();

    Ownership o = this.of.createOwnership(itemPath, bookshelfPath);

    String path = "/bookshelves/" + bookshelfId + "/ownerships/" + itemId;

    Response r2 = target
      .path(path)
      .request(MediaType.APPLICATION_JSON_TYPE)
      .header("role", this.role)
      .put(Entity.entity(o, MediaType.APPLICATION_JSON_TYPE));

    int status = r2.getStatus();

    if (status == 404) {
      throw new DestroyedBookshelfException();
    }

    if (status == 409) {
      throw new TooManyItemsException();
    }

    if (status != 200) {
      throw new ServiceException();
    }
  }

  @Override
  public void removeItem(ItemReader item)
    throws DestroyedBookshelfException, UnknownItemException, ServiceException {
    String itemId = ((ItemReaderImpl) item).getId().toString();
    String bookshelfId = Paths.get(this.getSelf()).getFileName().toString();

    String path = "/bookshelves/" + bookshelfId + "/ownerships/" + itemId;

    Response r2 = target
      .path(path)
      .request(MediaType.APPLICATION_JSON_TYPE)
      .header("role", this.role)
      .delete();

    int status = r2.getStatus();

    if (status == 404) {
      throw new DestroyedBookshelfException();
    }

    if (status == 409) {
      throw new UnknownItemException();
    }

    if (status != 204) {
      throw new ServiceException();
    }
  }

  @Override
  public Set<ItemReader> getItems()
    throws DestroyedBookshelfException, ServiceException {
    String path = "/bookshelves/" + this.id + "/ownerships/targets";

    Response r = target
      .path(path)
      .request(MediaType.APPLICATION_JSON_TYPE)
      .header("role", this.role)
      .get();

    int status = r.getStatus();

    if (status == 404) {
      throw new DestroyedBookshelfException();
    }

    if (status != 200) {
      throw new ServiceException();
    }

    Items items = r.readEntity(Items.class);
    Set<ItemReader> itemSet = new HashSet<>();

    for (it.polito.dp2.BIB.sol3.client.Items.Item i : items.getItem()) {
      itemSet.add(new ItemReaderImpl(i));
    }

    return itemSet;
  }

  @Override
  public void destroyBookshelf()
    throws DestroyedBookshelfException, ServiceException {
    String path = "/bookshelves/" + this.id;

    Response r = target
      .path(path)
      .request(MediaType.APPLICATION_JSON_TYPE)
      .header("role", this.role)
      .delete();

    int status = r.getStatus();

    if (status == 404) {
      throw new DestroyedBookshelfException();
    }

    if (status != 204) {
      throw new ServiceException();
    }
  }

  @Override
  public int getNumberOfReads()
    throws DestroyedBookshelfException, ServiceException {
    String path = "/bookshelves/" + this.id;

    Response r = target
      .path(path)
      .request(MediaType.APPLICATION_JSON_TYPE)
      .header("role", this.role)
      .get();

    int status = r.getStatus();

    if (status == 404) {
      throw new DestroyedBookshelfException();
    }

    if (status != 200) {
      throw new ServiceException();
    }

    BookshelfImpl b = new BookshelfImpl(
      r.readEntity(Bookshelves.Bookshelf.class)
    );

    this.setName(b.getName());
    this.setOwnerships(b.getOwnerships());
    this.setReadCounter(b.getReadCounter());
    this.setSelf(b.getSelf());
    this.setTargets(b.getTargets());

    return this.getReadCounter().intValue();
  }
}
