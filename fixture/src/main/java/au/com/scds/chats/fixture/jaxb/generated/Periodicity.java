//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.09 at 02:07:25 PM AEDT 
//


package au.com.scds.chats.fixture.jaxb.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Periodicity.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Periodicity">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="WEEKLY"/>
 *     &lt;enumeration value="FORTNIGHTLY"/>
 *     &lt;enumeration value="MONTHLY"/>
 *     &lt;enumeration value="BIMONTHLY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
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
