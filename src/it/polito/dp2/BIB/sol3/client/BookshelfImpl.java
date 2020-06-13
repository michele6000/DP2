package it.polito.dp2.BIB.sol3.client;

import it.polito.dp2.BIB.ass3.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

public class BookshelfImpl implements it.polito.dp2.BIB.ass3.Bookshelf {

    String bookshelfID;
    private final String bookshelfName;
    private final int readedTimes;
    private final String self;
    private final Set<ItemReader> listOfItems;
    private final WebTarget myWebTarget;

    public BookshelfImpl(it.polito.dp2.BIB.sol3.client.Bookshelf b, WebTarget target) {
        this.bookshelfName = b.getName();

        this.self = b.getSelf();
        String[] strings = this.self.split("/");
        this.bookshelfID = strings[strings.length - 1];
        this.listOfItems = new HashSet<>();
        this.readedTimes = b.getNumberOfReads().intValue();
        this.myWebTarget = target;
    }

    public BookshelfImpl(it.polito.dp2.BIB.sol3.client.Bookshelves.Bookshelf b, WebTarget target) {
        this.bookshelfName = b.getName();
        this.self = b.getSelf();
        String[] strins = this.self.split("/");
        this.readedTimes = b.getNumberOfReads().intValue();
        this.bookshelfID = strins[strins.length - 1];
        this.myWebTarget = target;
        this.listOfItems = new HashSet<>();
    }

    @Override
    public void addItem(ItemReader item)
            throws DestroyedBookshelfException, UnknownItemException, TooManyItemsException, ServiceException {

        if (!(item instanceof ItemReaderImpl))
            throw new UnknownItemException();

        Integer itemID = ((ItemReaderImpl) item).getId();

        Response r = myWebTarget
                .path("bookshelves")
                .path(bookshelfID)
                .path("items")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(itemID, MediaType.TEXT_PLAIN));

        if (r.getStatus() != 201) {
            switch (r.getStatus()) {
                case 409:
                    throw new ServiceException();
                case 404:
                    throw new ServiceException();
                case 403:
                    throw new TooManyItemsException();
                case 400:
                    throw new DestroyedBookshelfException();
                default:
                    throw new ServiceException();
            }
        }

        it.polito.dp2.BIB.sol3.client.Items.Item i = r.readEntity(it.polito.dp2.BIB.sol3.client.Items.Item.class);
        listOfItems.add(new ItemReaderImpl(i));
    }

    @Override
    public Set<ItemReader> getItems() throws DestroyedBookshelfException, ServiceException {

        Set<ItemReader> itemSet = new HashSet<>();
        Response r = myWebTarget.path("bookshelves")
                .path(bookshelfID)
                .path("items")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (r.getStatus() != 200) {
            if (r.getStatus() != 400)
                throw new ServiceException();
            else
                throw new DestroyedBookshelfException();
        }

        return getItemReaders(itemSet, r);
    }

    @Override
    public String getName() throws DestroyedBookshelfException {

        Response r = myWebTarget.path("bookshelves").path(bookshelfID).request(MediaType.APPLICATION_JSON).get();

        if (r.getStatus() != 200)
            throw new DestroyedBookshelfException(
                    "Error in remote operation: " + r.getStatus() + " " + r.getStatusInfo());

        return this.bookshelfName;
    }

    @Override
    public int getNumberOfReads() throws DestroyedBookshelfException, ServiceException {
        Response r = myWebTarget
                .path("bookshelves")
                .path(bookshelfID)
                .path("numberOfReads")
                .request(MediaType.TEXT_PLAIN)
                .get();

        if (r.getStatus() != 200) {
            if (r.getStatus() != 404)
                throw new ServiceException();
            else
                throw new DestroyedBookshelfException();
        }

        String reads_s = r.readEntity(String.class);
        int reads = 0;

        try {
            reads = Integer.parseInt(reads_s);
        } catch (NumberFormatException e) {
            throw new ServiceException();
        }

        return reads;
    }

    @Override
    public void removeItem(ItemReader item) throws DestroyedBookshelfException, UnknownItemException, ServiceException {
        String tid = ((ItemReaderImpl) item).getId().toString();

        Response r = myWebTarget
                .path("bookshelves")
                .path(bookshelfID)
                .path("items")
                .path(tid)
                .request()
                .delete();

        if (r.getStatus() == 204) {

            listOfItems.remove(item);
        } else {

            switch (r.getStatus()) {
                case 404:
                    throw new UnknownItemException();
                case 400:
                    throw new DestroyedBookshelfException();
                default:
                    throw new ServiceException();
            }
        }

    }

    @Override
    public void destroyBookshelf() throws DestroyedBookshelfException, ServiceException {

        Response r = myWebTarget
                .path("bookshelves")
                .path(bookshelfID)
                .request()
                .delete();

        if (r.getStatus() != 204) {
            if (r.getStatus() != 404)
                throw new ServiceException();
            else
                throw new DestroyedBookshelfException();
        }
    }

    public String getSelf() {
        return self;
    }


}
