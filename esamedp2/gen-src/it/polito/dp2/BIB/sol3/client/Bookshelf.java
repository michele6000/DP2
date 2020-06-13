
package it.polito.dp2.BIB.sol3.client;

import java.math.BigInteger;
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
@XmlRootElement(name = "bookshelf")
public class Bookshelf {

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
