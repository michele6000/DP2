package it.polito.dp2.BIB.ass3;

import java.util.Set;

/**
 * An interface for operating on a bookshelf through a remote service
 * A bookshelf has a name (fixed upon creation) and it can contain items,
 * up to a maximum number (the maximum is fixed in the implementation being used)
 *
 */
public interface Bookshelf {
	/**
	 * Gets the name of the bookshelf (this is a local operation, the name is retrieved locally)
	 * @return the name of the bookshelf
	 * @throws DestroyedBookshelfException if the bookshelf represented by this interface does no longer exist
	 */
	public String getName() throws DestroyedBookshelfException;
	
	/**
	 * Adds an item to the bookshelf (this operation is done on the remote service)
	 * @param item the item to be added (it must be an object generated by a compatible implementation)
	 * @throws DestroyedBookshelfException if the bookshelf represented by this interface does no longer exist
	 * @throws UnknownItemException if the item object is not compatible with this implementation
	 * @throws TooManyItemsException if the item cannot be added because the bookshelf is full (reached its maximum number of items)
	 * @throws ServiceException if the operation could not be completed because of a problem with the remote service
	 */
	public void addItem(ItemReader item) throws DestroyedBookshelfException, UnknownItemException, TooManyItemsException, ServiceException;
	
	/**
	 * Removes an item from the bookshelf (if it is present; this operation is done on the remote service)
	 * @param item the item to be removed (it must be an object generated by a compatible implementation)
	 * @throws DestroyedBookshelfException if the bookshelf represented by this interface does no longer exist
	 * @throws UnknownItemException if the item object is not compatible with this implementation
	 * @throws ServiceException if the operation could not be completed because of a problem with the remote service
	 */
	public void removeItem(ItemReader item) throws DestroyedBookshelfException, UnknownItemException, ServiceException;
	
	/**
	 * Gets the items that are currently in the bookshelf (this operation is done on the remote service)
	 * @return a set of interfaces for reading the information of the items
	 * @throws DestroyedBookshelfException if the bookshelf represented by this interface does no longer exist
	 * @throws ServiceException if the operation could not be completed because of a problem with the remote service
	 */
	public Set<ItemReader> getItems() throws DestroyedBookshelfException, ServiceException;
	
	/**
	 * Destroys the bookshelf (this operation is done on the remote service)
	 * @throws DestroyedBookshelfException if the bookshelf represented by this interface does no longer exist
	 * @throws ServiceException if the operation could not be completed because of a problem with the remote service
	 */
	public void destroyBookshelf() throws DestroyedBookshelfException, ServiceException;
	
	/**
	 * Gives the total number of read operations performed on this bookshelf (this operation is done on the remote service)
	 * @return the number of read operations
	 * @throws DestroyedBookshelfException if the bookshelf represented by this interface does no longer exist
	 * @throws ServiceException if the operation could not be completed because of a problem with the remote service
	 */
	public int getNumberOfReads() throws DestroyedBookshelfException, ServiceException;
}
