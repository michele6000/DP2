package it.polito.dp2.BIB.sol3.test2.client;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.util.Json;
import it.polito.dp2.BIB.ass3.DestroyedBookshelfException;
import it.polito.dp2.BIB.ass3.ServiceException;
import it.polito.dp2.BIB.ass3.TooManyItemsException;
import it.polito.dp2.BIB.sol3.client.Bookshelf;
import it.polito.dp2.BIB.sol3.client.BookshelfImpl;

public class Bookshelf2Impl extends BookshelfImpl implements it.polito.dp2.BIB.ass3.test2.Bookshelf2 {
	private WebTarget target;
	
	public Bookshelf2Impl(Bookshelf b, WebTarget target) {
		super(b, target);
		
		this.target = target;
	}
	
	public Bookshelf2Impl(it.polito.dp2.BIB.sol3.client.Bookshelves.Bookshelf b2, WebTarget target2) {
		super(b2,target2);
		this.target = target2;
	}

	public String getID(){
		String selfUri = super.getSelf();

		String[] strings = selfUri.split("/");
		String bookshelfID = strings[strings.length-1];
		
		return bookshelfID;
	}

	@Override
	public void addAll(it.polito.dp2.BIB.ass3.Bookshelf otherBookshelf)
			throws DestroyedBookshelfException, TooManyItemsException, ServiceException {
		
		Bookshelf2Impl otherClass = (Bookshelf2Impl) otherBookshelf;
		String otherID = otherClass.getID();
		String myID = this.getID();
		
		
		Response r = target.path("bookshelves")
				.path(myID)
				.path(otherID)
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(null));
		
		if (r.getStatus() != 204) {
			switch (r.getStatus()) {
			case 409:
				throw new ServiceException();
			case 404:
				throw new DestroyedBookshelfException();
			case 403:
				throw new TooManyItemsException();
			case 400:
				throw new DestroyedBookshelfException();
			default:
				throw new ServiceException();
			}
		}
	}

}
