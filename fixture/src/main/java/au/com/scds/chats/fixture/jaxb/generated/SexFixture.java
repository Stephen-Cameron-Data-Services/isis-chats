
package au.com.scds.chats.fixture.jaxb.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SexFixture.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SexFixture">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MALE"/>
 *     &lt;enumeration value="FEMALE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SexFixture")
@XmlEnum
public enum SexFixture {

    MALE,
    FEMALE;

    public String value() {
        return name();
    }

    public static SexFixture fromValue(String v) {
        return valueOf(v);
    }

}
