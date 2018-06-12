package au.com.scds.chats.dom.scratch;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;
import org.isisaddons.wicket.gmap3.cpt.service.LocationLookupService;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
public class Place implements Locatable{
	
    //region > location

    private Double latitude;
	private Double longitude;
    
	@Property()
	@Column(allowsNull="true")
    public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double atitude) {
		this.latitude = atitude;
	}

	@Property()
	@Column(allowsNull="true")
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double ongitude) {
		this.longitude = ongitude;
	}


    @Property(
            optionality = Optionality.OPTIONAL
    )
    @MemberOrder(sequence="3")
    public Location getLocation() {
        return getLatitude() != null && getLongitude() != null? new Location(getLatitude(), getLongitude()): null;
    }
    public void setLocation(final Location location) {
        setLongitude(location != null ? location.getLongitude() : null);
        setLatitude(location != null ? location.getLatitude() : null);
    }

    @MemberOrder(name="location", sequence="1")
    public Place updateLocation(
            @ParameterLayout(named="Address") final String address) {
        final Location location = this.lookupService.lookup(address);
        setLocation(location);
        return this;
    }	
	@Inject
	private LocationLookupService lookupService;

}
