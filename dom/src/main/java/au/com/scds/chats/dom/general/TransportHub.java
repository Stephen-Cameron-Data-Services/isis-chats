package au.com.scds.chats.dom.general;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.util.TitleBuffer;

import au.com.scds.eventschedule.base.impl.Address;

@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(strategy = DiscriminatorStrategy.VALUE_MAP, value = "TRANSPORT_HUB")
@Queries({
		@Query(name = "findTransportHubByName", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.general.TransportHub WHERE name == :name"),
		@Query(name = "findAllTransportHubs", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.general.TransportHub"),
		@Query(name = "findAllNamedTransportHubs", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.general.TransportHub WHERE name != null && name.trim().length() > 0 ORDER BY name") })
@DomainObject()
public class TransportHub extends Address {

	@Override
	public String title() {
		final TitleBuffer buf = new TitleBuffer();
		if (getName() != null) {
			buf.append(getName());
			buf.append(",", getStreet1());
		} else {
			buf.append(getStreet1());
		}
		buf.append(",", getStreet2());
		buf.append(",", getSuburb());
		buf.append(",", getPostcode());
		return buf.toString();
	}
}
