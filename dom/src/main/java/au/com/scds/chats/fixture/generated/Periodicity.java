//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.09 at 02:37:01 PM AEST 
//


package au.com.scds.chats.fixture.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Periodicity.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Periodicity"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="WEEKLY"/&gt;
 *     &lt;enumeration value="FORTNIGHTLY"/&gt;
 *     &lt;enumeration value="MONTHLY"/&gt;
 *     &lt;enumeration value="BIMONTHLY"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Periodicity")
@XmlEnum
public enum Periodicity {

    WEEKLY,
    FORTNIGHTLY,
    MONTHLY,
    BIMONTHLY;

    public String value() {
        return name();
    }

    public static Periodicity fromValue(String v) {
        return valueOf(v);
    }

}
