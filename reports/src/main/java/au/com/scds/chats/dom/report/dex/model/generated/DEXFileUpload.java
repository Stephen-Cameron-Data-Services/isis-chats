//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.01 at 01:11:47 PM EST 
//

package au.com.scds.chats.dom.report.dex.model.generated;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import au.com.scds.chats.dom.report.dex.model.generated.Cases;
import au.com.scds.chats.dom.report.dex.model.generated.ClientAssessments;
import au.com.scds.chats.dom.report.dex.model.generated.Clients;
import au.com.scds.chats.dom.report.dex.model.generated.Outlets;
import au.com.scds.chats.dom.report.dex.model.generated.SessionAssessments;
import au.com.scds.chats.dom.report.dex.model.generated.Sessions;

/**
 * <p>
 * Java class for DEXFileUpload complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="DEXFileUpload">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="5">
 *         &lt;element name="Clients" type="{}Clients"/>
 *         &lt;element name="Cases" type="{}Cases"/>
 *         &lt;element name="Sessions" type="{}Sessions"/>
 *         &lt;element name="SessionAssessments" type="{}SessionAssessments"/>
 *         &lt;element name="ClientAssessments" type="{}ClientAssessments"/>
 *         &lt;element name="Outlets" type="{}Outlets"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name = "DEXFileUpload")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DEXFileUpload", propOrder = { "clientsOrCasesOrSessions" })
public class DEXFileUpload {

	@XmlElements({ @XmlElement(name = "Clients", type = Clients.class), @XmlElement(name = "Cases", type = Cases.class),
			@XmlElement(name = "Sessions", type = Sessions.class),
			@XmlElement(name = "SessionAssessments", type = SessionAssessments.class),
			@XmlElement(name = "ClientAssessments", type = ClientAssessments.class),
			@XmlElement(name = "Outlets", type = Outlets.class) })
	protected List<Object> clientsOrCasesOrSessions;

	/**
	 * Gets the value of the clientsOrCasesOrSessions property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the clientsOrCasesOrSessions property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getClientsOrCasesOrSessions().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Clients }
	 * {@link Cases } {@link Sessions } {@link SessionAssessments }
	 * {@link ClientAssessments } {@link Outlets }
	 * 
	 * 
	 */
	public List<Object> getClientsOrCasesOrSessions() {
		if (clientsOrCasesOrSessions == null) {
			clientsOrCasesOrSessions = new ArrayList<Object>();
		}
		return this.clientsOrCasesOrSessions;
	}

}
