package au.com.scds.chats.dom.general;

import static org.assertj.core.api.Assertions.assertThat;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;
import org.isisaddons.wicket.gmap3.cpt.service.LocationLookupService;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import au.com.scds.chats.dom.general.Address;
import au.com.scds.chats.dom.general.Location;
import au.com.scds.chats.dom.general.Locations;

public class LocationTest {
	
	@Rule
	public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

	@Mock
	DomainObjectContainer mockContainer;
	LocationLookupService locationLookupService;
	Location location;
	Locations locations;
	Address address;

	@Before
	public void setUp() throws Exception {
		locationLookupService = new LocationLookupService();
		locations = new Locations(mockContainer,locationLookupService);
		address = new Address(locations);
	}

	public static class LocationTest_Tests extends LocationTest {

		@Test
		public void createLocationFromAddress() throws Exception {

			address.setName("Headquarters");
			address.setStreet1("66 Corinth Street");
			address.setSuburb("Howrah");
			address.setPostcode("7018");
			address.updateGeocodedLocation();
			assertThat(address.getLatitude()).isEqualTo(-42.8867495);
			assertThat(address.getLongitude()).isEqualTo(147.4088664);
			//is Location being return for display in Gmap.
			assertThat(address.getLocation()).isNotEqualTo(null);
			assertThat(address.getLocation().getLatitude()).isEqualTo(address.getLatitude());
			assertThat(address.getLocation().getLongitude()).isEqualTo(address.getLongitude());
		}

	}
}
