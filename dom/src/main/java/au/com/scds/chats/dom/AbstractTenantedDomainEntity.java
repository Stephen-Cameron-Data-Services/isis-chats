package au.com.scds.chats.dom;

import java.net.URL;
import java.util.List;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.WithApplicationTenancy;
import org.isisaddons.module.security.dom.user.ApplicationUser;

import au.com.scds.chats.dom.module.general.names.Region;
import au.com.scds.chats.dom.module.general.names.Regions;

/**
 * Adds Tenanting to AbstractDomainEntity based on region
 * 
 */
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class AbstractTenantedDomainEntity extends AbstractDomainEntity implements WithApplicationTenancy {

	protected Region region;

	@Property()
	@PropertyLayout(hidden = Where.EVERYWHERE)
	@MemberOrder(sequence = "12")
	@Column(allowsNull = "true")
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

	public ApplicationTenancy getApplicationTenancy() {
		ApplicationTenancy tenancy = new ApplicationTenancy();
		tenancy.setPath("/" + getRegion());
		return tenancy;
	}

	/*@Override
	public void created() {
		super.created();
		if (applicationUserRepository != null) {
			ApplicationUser user = applicationUserRepository.findUserByUsername(container.getUser().getName());
			if (user != null) {
				System.out.println("preStore5");
				String tenancyPath = user.getTenancy().getPath();
				if (tenancyPath.startsWith("/"))
					tenancyPath = tenancyPath.substring(1);
				Region region = regions.regionForName(tenancyPath);
				setRegion(region);
			}
		}
	}*/

	@javax.inject.Inject
	protected Regions regions;

}
