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



/**
 * <p>Java class for Assessment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Assessment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ScoreTypeCode" type="{}NonEmptyString"/>
 *         &lt;element name="AssessmentPhaseCode" type="{}NonEmptyString"/>
 *         &lt;element name="Scores">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ScoreCode" type="{}NonEmptyString" maxOccurs="unbounded"/>
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
@XmlType(name = "Assessment", propOrder = {
    "scoreTypeCode",
    "assessmentPhaseCode",
    "scores"
})
public class Assessment {

    @XmlElement(name = "ScoreTypeCode", required = true)
    protected String scoreTypeCode;
    @XmlElement(name = "AssessmentPhaseCode", required = true)
    protected String assessmentPhaseCode;
    @XmlElement(name = "Scores", required = true)
    protected Assessment.Scores scores;

    /**
     * Gets the value of the scoreTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScoreTypeCode() {
        return scoreTypeCode;
    }

    /**
     * Sets the value of the scoreTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScoreTypeCode(String value) {
        this.scoreTypeCode = value;
    }

    /**
     * Gets the value of the assessmentPhaseCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssessmentPhaseCode() {
        return assessmentPhaseCode;
    }

    /**
     * Sets the value of the assessmentPhaseCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssessmentPhaseCode(String value) {
        this.assessmentPhaseCode = value;
    }

    /**
     * Gets the value of the scores property.
     * 
     * @return
     *     possible object is
     *     {@link Assessment.Scores }
     *     
     */
    public Assessment.Scores getScores() {
        return scores;
    }

    /**
     * Sets the value of the scores property.
     * 
     * @param value
     *     allowed object is
     *     {@link Assessment.Scores }
     *     
     */
    public void setScores(Assessment.Scores value) {
        this.scores = value;
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
     *         &lt;element name="ScoreCode" type="{}NonEmptyString" maxOccurs="unbounded"/>
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
        "scoreCode"
    })
    public static class Scores {

        @XmlElement(name = "ScoreCode", required = true)
        protected List<String> scoreCode;

        /**
         * Gets the value of the scoreCode property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the scoreCode property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getScoreCode().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getScoreCode() {
            if (scoreCode == null) {
                scoreCode = new ArrayList<String>();
            }
            return this.scoreCode;
        }

    }

}
