
package it.polito.dp2.BIB.sol3.client;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="bookshelf" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="readCounter" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="ownerships" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                   &lt;element name="targets" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                   &lt;element name="self" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "self"
})
@XmlRootElement(name = "bookshelves")
public class Bookshelves {

    @XmlElement(nillable = true)
    protected List<Bookshelves.Bookshelf> bookshelf;
    @XmlSchemaType(name = "anyURI")
    protected String self;

    /**
     * Gets the value of the bookshelf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bookshelf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBookshelf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Bookshelves.Bookshelf }
     * 
     * 
     */
    public List<Bookshelves.Bookshelf> getBookshelf() {
        if (bookshelf == null) {
            bookshelf = new ArrayList<Bookshelves.Bookshelf>();
        }
        return this.bookshelf;
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
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="readCounter" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="ownerships" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
     *         &lt;element name="targets" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
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
        "name",
        "readCounter",
        "ownerships",
        "targets",
        "self"
    })
    public static class Bookshelf {

        @XmlElement(required = true)
        protected String name;
        @XmlElement(required = true)
        protected BigInteger readCounter;
        @XmlSchemaType(name = "anyURI")
        protected String ownerships;
        @XmlSchemaType(name = "anyURI")
        protected String targets;
        @XmlSchemaType(name = "anyURI")
        protected String self;

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the readCounter property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getReadCounter() {
            return readCounter;
        }

        /**
         * Sets the value of the readCounter property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setReadCounter(BigInteger value) {
            this.readCounter = value;
        }

        /**
         * Gets the value of the ownerships property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOwnerships() {
            return ownerships;
        }

        /**
         * Sets the value of the ownerships property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOwnerships(String value) {
            this.ownerships = value;
        }

        /**
         * Gets the value of the targets property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTargets() {
            return targets;
        }

        /**
         * Sets the value of the targets property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTargets(String value) {
            this.targets = value;
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

}
