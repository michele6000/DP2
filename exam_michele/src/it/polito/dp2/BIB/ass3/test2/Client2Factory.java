package it.polito.dp2.BIB.ass3.test2;

import it.polito.dp2.BIB.FactoryConfigurationError;
import it.polito.dp2.BIB.ass3.ClientException;

/**
 * Defines a factory API that enables applications to access to information
 * about items and bookshelves implementing the {@link Client} interface.
 *
 */
public abstract class Client2Factory {
	private static final String propertyName = "it.polito.dp2.BIB.ass3.test2.Client2Factory";

	protected Client2Factory() {
	}

	public static Client2Factory newInstance() throws FactoryConfigurationError {

		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		if (loader == null) {
			loader = Client2Factory.class.getClassLoader();
		}

		String className = System.getProperty(propertyName);
		if (className == null) {
			throw new FactoryConfigurationError("cannot create a new instance of a Client1Factory"
					+ "since the system property '" + propertyName + "'" + "is not defined");
		}

		try {
			Class<?> c = (loader != null) ? loader.loadClass(className) : Class.forName(className);
			return (Client2Factory) c.newInstance();
		} catch (Exception e) {
			throw new FactoryConfigurationError(e, "error instantiatig class '" + className + "'.");
		}
	}
	
	public abstract Client2 newClient2() throws ClientException;
}
