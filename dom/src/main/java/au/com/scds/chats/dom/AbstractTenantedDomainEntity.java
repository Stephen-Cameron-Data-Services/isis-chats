package au.com.scds.chats.dom;

import java.net.URL;
import java.util.List;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.WithApplicationTenancy;

import au.com.scds.chats.dom.module.general.names.Region;
import au.com.scds.chats.dom.module.general.names.Regions;

/**
 * Adds Tenanting to AbstractDomainEntity based on region
 * 
 * @author steve cameron
 * 
 */
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class AbstractTenantedDomainEntity extends AbstractDomainEntity implements WithApplicationTenancy {

	protected Region region;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "12")
	@PropertyLayout(hidden = Where.EVERYWHERE)
	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public List<Region> choicesRegion() {
		return regions.listAllRegions();
	}

	@MemberOrder(sequence = "7")
	@PropertyLayout(hidden = Where.ALL_TABLES)
	@NotPersistent
	public String getRegionName() {
		return regions.nameForRegion(getRegion());
	}

	public void setRegionName(String name) {
		setRegion(regions.regionForName(name));
	}

	public List<String> choicesRegionName() {
		return regions.allNames();
	}
	
	public ApplicationTenancy getApplicationTenancy(){
		ApplicationTenancy tenancy = new ApplicationTenancy();
		tenancy.setPath("/"+getRegion());
		return tenancy;
	}
	
	@javax.inject.Inject
	protected Regions regions;

}
