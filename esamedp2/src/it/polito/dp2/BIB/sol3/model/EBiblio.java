package it.polito.dp2.BIB.sol3.model;

import it.polito.dp2.BIB.sol3.service.jaxb.Biblio;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlType(
  name = "",
  propOrder = {
    "items", "journals", "articles", "books", "bookshelves", "self",
  }
)
@XmlRootElement(name = "biblio")
public class EBiblio extends Biblio {
  private transient UriBuilder root;
  private transient UriBuilder items;
  private transient UriBuilder bookshelves;

  public EBiblio() {
    super();
  }

  public EBiblio(UriBuilder root) {
    this.root = root;
    items = root.clone().path("items");
    this.bookshelves = root.clone().path("bookshelves");
  }

  @Override
  @XmlElement(required = true)
  @XmlSchemaType(name = "anyURI")
  public String getItems() {
    return items.toTemplate();
  }

  @Override
  @XmlElement(required = true)
  @XmlSchemaType(name = "anyURI")
  public String getJournals() {
    return root.clone().path("journals").toTemplate();
  }

  @Override
  @XmlElement(required = true)
  @XmlSchemaType(name = "anyURI")
  public String getArticles() {
    return items.clone().path("articles").toTemplate();
  }

  @Override
  @XmlElement(required = true)
  @XmlSchemaType(name = "anyURI")
  public String getBooks() {
    return items.clone().path("books").toTemplate();
  }

  @Override
  @XmlElement(required = true)
  @XmlSchemaType(name = "anyURI")
  public String getBookshelves() {
    return bookshelves.toTemplate();
  }

  @Override
  @XmlElement(required = true)
  @XmlSchemaType(name = "anyURI")
  public String getSelf() {
    return root.toTemplate();
  }
}
