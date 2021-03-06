
package com.ebay.soap.eBLBaseComponents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * 
 * 				ProStores listing level preferences.
 * 			
 * 
 * <p>Java class for ListingCheckoutRedirectPreferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListingCheckoutRedirectPreferenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProStoresStoreName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SellerThirdPartyUsername" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;any/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListingCheckoutRedirectPreferenceType", propOrder = {
    "proStoresStoreName",
    "sellerThirdPartyUsername",
    "any"
})
public class ListingCheckoutRedirectPreferenceType
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "ProStoresStoreName")
    protected String proStoresStoreName;
    @XmlElement(name = "SellerThirdPartyUsername")
    protected String sellerThirdPartyUsername;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the proStoresStoreName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProStoresStoreName() {
        return proStoresStoreName;
    }

    /**
     * Sets the value of the proStoresStoreName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProStoresStoreName(String value) {
        this.proStoresStoreName = value;
    }

    /**
     * Gets the value of the sellerThirdPartyUsername property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSellerThirdPartyUsername() {
        return sellerThirdPartyUsername;
    }

    /**
     * Sets the value of the sellerThirdPartyUsername property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSellerThirdPartyUsername(String value) {
        this.sellerThirdPartyUsername = value;
    }

    /**
     * 
     * 
     * @return
     *     array of
     *     {@link Object }
     *     {@link Element }
     *     
     */
    public Object[] getAny() {
        if (this.any == null) {
            return new Object[ 0 ] ;
        }
        return ((Object[]) this.any.toArray(new Object[this.any.size()] ));
    }

    /**
     * 
     * 
     * @return
     *     one of
     *     {@link Object }
     *     {@link Element }
     *     
     */
    public Object getAny(int idx) {
        if (this.any == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.any.get(idx);
    }

    public int getAnyLength() {
        if (this.any == null) {
            return  0;
        }
        return this.any.size();
    }

    /**
     * 
     * 
     * @param values
     *     allowed objects are
     *     {@link Object }
     *     {@link Element }
     *     
     */
    public void setAny(Object[] values) {
        this._getAny().clear();
        int len = values.length;
        for (int i = 0; (i<len); i ++) {
            this.any.add(values[i]);
        }
    }

    protected List<Object> _getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return any;
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     {@link Element }
     *     
     */
    public Object setAny(int idx, Object value) {
        return this.any.set(idx, value);
    }

}
