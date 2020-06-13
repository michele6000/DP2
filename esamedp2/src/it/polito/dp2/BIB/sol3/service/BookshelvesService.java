package it.polito.dp2.BIB.sol3.service;

import it.polito.dp2.BIB.sol3.service.jaxb.Bookshelf;
import it.polito.dp2.BIB.sol3.service.jaxb.Bookshelves;
import it.polito.dp2.BIB.sol3.service.jaxb.Item;
import it.polito.dp2.BIB.sol3.service.jaxb.Items;
import it.polito.dp2.BIB.sol3.service.jaxb.Ownership;
import it.polito.dp2.BIB.sol3.service.jaxb.Ownerships;
import it.polito.dp2.BIB.sol3.service.util.ResourseUtils;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.UriInfo;

public class BookshelvesService {
  BookshelvesImpl bookshelves;
  UriInfo uriInfo;
  ResourseUtils rutil;

  /*
   * Public constructor of the service
   */
  public BookshelvesService(UriInfo uriInfo) {
    this.uriInfo = uriInfo;
    this.bookshelves = BookshelvesImpl.getBookshelvesImpl();
    rutil = new ResourseUtils(uriInfo.getBaseUriBuilder());
  }

  public Bookshelves getBookshelves(String keyword) throws Exception {
    Map<BigInteger, Bookshelf> map = this.bookshelves.getBookshelves(keyword);

    if (map == null) {
      throw new Exception("null map returned");
    }

    Bookshelves bookshelves = new Bookshelves();

    List<Bookshelf> list = bookshelves.getBookshelf();
    map.forEach(
      (k, v) -> {
        rutil.completeBookshelf(v, k);
        list.add(v);
      }
    );

    return bookshelves;
  }

  /*
   * service that provides the POST method for the bookshelf
   */
  public Bookshelf createBookshelf(Bookshelf b) throws Exception {
    BigInteger id = this.bookshelves.createBookshelf(b);
    if (id == null) {
      throw new Exception("Null id");
    }

    b.setReadCounter(BigInteger.valueOf(0));
    this.rutil.completeBookshelf(b, id);
    return b;
  }

  /*
   * the getter method for a single bookshelf
   */
  public Bookshelf getBookshelf(BigInteger id) throws Exception {
    if (id == null) {
      throw new Exception("null id");
    }

    Bookshelf b = this.bookshelves.getBookshelf(id);

    if (b != null) {
      rutil.completeBookshelf(b, id);
    }

    return b;
  }

  /*
   * the method that updates a given bookshelf
   */
  public Bookshelf updateBookshelf(BigInteger id, Bookshelf nb)
    throws Exception {
    Bookshelf b = this.bookshelves.updateBookshelf(id, nb);

    if (b != null) {
      rutil.completeBookshelf(b, id);
    }

    return b;
  }

  /*
   * the method that deletes a given bookshelf
   */
  public Bookshelf deleteBookshelf(BigInteger id) {
    return this.bookshelves.deleteBookshelf(id);
  }

  public Ownership getOwnership(BigInteger bid, BigInteger iid) {
    return this.bookshelves.getOwnership(bid, iid);
  }

  /*
   * Get all the elements belonging to a given bookshelf
   */
  public Items getTargets(BigInteger id) throws Exception {
    BiblioService bibService = new BiblioService(this.uriInfo);

    // get the bookshelf
    Bookshelf b = this.bookshelves.getBookshelf(id);
    if (b == null) {
      return null;
    }

    // get the elements on the requested bookshelf
    List<BigInteger> list = this.bookshelves.getTargets(id);
    Items outItems = new Items();
    List<Item> items = outItems.getItem();

    for (BigInteger i : list) {
      Item tmpItem = bibService.getItem(i);
      if (tmpItem != null) {
        items.add(tmpItem);
      }
    }

    b.setReadCounter(b.getReadCounter().add(BigInteger.ONE));

    return outItems;
  }

  /*
   * the method that adds a new element to an existing bookshelf
   */
  public Ownership createOwnership(
    BigInteger bookshelfId,
    BigInteger itemId,
    Ownership ownership
  )
    throws Exception {
    BiblioService bibService = new BiblioService(this.uriInfo);

    // ensure that the bookshelf exists
    Bookshelf b = this.bookshelves.getBookshelf(bookshelfId);
    if (b == null) {
      return null;
    }

    // ensure that the item exists
    Item i = bibService.getItem(itemId);
    if (i == null) {
      return null;
    }

    // ensure that the item is not already in the bookshelf
    BigInteger returnedId = this.bookshelves.getElement(bookshelfId, itemId);
    if (returnedId != null) {
      throw new Exception();
    }

    // ensure that the paths match with the specified ids
    String bookshelfName = Paths
      .get(ownership.getBookshelf())
      .getFileName()
      .toString();
    String itemName = Paths.get(ownership.getItem()).getFileName().toString();

    if (
      !bookshelfName.equals(bookshelfId.toString()) ||
      !itemName.equals(itemId.toString())
    ) {
      throw new ConflictServiceException();
    }

    this.rutil.completeOwnership(ownership, bookshelfId, itemId);
    if (
      this.bookshelves.addOwnership(bookshelfId, itemId, ownership) == false
    ) {
      throw new ConflictServiceException();
    }

    return ownership;
  }

  public Ownerships getOwnerships(BigInteger bookshelfId) {
    Ownerships o = new Ownerships();
    List<Ownership> list = o.getOwnership();

    Collection<Ownership> owns = this.bookshelves.getOwnerships(bookshelfId);
    owns.forEach(
      own -> {
        list.add(own);
      }
    );

    return o;
  }

  public Item getTarget(BigInteger bid, BigInteger iid) throws Exception {
    BiblioService bibService = new BiblioService(this.uriInfo);

    // get the bookshelf
    Bookshelf b = this.bookshelves.getBookshelf(bid);
    if (b == null) {
      return null;
    }

    // get the item id
    BigInteger id = this.bookshelves.getElement(bid, iid);
    if (id == null) {
      return null;
    }

    // get the item
    Item i = bibService.getItem(iid);
    if (i == null) {
      return null;
    }

    this.rutil.completeItem(i, iid);
    b.setReadCounter(b.getReadCounter().add(BigInteger.ONE));

    return i;
  }

  public Ownership deleteOwnership(BigInteger bid, BigInteger idi)
    throws Exception {
    // get the bookshelf
    Bookshelf b = this.bookshelves.getBookshelf(bid);
    if (b == null) {
      throw new Exception();
    }

    // get the item id
    BigInteger id = this.bookshelves.getElement(bid, idi);
    if (id == null) {
      throw new ConflictServiceException();
    }

    return this.bookshelves.removeOwnership(bid, idi);
  }

  public void itemDeleted(BigInteger id) throws Exception {
    this.bookshelves.deleteAllOwnerships(id);
  }

  public Integer getSize(BigInteger bid) throws Exception {
    Integer s;
    try {
      s = this.bookshelves.getMaxSize(bid);
    } catch (Exception e) {
      return null;
    }

    return s;
  }

  public Integer setSize(BigInteger bid, Integer size)
    throws ConflictServiceException {
    try {
      if (this.bookshelves.setMaxSize(bid, size) == false) {
        throw new ConflictServiceException();
      }
    } catch (ConflictServiceException e) {
      throw e;
    } catch (Exception e) {
      return null;
    }

    return size;
  }
}
