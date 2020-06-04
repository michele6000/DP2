/**
 * 
 */
package it.polito.dp2.BIB.ass3.test2;

import java.util.Set;

import it.polito.dp2.BIB.ass3.Client;
import it.polito.dp2.BIB.ass3.ServiceException;

/**
 * An extended version of the interface for interacting with the Biblio Service (extended for final test 2)
 *
 */
public interface Client2 extends Client {
	/**
	 * Creates a new bookshelf with the given name (this operation is performed on the remote service)
	 * @param name the name of the bookshelf to be created
	 * @return an interface for operating on the created bookshelf
	 * @throws ServiceException if the operation cannot be completed because of problems with the remote service
	 */
	public Bookshelf2 createBookshelf(String name) throws ServiceException;
	
	/**
	 * Finds bookshelves whose name matches the given name (this operation is performed on the remote service)
	 * @param name the name to be searched
	 * @return a set of interfaces for operating on the selected bookshelves
	 * @throws ServiceException
	 */
	public Set<? extends Bookshelf2> getBookshelfs(String name) throws ServiceException;

}
