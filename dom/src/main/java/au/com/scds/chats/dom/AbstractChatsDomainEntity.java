package au.com.scds.chats.dom;

import javax.inject.Inject;

import javax.jdo.annotations.*;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.timestamp.Timestampable;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.WithApplicationTenancy;
import org.isisaddons.module.security.dom.user.ApplicationUser;
import org.isisaddons.module.security.dom.user.ApplicationUserRepository;

import org.joda.time.DateTime;

import au.com.scds.chats.dom.module.general.names.Region;
import au.com.scds.chats.dom.module.general.names.Regions;

/**
 * Base class of the Chats Entity Inheritance Tree.
 * 
 * Has the 'Admin' section properties.
 * 
 */
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class AbstractChatsDomainEntity implements Timestampable, WithApplicationTenancy {

	private String createdBy;
	private DateTime createdOn;
	private String lastModifiedBy;
	private DateTime lastModifiedOn;
	private Region region;

	@Property(editing = Editing.DISABLED, hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Created by")
	@MemberOrder(name = "Admin", sequence = "1")
	@Column(allowsNull = "true")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
	}

	@Property(editing = Editing.DISABLED, hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Created On")
	@MemberOrder(name = "Admin", sequence = "2")
	@Column(allowsNull = "true")
	public DateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(final DateTime createdOn) {
		this.createdOn = createdOn;
	}

	@Property(editing = Editing.DISABLED, hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Modified By")
	@MemberOrder(name = "Admin", sequence = "3")
	@Column(allowsNull = "true")
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Property(editing = Editing.DISABLED, hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Last Modified")
	@MemberOrder(name = "Admin", sequence = "4")
	@Column(allowsNull = "true")
	public DateTime getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(DateTime lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	@Property(editing = Editing.DISABLED, hidden = Where.EVERYWHERE)
	// @PropertyLayout(named = "Region")
	// @MemberOrder(name = "Admin", sequence = "5")
	@Column(allowsNull = "false")
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Region")
	@MemberOrder(name = "Admin", sequence = "100")
	@NotPersistent
	public String getRegionName() {
		if (getRegion() == null)
			return null;
		else
			return getRegion().getName();
	}

	@Programmatic
	public void setUpdatedBy(String updatedBy) {
		if (getCreatedBy() == null) {
			setCreatedBy(updatedBy);
			if (userRepository != null) {
				ApplicationUser user = userRepository.findByUsername(updatedBy);
				if (user != null && user.getTenancy() != null) {
					String path = user.getTenancy().getPath();
					String name = path.substring(path.lastIndexOf("/") + 1);
					if (name.isEmpty())
						name = "GLOBAL";
					setRegion(regions.regionForName(name));
				}else
					setRegion(regions.regionForName("GLOBAL")); //TODO temp fix
			}
			else
				setRegion(regions.regionForName("GLOBAL")); //TODO temp fix
		} else
			setLastModifiedBy(updatedBy);
	}

	@Programmatic
	public void setUpdatedAt(java.sql.Timestamp updatedAt) {
		if (getCreatedOn() == null)
			setCreatedOn(new DateTime(updatedAt));
		else
			setLastModifiedOn(new DateTime(updatedAt));
	}

	@Programmatic
	public ApplicationTenancy getApplicationTenancy() {
		ApplicationTenancy tenancy = new ApplicationTenancy();
		if (getRegion() == null || getRegion().equals("GLOBAL"))
			tenancy.setPath("/");
		else {
			tenancy.setPath("/" + getRegion().getName());
		}
		return tenancy;
	}

	@Inject
	protected DomainObjectContainer container;

	@Inject
	protected ClockService clockService;

	@Inject
	protected ApplicationUserRepository userRepository;

	@Inject
	protected Regions regions;

}
