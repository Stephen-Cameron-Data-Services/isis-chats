
package au.com.scds.chats.fixture.jaxb.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NamesFixture complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NamesFixture">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="regions">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="region" type="{http://scds.com.au/chats/fixture/jaxb/generated}RegionFixture" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="activityTypes">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="activityType" type="{http://scds.com.au/chats/fixture/jaxb/generated}ActivityTypeFixture" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="transportTypes">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="transportType" type="{http://scds.com.au/chats/fixture/jaxb/generated}TransportTypeFixture" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="periodicities">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="periodicity" type="{http://scds.com.au/chats/fixture/jaxb/generated}PeriodicityFixture" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="contactTypes">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="contactType" type="{http://scds.com.au/chats/fixture/jaxb/generated}ContactTypeFixture" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="sexes">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="sex" type="{http://scds.com.au/chats/fixture/jaxb/generated}SexFixture" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="statuses">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="status" type="{http://scds.com.au/chats/fixture/jaxb/generated}StatusFixture" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="volunteerRoles">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="volunteerRole" type="{http://scds.com.au/chats/fixture/jaxb/generated}VolunteerRoleFixture" maxOccurs="unbounded"/>
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
@XmlType(name = "NamesFixture", propOrder = {
    "regions",
    "activityTypes",
    "transportTypes",
    "periodicities",
    "contactTypes",
    "sexes",
    "statuses",
    "volunteerRoles"
})
public class NamesFixture {

    @XmlElement(required = true)
    protected NamesFixture.Regions regions;
    @XmlElement(required = true)
    protected NamesFixture.ActivityTypes activityTypes;
    @XmlElement(required = true)
    protected NamesFixture.TransportTypes transportTypes;
    @XmlElement(required = true)
    protected NamesFixture.Periodicities periodicities;
    @XmlElement(required = true)
    protected NamesFixture.ContactTypes contactTypes;
    @XmlElement(required = true)
    protected NamesFixture.Sexes sexes;
    @XmlElement(required = true)
    protected NamesFixture.Statuses statuses;
    @XmlElement(required = true)
    protected NamesFixture.VolunteerRoles volunteerRoles;

    /**
     * Gets the value of the regions property.
     * 
     * @return
     *     possible object is
     *     {@link NamesFixture.Regions }
     *     
     */
    public NamesFixture.Regions getRegions() {
        return regions;
    }

    /**
     * Sets the value of the regions property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamesFixture.Regions }
     *     
     */
    public void setRegions(NamesFixture.Regions value) {
        this.regions = value;
    }

    /**
     * Gets the value of the activityTypes property.
     * 
     * @return
     *     possible object is
     *     {@link NamesFixture.ActivityTypes }
     *     
     */
    public NamesFixture.ActivityTypes getActivityTypes() {
        return activityTypes;
    }

    /**
     * Sets the value of the activityTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamesFixture.ActivityTypes }
     *     
     */
    public void setActivityTypes(NamesFixture.ActivityTypes value) {
        this.activityTypes = value;
    }

    /**
     * Gets the value of the transportTypes property.
     * 
     * @return
     *     possible object is
     *     {@link NamesFixture.TransportTypes }
     *     
     */
    public NamesFixture.TransportTypes getTransportTypes() {
        return transportTypes;
    }

    /**
     * Sets the value of the transportTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamesFixture.TransportTypes }
     *     
     */
    public void setTransportTypes(NamesFixture.TransportTypes value) {
        this.transportTypes = value;
    }

    /**
     * Gets the value of the periodicities property.
     * 
     * @return
     *     possible object is
     *     {@link NamesFixture.Periodicities }
     *     
     */
    public NamesFixture.Periodicities getPeriodicities() {
        return periodicities;
    }

    /**
     * Sets the value of the periodicities property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamesFixture.Periodicities }
     *     
     */
    public void setPeriodicities(NamesFixture.Periodicities value) {
        this.periodicities = value;
    }

    /**
     * Gets the value of the contactTypes property.
     * 
     * @return
     *     possible object is
     *     {@link NamesFixture.ContactTypes }
     *     
     */
    public NamesFixture.ContactTypes getContactTypes() {
        return contactTypes;
    }

    /**
     * Sets the value of the contactTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamesFixture.ContactTypes }
     *     
     */
    public void setContactTypes(NamesFixture.ContactTypes value) {
        this.contactTypes = value;
    }

    /**
     * Gets the value of the sexes property.
     * 
     * @return
     *     possible object is
     *     {@link NamesFixture.Sexes }
     *     
     */
    public NamesFixture.Sexes getSexes() {
        return sexes;
    }

    /**
     * Sets the value of the sexes property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamesFixture.Sexes }
     *     
     */
    public void setSexes(NamesFixture.Sexes value) {
        this.sexes = value;
    }

    /**
     * Gets the value of the statuses property.
     * 
     * @return
     *     possible object is
     *     {@link NamesFixture.Statuses }
     *     
     */
    public NamesFixture.Statuses getStatuses() {
        return statuses;
    }

    /**
     * Sets the value of the statuses property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamesFixture.Statuses }
     *     
     */
    public void setStatuses(NamesFixture.Statuses value) {
        this.statuses = value;
    }

    /**
     * Gets the value of the volunteerRoles property.
     * 
     * @return
     *     possible object is
     *     {@link NamesFixture.VolunteerRoles }
     *     
     */
    public NamesFixture.VolunteerRoles getVolunteerRoles() {
        return volunteerRoles;
    }

    /**
     * Sets the value of the volunteerRoles property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamesFixture.VolunteerRoles }
     *     
     */
    public void setVolunteerRoles(NamesFixture.VolunteerRoles value) {
        this.volunteerRoles = value;
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
     *         &lt;element name="activityType" type="{http://scds.com.au/chats/fixture/jaxb/generated}ActivityTypeFixture" maxOccurs="unbounded"/>
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
        "activityType"
    })
    public static class ActivityTypes {

        @XmlElement(required = true)
        protected List<String> activityType;

        /**
         * Gets the value of the activityType property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the activityType property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getActivityType().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getActivityType() {
            if (activityType == null) {
                activityType = new ArrayList<String>();
            }
            return this.activityType;
        }

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
     *         &lt;element name="contactType" type="{http://scds.com.au/chats/fixture/jaxb/generated}ContactTypeFixture" maxOccurs="unbounded"/>
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
        "contactType"
    })
    public static class ContactTypes {

        @XmlElement(required = true)
        protected List<String> contactType;

        /**
         * Gets the value of the contactType property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the contactType property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getContactType().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getContactType() {
            if (contactType == null) {
                contactType = new ArrayList<String>();
            }
            return this.contactType;
        }

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
     *         &lt;element name="periodicity" type="{http://scds.com.au/chats/fixture/jaxb/generated}PeriodicityFixture" maxOccurs="unbounded"/>
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
        "periodicity"
    })
    public static class Periodicities {

        @XmlElement(required = true)
        protected List<PeriodicityFixture> periodicity;

        /**
         * Gets the value of the periodicity property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the periodicity property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPeriodicity().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PeriodicityFixture }
         * 
         * 
         */
        public List<PeriodicityFixture> getPeriodicity() {
            if (periodicity == null) {
                periodicity = new ArrayList<PeriodicityFixture>();
            }
            return this.periodicity;
        }

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
     *         &lt;element name="region" type="{http://scds.com.au/chats/fixture/jaxb/generated}RegionFixture" maxOccurs="unbounded"/>
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
        "region"
    })
    public static class Regions {

        @XmlElement(required = true)
        protected List<String> region;

        /**
         * Gets the value of the region property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the region property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRegion().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getRegion() {
            if (region == null) {
                region = new ArrayList<String>();
            }
            return this.region;
        }

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
     *         &lt;element name="sex" type="{http://scds.com.au/chats/fixture/jaxb/generated}SexFixture" maxOccurs="unbounded"/>
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
        "sex"
    })
    public static class Sexes {

        @XmlElement(required = true)
        protected List<SexFixture> sex;

        /**
         * Gets the value of the sex property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the sex property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSex().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SexFixture }
         * 
         * 
         */
        public List<SexFixture> getSex() {
            if (sex == null) {
                sex = new ArrayList<SexFixture>();
            }
            return this.sex;
        }

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
     *         &lt;element name="status" type="{http://scds.com.au/chats/fixture/jaxb/generated}StatusFixture" maxOccurs="unbounded"/>
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
        "status"
    })
    public static class Statuses {

        @XmlElement(required = true)
        protected List<StatusFixture> status;

        /**
         * Gets the value of the status property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the status property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getStatus().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link StatusFixture }
         * 
         * 
         */
        public List<StatusFixture> getStatus() {
            if (status == null) {
                status = new ArrayList<StatusFixture>();
            }
            return this.status;
        }

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
     *         &lt;element name="transportType" type="{http://scds.com.au/chats/fixture/jaxb/generated}TransportTypeFixture" maxOccurs="unbounded"/>
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
        "transportType"
    })
    public static class TransportTypes {

        @XmlElement(required = true)
        protected List<String> transportType;

        /**
         * Gets the value of the transportType property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the transportType property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTransportType().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getTransportType() {
            if (transportType == null) {
                transportType = new ArrayList<String>();
            }
            return this.transportType;
        }

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
     *         &lt;element name="volunteerRole" type="{http://scds.com.au/chats/fixture/jaxb/generated}VolunteerRoleFixture" maxOccurs="unbounded"/>
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
        "volunteerRole"
    })
    public static class VolunteerRoles {

        @XmlElement(required = true)
        protected List<String> volunteerRole;

        /**
         * Gets the value of the volunteerRole property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the volunteerRole property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getVolunteerRole().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getVolunteerRole() {
            if (volunteerRole == null) {
                volunteerRole = new ArrayList<String>();
            }
            return this.volunteerRole;
        }

    }

}