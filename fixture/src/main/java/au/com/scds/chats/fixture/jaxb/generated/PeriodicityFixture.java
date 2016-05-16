
package au.com.scds.chats.fixture.jaxb.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PeriodicityFixture.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PeriodicityFixture">
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
@XmlType(name = "PeriodicityFixture")
@XmlEnum
public enum PeriodicityFixture {

    WEEKLY,
    FORTNIGHTLY,
    MONTHLY,
    BIMONTHLY;

    public String value() {
        return name();
    }

    public static PeriodicityFixture fromValue(String v) {
        return valueOf(v);
    }

}
