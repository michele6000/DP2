package it.polito.dp2.BIB.sol3.db;

import it.polito.dp2.BIB.ass3.TooManyItemsException;
import it.polito.dp2.BIB.sol3.service.jaxb.Bookshelf;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

public class BookshelvesDB {

  public synchronized Integer getNumberOfReads(BigInteger b_id)
    throws Exception {
    Integer integer = new Integer(-1);
    BookshelfExt bookshelfExternal = null;

    if ((bookshelfExternal = bookshelfExtById.get(b_id)) == null) return null;

    Bookshelf b = bookshelfExternal.getBookshelf();
    integer = b.getNumberOfReads().intValue();

    b.setNumberOfReads(BigInteger.valueOf(++integer));

    return integer;
  }

  public Bookshelf createBookshelf(BigInteger b_id, Bookshelf bookshelf)
    throws Exception {
    BookshelfExt b_ext = new BookshelfExt(b_id, bookshelf);

    if (bookshelfExtById.putIfAbsent(b_id, b_ext) != null) {
      return null;
    } else return bookshelf;
  }

  public BookshelfExt deleteBookshelf(BigInteger bookshelfID) {
    return bookshelfExtById.remove(bookshelfID);
  }

  public synchronized Map<BigInteger, Bookshelf> getBookshelves(String prefix)
    throws Exception {
    if (!(prefix != null)) prefix = "";

    Map<BigInteger, Bookshelf> mapByPrefix = new HashMap<BigInteger, Bookshelf>();

    for (Map.Entry<BigInteger, BookshelfExt> entry : bookshelfExtById.entrySet()) {
      Bookshelf b = entry.getValue().getBookshelf();
      if (b.getName().startsWith(prefix)) mapByPrefix.put(entry.getKey(), b);
    }

    if (mapByPrefix.size() != 0) {
      return mapByPrefix;
    } else return new HashMap<BigInteger, Bookshelf>();
  }

  public synchronized Bookshelf getBookshelf(BigInteger b_id) throws Exception {
    BookshelfExt b_ext = bookshelfExtById.get(b_id);
    if (!(b_ext != null)) return null;

    Bookshelf b = b_ext.getBookshelf();
    Integer old = b.getNumberOfReads().intValue();
    b.setNumberOfReads(BigInteger.valueOf(++old));

    return b;
  }

  public Boolean addItemInListInBookshelf(BigInteger b_id, BigInteger tid)
    throws ForbiddenException, ClientErrorException, Exception {
    BookshelfExt b_ext = bookshelfExtById.computeIfPresent(
      b_id,
      (k, v) -> {
        try {
          v.addItemInListInBookshelf(tid);
          return v;
        } catch (ForbiddenException e) {
          throw new ForbiddenException();
        } catch (ClientErrorException e) {
          throw new ClientErrorException(e.getResponse().getStatus());
        }
      }
    );
    if (b_ext == null) return false;
    return true;
  }

  private BookshelvesDB() {
    this.bookshelfExtById = new ConcurrentHashMap<BigInteger, BookshelfExt>();
  }

  public static synchronized BigInteger getNextId() throws Exception {
    lastId += 1;
    BigInteger id = BigInteger.valueOf(lastId - 1);
    return id;
  }

  public Map<BigInteger, BookshelfExt> getSharedMap() {
    return this.bookshelfExtById;
  }

  public synchronized boolean getItemInListInBookshelf(
    BigInteger b_id,
    BigInteger tid
  )
    throws Exception {
    BookshelfExt b_ext = bookshelfExtById.get(b_id);

    Bookshelf b = b_ext.getBookshelf();
    Integer old = b.getNumberOfReads().intValue();
    old++;
    b.setNumberOfReads(BigInteger.valueOf(old));

    return b_ext.getItemInListInBookshelf(tid);
  }

  public Boolean removeItemInListInBookshelf(BigInteger b_id, BigInteger tid)
    throws NotFoundException {
    BookshelfExt b_ext = bookshelfExtById.computeIfPresent(
      b_id,
      (k, v) -> {
        try {
          v.removeItemInListInBookshelf(tid);
          return v;
        } catch (NotFoundException e) {
          throw new NotFoundException();
        }
      }
    );
    if (b_ext != null) return true;
    return false;
  }

  public synchronized Set<BigInteger> getItemListOfBookshelf(BigInteger b_id) {
    BookshelfExt b_ext = null;

    try {
      b_ext = bookshelfExtById.get(b_id);

      Bookshelf b = b_ext.getBookshelf();
      Integer old = b.getNumberOfReads().intValue();
      old++;
      b.setNumberOfReads(BigInteger.valueOf(old));

      return b_ext.getItemListOfBookshelf();
    } catch (Exception e) {
      throw new NotFoundException();
    }
  }

  private static Integer lastId = 0;
  private ConcurrentHashMap<BigInteger, BookshelfExt> bookshelfExtById;

  private static BookshelvesDB bookshelvesDB = new BookshelvesDB();

  public static BookshelvesDB getBookshelvesDB() {
    return bookshelvesDB;
  }

  // to lock the inserts of items
  public synchronized void addAllItem(String from, String to)
    throws ForbiddenException, NotFoundException {
    BigInteger fromID = new BigInteger(from);
    BigInteger toID = new BigInteger(to);

    Set<BigInteger> idsItems = getItemListOfBookshelf(fromID);

    // to lock the resource
    boolean success =
      null !=
      bookshelfExtById.computeIfPresent(
        toID,
        (k, v) -> {
          try {
            if (
              v.getItemListOfBookshelf().size() + idsItems.size() > 20
            ) throw new ClientErrorException(403);

            for (BigInteger id : idsItems) v.addItemInListInBookshelf(id);
            return v;
          } catch (ForbiddenException e) {
            throw new ForbiddenException();
          } catch (ClientErrorException e) {
            e.printStackTrace();
            throw new ForbiddenException();
          }
        }
      );

    if (!success) throw new NotFoundException();
  }
}
