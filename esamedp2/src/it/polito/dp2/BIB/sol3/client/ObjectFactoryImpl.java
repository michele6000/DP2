package it.polito.dp2.BIB.sol3.client;

import java.math.BigInteger;

public class ObjectFactoryImpl
  extends it.polito.dp2.BIB.sol3.client.ObjectFactory {

  public Bookshelves.Bookshelf createBookshelf(String name) {
    Bookshelves.Bookshelf b = super.createBookshelvesBookshelf();

    b.setName(name);
    b.setReadCounter(BigInteger.ZERO);

    return b;
  }

  public Ownerships.Ownership createOwnership(
    String itemPath,
    String bookshelfPath
  ) {
    Ownerships.Ownership o = super.createOwnershipsOwnership();

    o.setBookshelf(bookshelfPath);
    o.setItem(itemPath);

    return o;
  }
}
