package it.polito.dp2.BIB.sol3.test2.client;

import java.net.URI;
import java.net.URISyntaxException;

import it.polito.dp2.BIB.ass3.ClientException;
import it.polito.dp2.BIB.sol3.client.ClientFactoryImpl;

public class Client2Factory extends it.polito.dp2.BIB.ass3.test2.Client2Factory {

	@Override
	public it.polito.dp2.BIB.ass3.test2.Client2 newClient2() throws ClientException {
		URI uri = null;
		try {
			String uriProp = System.getProperty("it.polito.dp2.BIB.ass3.URL");
			if (uriProp != null) {
				uri = new URI(uriProp);
			} else {
				uri = new URI("http://localhost:8080/BibSystem/rest/");
			}

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return new Client2(uri);
	}

}
