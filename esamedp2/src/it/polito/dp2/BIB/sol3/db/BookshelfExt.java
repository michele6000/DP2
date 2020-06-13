package it.polito.dp2.BIB.sol3.db;

import it.polito.dp2.BIB.sol3.service.jaxb.Bookshelf;
import java.math.BigInteger;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class BookshelfExt {
  private BigInteger b_id;
  private Bookshelf bookshelf;
  private Set<BigInteger> items;

  public BookshelfExt(BigInteger b_id, Bookshelf bookshelf) {
    this.b_id = b_id;
    this.bookshelf = bookshelf;
    items = new ConcurrentSkipListSet<BigInteger>();
  }

  public Boolean removeItemInListInBookshelf(BigInteger tid)
    throws NotFoundException {
    if (items.remove(tid)) return true; else throw new NotFoundException();
  }

  public Boolean getItemInListInBookshelf(BigInteger tid) {
    return items.contains(tid);
  }

  public Set<BigInteger> getItemListOfBookshelf() {
    return items;
  }

  public Boolean addItemInListInBookshelf(BigInteger tid)
    throws ForbiddenException, ClientErrorException {
    if (items.size() >= 20) throw new ForbiddenException();
    if (items.add(tid)) return true;
    throw new ClientErrorException(Response.Status.CONFLICT);
  }

  public Bookshelf getBookshelf() {
    return this.bookshelf;
  }
}
