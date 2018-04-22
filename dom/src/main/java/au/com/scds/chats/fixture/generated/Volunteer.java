//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.04.20 at 01:35:06 PM AEST 
//


package au.com.scds.chats.fixture.generated;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import org.w3._2001.xmlschema.Adapter1;


/**
 * <p>Java class for Volunteer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Volunteer"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="person" type="{http://lifelinetasmania.org.au/chats}ChatsPerson"/&gt;
 *         &lt;element name="callAllocation" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="participant" type="{http://lifelinetasmania.org.au/chats}ChatsParticipant"/&gt;
 *                   &lt;element name="indicativeCallTime" type="{http://www.w3.org/2001/XMLSchema}time" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="call" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="participant" type="{http://lifelinetasmania.org.au/chats}ChatsParticipant"/&gt;
 *                   &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                   &lt;element name="end" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                   &lt;element name="notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Volunteer", propOrder = {
    "person",
    "callAllocation",
    "call"
})
public class Volunteer {

    @XmlElement(required = true)
    protected ChatsPerson person;
    protected List<Volunteer.CallAllocation> callAllocation;
    protected List<Volunteer.Call> call;

    /**
     * Gets the value of the person property.
     * 
     * @return
     *     possible object is
     *     {@link ChatsPerson }
     *     
     */
    public ChatsPerson getPerson() {
        return person;
    }

    /**
     * Sets the value of the person property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChatsPerson }
     *     
     */
    public void setPerson(ChatsPerson value) {
        this.person = value;
    }

    /**
     * Gets the value of the callAllocation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the callAllocation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCallAllocation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Volunteer.CallAllocation }
     * 
     * 
     */
    public List<Volunteer.CallAllocation> getCallAllocation() {
        if (callAllocation == null) {
            callAllocation = new ArrayList<Volunteer.CallAllocation>();
        }
        return this.callAllocation;
    }

    /**
     * Gets the value of the call property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the call property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCall().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Volunteer.Call }
     * 
     * 
     */
    public List<Volunteer.Call> getCall() {
        if (call == null) {
            call = new ArrayList<Volunteer.Call>();
        }
        return this.call;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="participant" type="{http://lifelinetasmania.org.au/chats}ChatsParticipant"/&gt;
     *         &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *         &lt;element name="end" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *         &lt;element name="notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "participant",
        "start",
        "end",
        "notes"
    })
    public static class Call {

        @XmlElement(required = true)
        protected ChatsParticipant participant;
        @XmlElement(required = true, type = String.class)
        @XmlJavaTypeAdapter(Adapter1 .class)
        @XmlSchemaType(name = "dateTime")
        protected Date start;
        @XmlElement(required = true, type = String.class)
        @XmlJavaTypeAdapter(Adapter1 .class)
        @XmlSchemaType(name = "dateTime")
        protected Date end;
        protected String notes;

        /**
         * Gets the value of the participant property.
         * 
         * @return
         *     possible object is
         *     {@link ChatsParticipant }
         *     
         */
        public ChatsParticipant getParticipant() {
            return participant;
        }

        /**
         * Sets the value of the participant property.
         * 
         * @param value
         *     allowed object is
         *     {@link ChatsParticipant }
         *     
         */
        public void setParticipant(ChatsParticipant value) {
            this.participant = value;
        }

        /**
         * Gets the value of the start property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public Date getStart() {
            return start;
        }

        /**
         * Sets the value of the start property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStart(Date value) {
            this.start = value;
        }

        /**
         * Gets the value of the end property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public Date getEnd() {
            return end;
        }

        /**
         * Sets the value of the end property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEnd(Date value) {
            this.end = value;
        }

        /**
         * Gets the value of the notes property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNotes() {
            return notes;
        }

        /**
         * Sets the value of the notes property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNotes(String value) {
            this.notes = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="participant" type="{http://lifelinetasmania.org.au/chats}ChatsParticipant"/&gt;
     *         &lt;element name="indicativeCallTime" type="{http://www.w3.org/2001/XMLSchema}time" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "participant",
        "indicativeCallTime"
    })
    public static class CallAllocation {

        @XmlElement(required = true)
        protected ChatsParticipant participant;
        @XmlSchemaType(name = "time")
        protected XMLGregorianCalendar indicativeCallTime;

        /**
         * Gets the value of the participant property.
         * 
         * @return
         *     possible object is
         *     {@link ChatsParticipant }
         *     
         */
        public ChatsParticipant getParticipant() {
            return participant;
        }

        /**
         * Sets the value of the participant property.
         * 
         * @param value
         *     allowed object is
         *     {@link ChatsParticipant }
         *     
         */
        public void setParticipant(ChatsParticipant value) {
            this.participant = value;
        }

        /**
         * Gets the value of the indicativeCallTime property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getIndicativeCallTime() {
            return indicativeCallTime;
        }

        /**
         * Sets the value of the indicativeCallTime property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setIndicativeCallTime(XMLGregorianCalendar value) {
            this.indicativeCallTime = value;
        }

    }

}
