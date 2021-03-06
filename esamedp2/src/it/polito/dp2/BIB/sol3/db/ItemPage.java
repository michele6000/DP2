package it.polito.dp2.BIB.sol3.db;

import it.polito.dp2.BIB.sol3.service.jaxb.Item;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class ItemPage {

  public void setTotalPages(BigInteger totalPages) {
    this.totalPages = totalPages;
  }

  public BigInteger getTotalPages() {
    return totalPages;
  }

  public Map<BigInteger, Item> getMap() {
    if (map == null) map = new HashMap<BigInteger, Item>();
    return map;
  }

  Map<BigInteger, Item> map;
  BigInteger totalPages = BigInteger.ONE;
}
