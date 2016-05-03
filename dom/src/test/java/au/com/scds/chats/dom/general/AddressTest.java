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

import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.general.Address;
import au.com.scds.chats.dom.general.Location;
import au.com.scds.chats.dom.general.Locations;

public class AddressTest {
	
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

	public static class AddressTest_Tests extends LocationTest {

		@Test
		public void createLocationFromAddress() throws Exception {

			address.setName("Headquarters");
			address.setStreet1("66 Corinth Street");
			address.setSuburb("Howrah");
			address.setPostcode("7018");
			address.updateGeocodedLocation();
			assertThat(address.getLatitude()).isEqualTo(-42.886763);
			assertThat(address.getLongitude()).isEqualTo(147.408854);
			//is Location being return for display in Gmap.
			assertThat(address.getLocation()).isNotEqualTo(null);
			assertThat(address.getLocation().getLatitude()).isEqualTo(address.getLatitude());
			assertThat(address.getLocation().getLongitude()).isEqualTo(address.getLongitude());
		}
		
		@Test
		public void updateActivityAddress(){
				
				address.setName("Lifeline");
				address.setStreet1("Level 5");
				address.setStreet2("31 Cambridge Road");
				address.setSuburb("Bellerive");
				address.setPostcode("7018");
				try{
					address.updateGeocodedLocation();
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
				ActivityEvent activity = new ActivityEvent();
				activity.setAddress(address);
				assertThat(activity.getAddressLocationName()).isEqualTo("Lifeline");
				assertThat(activity.getFullAddress()).isEqualTo("Level 5, 31 Cambridge Road, Bellerive, 7018");
				assertThat(activity.getLocation()).isNotNull();
				assertThat(activity.getLocation().getLatitude()).isEqualTo(-42.87481);
				assertThat(activity.getLocation().getLongitude()).isEqualTo(147.3687);
		}
		
		@Test
		public void updateActivityLocation(){
				
				address.setName("Lifeline");
				try{
					address.updateGeocodedLocation();
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
				ActivityEvent activity = new ActivityEvent();
				activity.setAddress(address);
				assertThat(activity.getAddressLocationName()).isEqualTo("Lifeline");
				assertThat(activity.getFullAddress()).isEqualTo("");
		}

	}
}
