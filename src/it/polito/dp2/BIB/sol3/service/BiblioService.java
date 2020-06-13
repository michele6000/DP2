package it.polito.dp2.BIB.sol3.service;

import it.polito.dp2.BIB.sol3.db.*;
import it.polito.dp2.BIB.sol3.service.jaxb.*;
import it.polito.dp2.BIB.sol3.service.util.ResourseUtils;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BiblioService {
    ResourseUtils rutil;
    private final DB n4jDb = Neo4jDB.getNeo4jDB();
    // Creation BookshelvesDB instance
    private final BookshelvesDB bookshelvesDB = BookshelvesDB.getBookshelvesDB();

    public BiblioService(UriInfo uriInfo) {
        rutil = new ResourseUtils((uriInfo.getBaseUriBuilder()));
    }

    public Bookshelves getBookshelves(String prefix) throws Exception {
        Set<Entry<BigInteger, Bookshelf>> b_set = bookshelvesDB.getBookshelves(prefix).entrySet();

        Bookshelves bookshelves = new Bookshelves();
        List<Bookshelf> list = bookshelves.getBookshelf(); // Creation bookshelf
        // list if not yet
        // created

        for (Map.Entry<BigInteger, Bookshelf> entry : b_set) {
            Bookshelf b = entry.getValue();
            list.add(b);
            Integer old = b.getNumberOfReads().intValue();
            old++;
            b.setNumberOfReads(BigInteger.valueOf(old));
        }
        return bookshelves;
    }

    public BigInteger getNextId() throws Exception {
        return BookshelvesDB.getNextId();
    }

    public void deleteBookshelf(BigInteger b_id) throws Exception {

        if ((bookshelvesDB.deleteBookshelf(b_id)) == null)
            throw new NotFoundException();
    }

    public Bookshelf getBookshelf(BigInteger b_id) throws Exception {

        Bookshelf b = bookshelvesDB.getBookshelf(b_id);
        if (b == null)
            throw new NotFoundException();

        return b;
    }

    public Item getItemInListInBookshelf(BigInteger b_id, BigInteger tid)
            throws Exception {

        Bookshelf b = getBookshelf(b_id);
        if (b == null)
            throw new BadRequestException();

        Item item = null;

        if (bookshelvesDB.getItemInListInBookshelf(b_id, tid)) {
            item = getItem(tid);
            if (item == null)
                throw new NotFoundException();
        } else
            throw new NotFoundException();

        return item;
    }

    public Bookshelf createBookshelf(BigInteger b_id, Bookshelf bookshelf) throws Exception {

        rutil.completeBookshelf(bookshelf, b_id);
        bookshelf = bookshelvesDB.createBookshelf(b_id, bookshelf);
        if (bookshelf == null)
            throw new Exception();
        return bookshelf;
    }

    public Items getItemListOfBookshelf(BigInteger b_id) throws Exception {

        if (getBookshelf(b_id) == null)
            throw new NotFoundException();
        Items items = new Items();

        for (BigInteger tid : bookshelvesDB.getItemListOfBookshelf(b_id)) {
            items.getItem().add(getItem(tid));
        }

        return items;
    }

    public Item addItemInListInBookshelf(BigInteger b_id, BigInteger tid)
            throws Exception {

        // Check if item in Neo4j is available yet
        Item item = getItem(tid);
        if (item == null)
            throw new NotFoundException();

        Boolean success = bookshelvesDB.addItemInListInBookshelf(b_id, tid);
        if (!success)
            throw new BadRequestException();
        return item;
    }

    public Integer getNumberOfReads(BigInteger b_id) throws Exception {
        Integer reads;

        reads = bookshelvesDB.getNumberOfReads(b_id);
        if (reads == null)
            throw new NotFoundException();

        return reads;
    }

    public void removeItemInListInBookshelf(BigInteger b_id, BigInteger tid)
            throws Exception {

        Boolean success = bookshelvesDB.removeItemInListInBookshelf(b_id, tid);
        if (!success)
            throw new BadRequestException();
    }

    public Map<BigInteger, BookshelfExt> getSharedMap() {
        return bookshelvesDB.getSharedMap();
    }

    public Items getItems(SearchScope scope, String keyword, int beforeInclusive, int afterInclusive, BigInteger page)
            throws Exception {
        ItemPage itemPage = n4jDb.getItems(scope, keyword, beforeInclusive, afterInclusive, page);

        Items items = new Items();
        List<Item> list = items.getItem();

        Set<Entry<BigInteger, Item>> set = itemPage.getMap().entrySet();
        for (Entry<BigInteger, Item> entry : set) {
            Item item = entry.getValue();
            rutil.completeItem(item, entry.getKey());
            list.add(item);
        }
        items.setTotalPages(itemPage.getTotalPages());
        items.setPage(page);
        return items;
    }

    public BigInteger deleteItem(BigInteger id) throws Exception {
        try {

            Map<BigInteger, BookshelfExt> b_map = bookshelvesDB.getSharedMap();
            for (Map.Entry<BigInteger, BookshelfExt> entry : b_map.entrySet()) {
                BigInteger b_id = entry.getKey();
                try {
                    bookshelvesDB.removeItemInListInBookshelf(b_id, id);
                } catch (NotFoundException e) {
                    System.out.println("Bookshelf: " + b_id + "Not contains item: " + id);
                }
            }

            return n4jDb.deleteItem(id);
        } catch (ConflictInOperationException e) {
            throw new ConflictServiceException();
        }
    }

    public Item getItem(BigInteger id) throws Exception {
        Item item = n4jDb.getItem(id);
        if (item != null)
            rutil.completeItem(item, id);
        return item;
    }

    public Item updateItem(BigInteger id, Item item) throws Exception {
        Item ret = n4jDb.updateItem(id, item);
        if (ret != null) {
            rutil.completeItem(item, id);
            return item;
        } else
            return null;
    }

    public Item createItem(Item item) throws Exception {
        BigInteger id = n4jDb.createItem(item);
        if (id == null)
            throw new Exception("Null id");
        rutil.completeItem(item, id);
        return item;
    }

    public boolean deleteItemCitation(BigInteger id, BigInteger tid) throws Exception {
        return n4jDb.deleteItemCitation(id, tid);
    }

    public Citation getItemCitation(BigInteger id, BigInteger tid) throws Exception {
        Citation citation = n4jDb.getItemCitation(id, tid);
        if (citation != null)
            rutil.completeCitation(citation, id, tid);
        return citation;
    }

    public Citation createItemCitation(BigInteger id, BigInteger tid, Citation citation) throws Exception {
        try {
            return n4jDb.createItemCitation(id, tid, citation);
        } catch (BadRequestInOperationException e) {
            throw new BadRequestServiceException();
        }
    }

    public Items getItemCitedBy(BigInteger id) throws Exception {
        ItemPage itemPage = n4jDb.getItemCitedBy(id, BigInteger.ONE);
        if (itemPage == null)
            return null;

        Items items = new Items();
        List<Item> list = items.getItem();

        Set<Entry<BigInteger, Item>> set = itemPage.getMap().entrySet();
        for (Entry<BigInteger, Item> entry : set) {
            Item item = entry.getValue();
            rutil.completeItem(item, entry.getKey());
            list.add(item);
        }
        items.setTotalPages(itemPage.getTotalPages());
        items.setPage(BigInteger.ONE);
        return items;
    }

    public Items getItemCitations(BigInteger id) throws Exception {
        ItemPage itemPage = n4jDb.getItemCitations(id, BigInteger.ONE);
        if (itemPage == null)
            return null;

        Items items = new Items();
        List<Item> list = items.getItem();

        Set<Entry<BigInteger, Item>> set = itemPage.getMap().entrySet();
        for (Entry<BigInteger, Item> entry : set) {
            Item item = entry.getValue();
            rutil.completeItem(item, entry.getKey());
            list.add(item);
        }
        items.setTotalPages(itemPage.getTotalPages());
        items.setPage(BigInteger.ONE);
        return items;
    }


//	public void copyBookshelfItems(String from, String to)  throws ForbiddenException,NotFoundException {
//		bookshelvesDB.addAllItem(to, from);
//	}
}
