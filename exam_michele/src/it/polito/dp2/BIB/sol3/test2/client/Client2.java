package it.polito.dp2.BIB.sol3.test2.client;

import java.math.BigInteger;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.polito.dp2.BIB.ass3.ItemReader;
import it.polito.dp2.BIB.ass3.ServiceException;
import it.polito.dp2.BIB.ass3.test2.Bookshelf2;
import it.polito.dp2.BIB.sol3.client.Bookshelves;
import it.polito.dp2.BIB.sol3.client.ClientFactoryImpl;
import it.polito.dp2.BIB.sol3.client.ItemReaderImpl;
import it.polito.dp2.BIB.sol3.client.Items;

public class Client2 extends ClientFactoryImpl implements it.polito.dp2.BIB.ass3.test2.Client2 {
	
	private String uri;
	private javax.ws.rs.client.Client client;
	private WebTarget target;

	public Client2(URI uri){
		super(uri);
		this.uri = uri.toString();

		client = ClientBuilder.newClient();
		target = client.target(uri).path("biblio");
	}
	
	@Override
	public Set<ItemReader> getItems(String keyword, int since, int to) throws ServiceException {
		Set<ItemReader> itemSet = new HashSet<>();
		Response r = target.path("items").queryParam("keyword", keyword).queryParam("beforeInclusive", to)
				.queryParam("afterInclusive", since).request(MediaType.APPLICATION_JSON).get();

		if (r.getStatus() != 200)
			throw new ServiceException("Error in remote operation: " + r.getStatus() + " " + r.getStatusInfo());

		Items items = (it.polito.dp2.BIB.sol3.client.Items) r.readEntity(it.polito.dp2.BIB.sol3.client.Items.class);

		for (it.polito.dp2.BIB.sol3.client.Items.Item i : items.getItem()) {
			itemSet.add(new ItemReaderImpl(i));
		}

		return itemSet;
	}

	@Override
	public Bookshelf2 createBookshelf(String name) throws ServiceException {
		it.polito.dp2.BIB.sol3.client.Bookshelf bookshelf = new it.polito.dp2.BIB.sol3.client.Bookshelf();
		bookshelf.setName(name);
		bookshelf.setNumberOfReads(BigInteger.ZERO);
		
		Response r = target.path("bookshelves")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(bookshelf, MediaType.APPLICATION_JSON));

		if (r.getStatus() != 201)
			throw new ServiceException("Error in remote operation: " + r.getStatus() + " " + r.getStatusInfo());

		bookshelf = (it.polito.dp2.BIB.sol3.client.Bookshelf) r
				.readEntity(it.polito.dp2.BIB.sol3.client.Bookshelf.class);
		
		Bookshelf2Impl bookImpl = new Bookshelf2Impl(bookshelf, target);
		
		return bookImpl;
	}

	@Override
	public Set<Bookshelf2> getBookshelfs(String name) throws ServiceException {
		Set<Bookshelf2> bookshelfSet = new HashSet<Bookshelf2>();

		Response r = target
				.path("bookshelves")
				.queryParam("prefix", name)
				.request(MediaType.APPLICATION_JSON)
				.get();

		if (r.getStatus() != 200)
				throw new ServiceException();


		Bookshelves bookshelves = (it.polito.dp2.BIB.sol3.client.Bookshelves) r
				.readEntity(it.polito.dp2.BIB.sol3.client.Bookshelves.class);

		for (it.polito.dp2.BIB.sol3.client.Bookshelves.Bookshelf b : bookshelves.getBookshelf()) {
			Bookshelf2Impl impl = new Bookshelf2Impl(b, target);
			bookshelfSet.add((Bookshelf2Impl)impl);
		}
		
		return bookshelfSet;
	}

}
