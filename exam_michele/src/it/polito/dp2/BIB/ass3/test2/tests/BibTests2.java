package it.polito.dp2.BIB.ass3.test2.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.dp2.BIB.BibReaderFactory;
import it.polito.dp2.BIB.FactoryConfigurationError;
import it.polito.dp2.BIB.ass3.Bookshelf;
import it.polito.dp2.BIB.ass3.ClientFactory;
import it.polito.dp2.BIB.ass3.DestroyedBookshelfException;
import it.polito.dp2.BIB.ass3.ItemReader;
import it.polito.dp2.BIB.ass3.ServiceException;
import it.polito.dp2.BIB.ass3.TooManyItemsException;
import it.polito.dp2.BIB.ass3.UnknownItemException;
import it.polito.dp2.BIB.ass3.admClient.AdminClient;
import it.polito.dp2.BIB.ass3.test2.Bookshelf2;
import it.polito.dp2.BIB.ass3.test2.Client2;
import it.polito.dp2.BIB.ass3.test2.Client2Factory;
import it.polito.dp2.BIB.ass3.tests.BibTests;

public class BibTests2 extends BibTests {
	private static Client2 testClient2; // Client2 under new tests

	// prepare the environment
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Create reference data generator
		System.setProperty("it.polito.dp2.BIB.BibReaderFactory", "it.polito.dp2.BIB.Random.BibReaderFactoryImpl");
		referenceBibReader = BibReaderFactory.newInstance().newBibReader();

		try {
			testClient2 = Client2Factory.newInstance().newClient2();
			testClient = ClientFactory.newInstance().newClient();
			adminClient = new AdminClient();
		} catch (FactoryConfigurationError fce) {
			fce.printStackTrace();
		}
		assertNotNull("Internal tester error during test setup: null reference", referenceBibReader);
		assertNotNull("Internal tester error during test setup: null reference", adminClient);
		assertNotNull("Could not run test: the implementation under test generated a null Client2", testClient2);
		assertNotNull("Could not run test: the implementation under test generated a null Client", testClient);
	}

	/**
	 * Check that adding items from one bookshelf to another bookshelf is correctly managed
	 * @throws ServiceException
	 * @throws DestroyedBookshelfException
	 * @throws UnknownItemException
	 * @throws TooManyItemsException
	 */
	@Test
	public final void testAddAllFewItems()
			throws ServiceException, DestroyedBookshelfException, UnknownItemException, TooManyItemsException {
		System.out.println("DEBUG: starting testAddAllFewItems");

		// initial number of items
		int max = 5;

		// creating 2 bookshelves for the test
		Bookshelf2 bookshelfA = testClient2.createBookshelf("Legend");
		Bookshelf2 bookshelfB = testClient2.createBookshelf("Detective");

		assertNotNull("The implementation under test generated a null Bookshelf2", bookshelfA);
		assertNotNull("The implementation under test generated a null Bookshelf2", bookshelfB);

		// add limited items to the bookshelf
		addLimitedItemsBookshelf(bookshelfA, max);
		assertNotNull("The getItems of the implementation under test generated a null set of added ItemReader",
				bookshelfA.getItems());

		// check if correct number of items were added to the first bookshelf
		assertEquals("wrong number of items for the bookshelf1 " + bookshelfA.getName(), max, bookshelfA.getItems().size());

		// add all items from bookshelfA to bookshelfB
		bookshelfB.addAll(bookshelfA);
		assertNotNull("The getItems of the implementation under test generated a null set of added ItemReader",	bookshelfB.getItems());

		assertEquals("wrong number of items in bookshelf " + bookshelfB.getName(), bookshelfA.getItems().size(),
				bookshelfB.getItems().size());

	}

	/**
	 * Check that adding items more than required throws the expected exception
	 * @throws ServiceException
	 * @throws TooManyItemsException
	 * @throws UnknownItemException
	 */
	@Test(expected = TooManyItemsException.class)
	public final void testAddAllMoreItems()
			throws ServiceException, DestroyedBookshelfException, UnknownItemException, TooManyItemsException {
		System.out.println("DEBUG: starting testAddAllFewItems");

		// initial number of items
		int max = 15;

		// creating 3 bookshelves for the test
		Bookshelf2 bookshelfA = testClient2.createBookshelf("Mythology");
		Bookshelf2 bookshelfB = testClient2.createBookshelf("Adventure");
		Bookshelf2 bookshelfC = testClient2.createBookshelf("Classic");

		assertNotNull("The implementation under test generated a null Bookshelf2", bookshelfA);
		assertNotNull("The implementation under test generated a null Bookshelf2", bookshelfB);
		assertNotNull("The implementation under test generated a null Bookshelf2", bookshelfC);

		// add limited items to the bookshelves
		addLimitedItemsBookshelf(bookshelfA, max);
		addLimitedItemsBookshelf(bookshelfB, max);

		assertNotNull("The getItems of the implementation under test generated a null set of added ItemReader",
				bookshelfA.getItems());
		assertNotNull("The getItems of the implementation under test generated a null set of added ItemReader",
				bookshelfB.getItems());

		bookshelfC.addAll(bookshelfA);
		// check if adding items more than required throws the expected exception
		bookshelfC.addAll(bookshelfB);
	}

	/**
	 * Check that adding items from the destroyed bookshelf throws the expected exception
	 * @throws ServiceException
	 * @throws DestroyedBookshelfException
	 * @throws TooManyItemsException
	 */
	@Test(expected = DestroyedBookshelfException.class)
	public final void testAddItemsToDestroyedBookshelf()
			throws ServiceException, DestroyedBookshelfException, TooManyItemsException {
		System.out.println("DEBUG: starting testAddAllFewItems");

		// creating 2 bookshelves for the test
		Bookshelf2 bookshelf1 = testClient2.createBookshelf("Novel");
		Bookshelf2 bookshelf2 = testClient2.createBookshelf("SciFi");

		assertNotNull("The implementation under test generated a null Bookshelf2", bookshelf1);
		assertNotNull("The implementation under test generated a null Bookshelf2", bookshelf2);

		bookshelf1.destroyBookshelf();

		// check if adding items to the destroyed bookshelf throws the expected exception
		bookshelf2.addAll(bookshelf1);
	}

	
	// add all items that are available in the system to the current bookshelf up to limit and check number of items
	private int addLimitedItemsBookshelf(Bookshelf bookshelf, int limit)
			throws ServiceException, DestroyedBookshelfException, UnknownItemException, TooManyItemsException {
		Set<ItemReader> items = testClient2.getItems("", 0, 3000);
		assertNotNull("The getItems of the implementation under test generated a null set of ItemReader", items);

		TreeSet<ItemReader> sortedItems = new TreeSet<ItemReader>(new ItemReaderComparator());
		sortedItems.addAll(items);

		// limit the number of items to be added to the bookshelf
		int numberOfItemsInBookshelf = 0;
		for (ItemReader item : sortedItems) {
			if (numberOfItemsInBookshelf >= limit)
				break;
			bookshelf.addItem(item);
			numberOfItemsInBookshelf++;
		}

		Set<ItemReader> itemsAfterAdd = bookshelf.getItems();
		assertNotNull("The getItems of the implementation under test generated a null set of ItemReader",
				itemsAfterAdd);
		int actual = itemsAfterAdd.size();

		// check the number of items in bookshelf has been increased as expected
		assertEquals("Wrong number of items in the bookshelf " + bookshelf.getName() + " after adding "
				+ numberOfItemsInBookshelf + " items ", numberOfItemsInBookshelf, actual);
		return numberOfItemsInBookshelf;
	}

	class ItemReaderComparator implements Comparator<ItemReader> {
		public int compare(ItemReader f0, ItemReader f1) {
			if (f0 == f1)
				return 0;
			if (f0 == null)
				return -1;
			if (f1 == null)
				return 1;
			String title0 = f0.getTitle();
			String title1 = f1.getTitle();
			if (title0 == title1)
				return 0;
			if (title0 == null)
				return -1;
			if (title1 == null)
				return 1;
			return title0.compareTo(title1);
		}
	}

}
