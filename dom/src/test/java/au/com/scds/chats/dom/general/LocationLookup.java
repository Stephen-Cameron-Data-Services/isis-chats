package au.com.scds.chats.dom.general;


import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

import org.isisaddons.wicket.gmap3.cpt.service.LocationLookupService;
import org.junit.Before;
import org.junit.Test;

import org.isisaddons.wicket.gmap3.cpt.applib.Location;

public class LocationLookup {

	private LocationLookupService locationLookupService;
	
	@Before
	public void setup() {
		locationLookupService = new LocationLookupService();
	}
	
	@Test
	public void whenValid() {
		Location location = locationLookupService.lookup("66 Corinth Street, Howrah, Australia");
		assertThat(location, is(not(nullValue())));
		assertEquals(51.503, location.getLatitude(), 0.01);
		assertEquals(-0.128, location.getLongitude(), 0.01);
	}

	@Test
	public void whenInvalid() {
		Location location = locationLookupService.lookup("$%$%^Y%^fgnsdlfk glfg");
		assertThat(location, is(nullValue()));
	}
}
