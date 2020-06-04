package it.polito.dp2.BIB.sol3.client;

import java.math.BigInteger;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.polito.dp2.BIB.ass3.Bookshelf;
import it.polito.dp2.BIB.ass3.Client;
import it.polito.dp2.BIB.ass3.ItemReader;
import it.polito.dp2.BIB.ass3.ServiceException;
import it.polito.dp2.BIB.ass3.test2.Bookshelf2;
import it.polito.dp2.BIB.sol3.test2.client.Bookshelf2Impl;

public class ClientFactoryImpl implements Client {

	@Override
	public Set<Bookshelf2> getBookshelfs(String name) throws ServiceException {
		Set<Bookshelf2> bookshelfSet = new HashSet<>();

		Response r = target
				.path("bookshelves")
				.queryParam("prefix", name)
				.request(MediaType.APPLICATION_JSON)
				.get();

		if (!(r.getStatus() == 200))
			throw new ServiceException();

		Bookshelves bookshelves = (it.polito.dp2.BIB.sol3.client.Bookshelves) r
				.readEntity(it.polito.dp2.BIB.sol3.client.Bookshelves.class);

		for (it.polito.dp2.BIB.sol3.client.Bookshelves.Bookshelf b : bookshelves.getBookshelf()) {
			bookshelfSet.add((Bookshelf2) new Bookshelf2Impl(b, target));
		}
		
		return bookshelfSet;
	}
	
	/*

	private static void printItems() throws ServiceException {
		Set<ItemReader> set = mainClient.getItems("", 0, 3000);
		System.out.println("Items returned: " + set.size());

		// For each Item print related data
		for (ItemReader item : set) {
			System.out.println("Title: " + item.getTitle());
			if (item.getSubtitle() != null)
				System.out.println("Subtitle: " + item.getSubtitle());
			System.out.print("Authors: ");
			String[] authors = item.getAuthors();
			System.out.print(authors[0]);
			for (int i = 1; i < authors.length; i++)
				System.out.print(", " + authors[i]);
			System.out.println(";");

			Set<ItemReader> citingItems = item.getCitingItems();
			System.out.println("Cited by " + citingItems.size() + " items:");
			for (ItemReader citing : citingItems) {
				System.out.println("- " + citing.getTitle());
			}
			printLine('-');

		}
		printBlankLine();
	}
	
	*/

	@Override
	public Bookshelf createBookshelf(String name) throws ServiceException {
		it.polito.dp2.BIB.sol3.client.Bookshelf bookshelf = new it.polito.dp2.BIB.sol3.client.Bookshelf();
		bookshelf.setName(name);
		bookshelf.setNumberOfReads(BigInteger.ZERO);

		Response r = target.path("bookshelves").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(bookshelf, MediaType.APPLICATION_JSON));

		if (r.getStatus() != 201)
			throw new ServiceException("Error in remote operation: " + r.getStatus() + " " + r.getStatusInfo());

		bookshelf = (it.polito.dp2.BIB.sol3.client.Bookshelf) r
				.readEntity(it.polito.dp2.BIB.sol3.client.Bookshelf.class);
		return (new BookshelfImpl(bookshelf, target));
	}
	
	/*

	private static void printBlankLine() {
		System.out.println(" ");
	}

	private static void printLine(char c) {
		System.out.println(makeLine(c));
	}
	
	*/

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

	public ClientFactoryImpl(URI uri) {
		this.uri = uri.toString();

		client = ClientBuilder.newClient();
		target = client.target(uri).path("biblio");
	}

	static ClientFactoryImpl mainClient;

	public static void main(String[] args) {
		System.setProperty("it.polito.dp2.BIB.BibReaderFactory", "it.polito.dp2.BIB.Random.BibReaderFactoryImpl");
		String myCustomPort = System.getProperty(portProperty);
		String myCustomUri = System.getProperty(urlProperty);
		if (myCustomUri != null)
			uri = myCustomUri;

		/*
		*try {
		*	mainClient = new ClientFactoryImpl(new URI(uri));
		*	//printItems();
		*} catch (URISyntaxException) {
		*	e.printStackTrace();
		*}
		*/
	}

	/*
	*private static StringBuffer makeLine(char c) {
	*	StringBuffer line = new StringBuffer(132);

	*	for (int i = 0; i < 132; ++i) {
	*		line.append(c);
	*	}
	*	return line;
	*}
	*/

	javax.ws.rs.client.Client client;
	WebTarget target;
	static String uri = "http://localhost:8080/BiblioSystem/rest";
	static String urlProperty = "it.polito.dp2.BIB.ass3.URL";
	static String portProperty = "it.polito.dp2.BIB.ass3.PORT";
}
