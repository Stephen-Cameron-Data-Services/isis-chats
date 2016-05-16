
package au.com.scds.chats.fixture.jaxb.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StatusFixture.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StatusFixture">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ACTIVE"/>
 *     &lt;enumeration value="INACTIVE"/>
 *     &lt;enumeration value="TO_EXIT"/>
 *     &lt;enumeration value="EXITED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StatusFixture")
@XmlEnum
public enum StatusFixture {

    ACTIVE,
    INACTIVE,
    TO_EXIT,
    EXITED;

    public String value() {
        return name();
    }

    public static StatusFixture fromValue(String v) {
        return valueOf(v);
    }

}
