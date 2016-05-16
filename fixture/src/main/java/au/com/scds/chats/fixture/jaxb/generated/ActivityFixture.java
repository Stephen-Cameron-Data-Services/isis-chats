
package au.com.scds.chats.fixture.jaxb.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ActivityFixture complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ActivityFixture">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="activityType" type="{http://scds.com.au/chats/fixture/jaxb/generated}ActivityTypeFixture" minOccurs="0"/>
 *         &lt;element name="address" type="{http://scds.com.au/chats/fixture/jaxb/generated}AddressFixture" minOccurs="0"/>
 *         &lt;element name="startDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="costForParticipant" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="participations">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="participation" type="{http://scds.com.au/chats/fixture/jaxb/generated}ParticipationFixture" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="volunteeredTimes">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="volunteeredTime" type="{http://scds.com.au/chats/fixture/jaxb/generated}VolunteeredTimeFixture" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "ActivityFixture", propOrder = {
    "activityType",
    "address",
    "startDateTime",
    "name",
    "description",
    "costForParticipant",
    "participations",
    "volunteeredTimes"
})
@XmlSeeAlso({
    ActivityEventFixture.class,
    RecurringActivityFixture.class
})
public class ActivityFixture {

    protected String activityType;
    protected AddressFixture address;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar startDateTime;
    @XmlElement(required = true)
    protected String name;
    protected String description;
    protected String costForParticipant;
    @XmlElement(required = true)
    protected ActivityFixture.Participations participations;
    @XmlElement(required = true)
    protected ActivityFixture.VolunteeredTimes volunteeredTimes;

    /**
     * Gets the value of the activityType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivityType() {
        return activityType;
    }

    /**
     * Sets the value of the activityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivityType(String value) {
        this.activityType = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link AddressFixture }
     *     
     */
    public AddressFixture getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressFixture }
     *     
     */
    public void setAddress(AddressFixture value) {
        this.address = value;
    }

    /**
     * Gets the value of the startDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets the value of the startDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartDateTime(XMLGregorianCalendar value) {
        this.startDateTime = value;
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
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the costForParticipant property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCostForParticipant() {
        return costForParticipant;
    }

    /**
     * Sets the value of the costForParticipant property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCostForParticipant(String value) {
        this.costForParticipant = value;
    }

    /**
     * Gets the value of the participations property.
     * 
     * @return
     *     possible object is
     *     {@link ActivityFixture.Participations }
     *     
     */
    public ActivityFixture.Participations getParticipations() {
        return participations;
    }

    /**
     * Sets the value of the participations property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivityFixture.Participations }
     *     
     */
    public void setParticipations(ActivityFixture.Participations value) {
        this.participations = value;
    }

    /**
     * Gets the value of the volunteeredTimes property.
     * 
     * @return
     *     possible object is
     *     {@link ActivityFixture.VolunteeredTimes }
     *     
     */
    public ActivityFixture.VolunteeredTimes getVolunteeredTimes() {
        return volunteeredTimes;
    }

    /**
     * Sets the value of the volunteeredTimes property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivityFixture.VolunteeredTimes }
     *     
     */
    public void setVolunteeredTimes(ActivityFixture.VolunteeredTimes value) {
        this.volunteeredTimes = value;
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
     *         &lt;element name="participation" type="{http://scds.com.au/chats/fixture/jaxb/generated}ParticipationFixture" maxOccurs="unbounded" minOccurs="0"/>
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
        "participation"
    })
    public static class Participations {

        protected List<ParticipationFixture> participation;

        /**
         * Gets the value of the participation property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the participation property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getParticipation().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ParticipationFixture }
         * 
         * 
         */
        public List<ParticipationFixture> getParticipation() {
            if (participation == null) {
                participation = new ArrayList<ParticipationFixture>();
            }
            return this.participation;
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
     *         &lt;element name="volunteeredTime" type="{http://scds.com.au/chats/fixture/jaxb/generated}VolunteeredTimeFixture" maxOccurs="unbounded" minOccurs="0"/>
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
        "volunteeredTime"
    })
    public static class VolunteeredTimes {

        protected List<VolunteeredTimeFixture> volunteeredTime;

        /**
         * Gets the value of the volunteeredTime property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the volunteeredTime property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getVolunteeredTime().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link VolunteeredTimeFixture }
         * 
         * 
         */
        public List<VolunteeredTimeFixture> getVolunteeredTime() {
            if (volunteeredTime == null) {
                volunteeredTime = new ArrayList<VolunteeredTimeFixture>();
            }
            return this.volunteeredTime;
        }

    }

}
