//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.01 at 01:11:47 PM EST 
//


package au.com.scds.chats.report.dex.model.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import au.com.scds.chats.report.dex.model.generated.Outlet;
import au.com.scds.chats.report.dex.model.generated.OutletActivity;
import au.com.scds.chats.report.dex.model.generated.ResidentialAddress;


/**
 * <p>Java class for Outlet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Outlet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OutletId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Name" type="{}NonEmptyString"/>
 *         &lt;element name="ResidentialAddress" type="{}ResidentialAddress"/>
 *         &lt;element name="OutletActivities" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="OutletActivity" type="{}OutletActivity" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Outlet", propOrder = {
    "outletId",
    "name",
    "residentialAddress",
    "outletActivities"
})
public class Outlet {

    @XmlElement(name = "OutletId", nillable = true)
    protected Integer outletId;
    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "ResidentialAddress", required = true)
    protected ResidentialAddress residentialAddress;
    @XmlElement(name = "OutletActivities", nillable = true)
    protected Outlet.OutletActivities outletActivities;

    /**
     * Gets the value of the outletId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOutletId() {
        return outletId;
    }

    /**
     * Sets the value of the outletId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOutletId(Integer value) {
        this.outletId = value;
    }

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
     * Gets the value of the residentialAddress property.
     * 
     * @return
     *     possible object is
     *     {@link ResidentialAddress }
     *     
     */
    public ResidentialAddress getResidentialAddress() {
        return residentialAddress;
    }

    /**
     * Sets the value of the residentialAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResidentialAddress }
     *     
     */
    public void setResidentialAddress(ResidentialAddress value) {
        this.residentialAddress = value;
    }

    /**
     * Gets the value of the outletActivities property.
     * 
     * @return
     *     possible object is
     *     {@link Outlet.OutletActivities }
     *     
     */
    public Outlet.OutletActivities getOutletActivities() {
        return outletActivities;
    }

    /**
     * Sets the value of the outletActivities property.
     * 
     * @param value
     *     allowed object is
     *     {@link Outlet.OutletActivities }
     *     
     */
    public void setOutletActivities(Outlet.OutletActivities value) {
        this.outletActivities = value;
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
     *         &lt;element name="OutletActivity" type="{}OutletActivity" maxOccurs="unbounded"/>
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
        "outletActivity"
    })
    public static class OutletActivities {

        @XmlElement(name = "OutletActivity", required = true)
        protected List<OutletActivity> outletActivity;

        /**
         * Gets the value of the outletActivity property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the outletActivity property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getOutletActivity().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link OutletActivity }
         * 
         * 
         */
        public List<OutletActivity> getOutletActivity() {
            if (outletActivity == null) {
                outletActivity = new ArrayList<OutletActivity>();
            }
            return this.outletActivity;
        }

    }

}