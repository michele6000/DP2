/**
 * 
 */
package it.polito.dp2.BIB.ass3.test2;

import it.polito.dp2.BIB.ass3.Bookshelf;
import it.polito.dp2.BIB.ass3.DestroyedBookshelfException;
import it.polito.dp2.BIB.ass3.ServiceException;
import it.polito.dp2.BIB.ass3.TooManyItemsException;

/**
 * An extended version of the Bookshelf interface (extended for final test 2) that
 * also allows the addition of all the items in another bookshelf to this bookshelf.
 *
 */
public interface Bookshelf2 extends Bookshelf {
	/**
	 * Add all the items of another bookshelf to this bookshelf
	 * @param otherBookshelf the other bookshelf
	 * @throws DestroyedBookshelfException if this bookshelf or the other bookshelf does no longer exist or is unknown
	 * @throws TooManyItemsException if the operation would cause the maximum number of items in this bookshelf to be exceeded
	 * @throws ServiceException if the operation cannot be completed because of a problem with the remote service
	 */
	public void addAll(Bookshelf otherBookshelf) throws  DestroyedBookshelfException, TooManyItemsException, ServiceException; 
}
