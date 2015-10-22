package au.com.scds.chats.dom;

import javax.inject.Inject;
import javax.jdo.InstanceCallbacks;
import javax.jdo.annotations.*;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.isisaddons.module.security.dom.user.ApplicationUser;
import org.isisaddons.module.security.dom.user.ApplicationUsers;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.module.general.names.Region;
import au.com.scds.chats.dom.module.general.names.Regions;
import au.com.scds.chats.dom.user.CreateTrackedEntity;
import au.com.scds.chats.dom.user.ModifyTrackedEntity;

/**
 * Base class of the Chats Entity Inheritance Tree.
 * 
 * Has the 'Admin' section properties.
 * 
 */
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class AbstractDomainEntity /*
											 * implements InstanceCallbacks,
											 * CreateTrackedEntity,
											 * ModifyTrackedEntity
											 */{

	private String createdBy;
	private DateTime createdOn;
	private DateTime deletedOn;
	private String lastModifiedBy;
	private DateTime lastModifiedOn;

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
	@PropertyLayout(named = "Deleted On")
	@MemberOrder(name = "Admin", sequence = "3")
	@Column(allowsNull = "true")
	public DateTime getDeletedOn() {
		return deletedOn;
	}

	public void setDeletedOn(final DateTime deletedOn) {
		this.deletedOn = deletedOn;
	}

	@Property(editing = Editing.DISABLED, hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Modified By")
	@MemberOrder(name = "Admin", sequence = "4")
	@Column(allowsNull = "true")
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Property(editing = Editing.DISABLED, hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Last Modified")
	@MemberOrder(name = "Admin", sequence = "5")
	@Column(allowsNull = "true")
	public DateTime getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(DateTime lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	/*
	 * @Programmatic public void jdoPostLoad() { System.out.println("postLoad");
	 * }
	 * 
	 * @Programmatic public void jdoPreClear() { System.out.println("preClear");
	 * }
	 * 
	 * @Programmatic public void jdoPreStore() { System.out.println("preStore");
	 * }
	 * 
	 * @Programmatic public void jdoPreDelete() {
	 * System.out.println("preDelete"); }
	 */

	@Programmatic
	public void created() {
		setCreatedBy(container.getUser().getName());
		setCreatedOn(clockService.nowAsDateTime());
	}

	/*
	 * @Programmatic public void updating(){ System.out.println(">>>>updating");
	 * setLastModifiedBy(container.getUser().getName());
	 * setLastModifiedOn(clockService.nowAsDateTime()); }
	 */

	@Inject
	protected DomainObjectContainer container;

	@Inject
	protected ClockService clockService;

	@Inject
	protected ApplicationUsers applicationUserRepository;

}
