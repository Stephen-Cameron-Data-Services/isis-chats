//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.01 at 01:11:47 PM EST 
//


package au.com.scds.chats.dom.report.dex.model.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import au.com.scds.chats.dom.report.dex.model.generated.CaseClients;
import au.com.scds.chats.dom.report.dex.model.generated.ParentingAgreementOutcome;
import au.com.scds.chats.dom.report.dex.model.generated.Section60I;


/**
 * <p>Java class for Case complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Case">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CaseId" type="{}NonEmptyString"/>
 *         &lt;element name="OutletActivityId" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;element name="TotalNumberOfUnidentifiedClients" type="{}MaxUnidentifiedClients"/>
 *         &lt;element name="CaseClients" type="{}CaseClients" minOccurs="0"/>
 *         &lt;element name="ParentingAgreementOutcome" type="{}ParentingAgreementOutcome" minOccurs="0"/>
 *         &lt;element name="Section60I" type="{}Section60I" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Case", propOrder = {
    "caseId",
    "outletActivityId",
    "totalNumberOfUnidentifiedClients",
    "caseClients",
    "parentingAgreementOutcome",
    "section60I"
})
public class Case {

    @XmlElement(name = "CaseId", required = true)
    protected String caseId;
    @XmlElement(name = "OutletActivityId")
    @XmlSchemaType(name = "unsignedInt")
    protected long outletActivityId;
    @XmlElement(name = "TotalNumberOfUnidentifiedClients")
    protected int totalNumberOfUnidentifiedClients;
    @XmlElement(name = "CaseClients", nillable = true)
    protected CaseClients caseClients;
    @XmlElement(name = "ParentingAgreementOutcome")
    protected ParentingAgreementOutcome parentingAgreementOutcome;
    @XmlElement(name = "Section60I")
    protected Section60I section60I;

    /**
     * Gets the value of the caseId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaseId() {
        return caseId;
    }

    /**
     * Sets the value of the caseId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaseId(String value) {
        this.caseId = value;
    }

    /**
     * Gets the value of the outletActivityId property.
     * 
     */
    public long getOutletActivityId() {
        return outletActivityId;
    }

    /**
     * Sets the value of the outletActivityId property.
     * 
     */
    public void setOutletActivityId(long value) {
        this.outletActivityId = value;
    }

    /**
     * Gets the value of the totalNumberOfUnidentifiedClients property.
     * 
     */
    public int getTotalNumberOfUnidentifiedClients() {
        return totalNumberOfUnidentifiedClients;
    }

    /**
     * Sets the value of the totalNumberOfUnidentifiedClients property.
     * 
     */
    public void setTotalNumberOfUnidentifiedClients(int value) {
        this.totalNumberOfUnidentifiedClients = value;
    }

    /**
     * Gets the value of the caseClients property.
     * 
     * @return
     *     possible object is
     *     {@link CaseClients }
     *     
     */
    public CaseClients getCaseClients() {
        return caseClients;
    }

    /**
     * Sets the value of the caseClients property.
     * 
     * @param value
     *     allowed object is
     *     {@link CaseClients }
     *     
     */
    public void setCaseClients(CaseClients value) {
        this.caseClients = value;
    }

    /**
     * Gets the value of the parentingAgreementOutcome property.
     * 
     * @return
     *     possible object is
     *     {@link ParentingAgreementOutcome }
     *     
     */
    public ParentingAgreementOutcome getParentingAgreementOutcome() {
        return parentingAgreementOutcome;
    }

    /**
     * Sets the value of the parentingAgreementOutcome property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParentingAgreementOutcome }
     *     
     */
    public void setParentingAgreementOutcome(ParentingAgreementOutcome value) {
        this.parentingAgreementOutcome = value;
    }

    /**
     * Gets the value of the section60I property.
     * 
     * @return
     *     possible object is
     *     {@link Section60I }
     *     
     */
    public Section60I getSection60I() {
        return section60I;
    }

    /**
     * Sets the value of the section60I property.
     * 
     * @param value
     *     allowed object is
     *     {@link Section60I }
     *     
     */
    public void setSection60I(Section60I value) {
        this.section60I = value;
    }

}