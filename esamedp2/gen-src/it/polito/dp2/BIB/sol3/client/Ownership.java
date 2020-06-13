
package it.polito.dp2.BIB.sol3.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bookshelf" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="item" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="self" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bookshelf",
    "item",
    "self"
})
@XmlRootElement(name = "ownership")
public class Ownership {

    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String bookshelf;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String item;
    @XmlSchemaType(name = "anyURI")
    protected String self;

    /**
     * Gets the value of the bookshelf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBookshelf() {
        return bookshelf;
    }

    /**
     * Sets the value of the bookshelf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBookshelf(String value) {
        this.bookshelf = value;
    }

    /**
     * Gets the value of the item property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem() {
        return item;
    }

    /**
     * Sets the value of the item property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem(String value) {
        this.item = value;
    }

    /**
     * Gets the value of the self property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelf() {
        return self;
    }

    /**
     * Sets the value of the self property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelf(String value) {
        this.self = value;
    }

}
