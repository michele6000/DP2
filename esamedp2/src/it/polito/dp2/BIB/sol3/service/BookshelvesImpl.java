package it.polito.dp2.BIB.sol3.service;

import it.polito.dp2.BIB.sol3.service.jaxb.Bookshelf;
import it.polito.dp2.BIB.sol3.service.jaxb.Ownership;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/*
 * Singleton implementation of the bookshelves service
 */
public class BookshelvesImpl {
  private static BookshelvesImpl bookshelvesImpl = new BookshelvesImpl();

  private Map<BigInteger, Bookshelf> bookshelves;
  private Map<BigInteger, List<BigInteger>> elements;
  private Map<BigInteger, Map<BigInteger, Ownership>> ownerships;
  private Map<BigInteger, Integer> sizes;
  private Map<BigInteger, Integer> maxSizes;

  private BigInteger id;

  private int maxSize;
  private int currentSize;

  private BookshelvesImpl() {
    this.bookshelves = new ConcurrentHashMap<BigInteger, Bookshelf>();
    this.elements = new ConcurrentHashMap<BigInteger, List<BigInteger>>();
    this.ownerships =
      new ConcurrentHashMap<BigInteger, Map<BigInteger, Ownership>>();
    this.sizes = new ConcurrentHashMap<BigInteger, Integer>();
    this.maxSizes = new ConcurrentHashMap<BigInteger, Integer>();
    this.id = new BigInteger("0");
    this.maxSize = 20;
    this.currentSize = 0;
  }

  /*
   * Get the singleton instance of this class
   */
  public static BookshelvesImpl getBookshelvesImpl() {
    return bookshelvesImpl;
  }

  public boolean setMaxSize(BigInteger bid, int size) {
    if (size < this.maxSize) {
      this.maxSizes.replace(bid, size);
      return true;
    }

    return false;
  }

  public Integer getMaxSize(BigInteger bid) {
    return this.maxSizes.get(bid).intValue();
  }

  /*
   * GET all the bookshelves with name matching a given keyword
   */
  public Map<BigInteger, Bookshelf> getBookshelves(String keyword) {
    try {
      HashMap<BigInteger, Bookshelf> map = new HashMap<BigInteger, Bookshelf>();

      this.bookshelves.forEach(
          (k, v) -> {
            if (keyword == "" || v.getName().contains(keyword)) {
              map.put(k, v);
            }
          }
        );

      return map;
    } catch (Exception e) {
      System.out.println("exception in building temporary map");
      return null;
    }
  }

  /*
   * GET method for a specific bookshelf, given the id
   */
  public Bookshelf getBookshelf(BigInteger id) {
    return this.bookshelves.get(id);
  }

  /*
   * POST method for a new bookshelf; a new id is created
   */
  public BigInteger createBookshelf(Bookshelf bookshelf) {
    this.bookshelves.put(this.id, bookshelf);
    this.elements.put(this.id, new CopyOnWriteArrayList<BigInteger>());
    this.ownerships.put(
        this.id,
        new ConcurrentHashMap<BigInteger, Ownership>()
      );
    this.sizes.put(this.id, 0);
    this.maxSizes.put(this.id, this.maxSize);

    BigInteger prevId = this.id;
    this.id = this.id.add(BigInteger.ONE);

    return prevId;
  }

  /*
   * PUT method to update an existing bookshelf, given its id
   */
  public Bookshelf updateBookshelf(BigInteger id, Bookshelf bookshelf) {
    Bookshelf b = this.bookshelves.get(id);
    if (b != null) {
      this.bookshelves.put(id, bookshelf);
      return bookshelf;
    }
    return b;
  }

  /*
   * DELETE method to delete an existing bookshelf, given its id
   */
  public Bookshelf deleteBookshelf(BigInteger id) {
    this.elements.remove(id);
    Bookshelf b = this.bookshelves.remove(id);

    return b;
  }

  /*
   * GET all the elements of a given bookshelf
   */
  public List<BigInteger> getTargets(BigInteger id) {
    return this.elements.get(id);
  }

  /*
   * PUT method to add an ownership to a bookshelf,
   * given both the id of the bookshelf and item
   */
  public boolean addOwnership(
    BigInteger bid,
    BigInteger iid,
    Ownership ownership
  ) {
    if (this.currentSize == this.maxSize) {
      return false;
    }

    this.elements.get(bid).add(iid);
    this.ownerships.get(bid).put(iid, ownership);

    Integer value = this.sizes.get(bid) + 1;
    this.sizes.replace(bid, value);

    return true;
  }

  public Ownership getOwnership(BigInteger bid, BigInteger iid) {
    return this.ownerships.get(bid).get(iid);
  }

  /*
   * returns the element id searched if found, otherwise it returns null
   */
  public BigInteger getElement(BigInteger bookshelfId, BigInteger elementId) {
    List<BigInteger> list = this.elements.get(bookshelfId);

    System.out.println("elementId to find: " + elementId);
    for (BigInteger e : list) {
      System.out.println(e);
      if (e.equals(elementId)) {
        return e;
      }
    }

    return null;
  }

  public Collection<Ownership> getOwnerships(BigInteger id) {
    return this.ownerships.get(id).values();
  }

  public Ownership removeOwnership(BigInteger bid, BigInteger idi) {
    List<BigInteger> list = this.elements.get(bid);

    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).equals(idi)) {
        list.remove(i);
        this.currentSize--;
      }
    }

    return this.ownerships.get(bid).remove(idi);
  }

  public void deleteAllOwnerships(BigInteger id) {
    this.elements.forEach(
        (k, v) -> {
          v.remove(id);

          Integer value = this.sizes.get(k) + 1;
          this.sizes.replace(k, value);
        }
      );

    for (Map<BigInteger, Ownership> map : this.ownerships.values()) {
      map.remove(id);
    }
  }
}
