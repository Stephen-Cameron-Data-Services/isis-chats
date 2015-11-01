package au.com.scds.chats.dom;

import javax.inject.Inject;
import javax.jdo.annotations.*;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.clock.ClockService;
//import org.apache.isis.applib.services.timestamp.Timestampable;
import org.isisaddons.module.security.dom.user.ApplicationUsers;
import org.joda.time.DateTime;

/**
 * Base class of the Chats Entity Inheritance Tree.
 * 
 * Has the 'Admin' section properties.
 * 
 */
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class AbstractDomainEntity /*implements Timestampable*/ {

	private String createdBy;
	private DateTime createdOn;
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

	public void setUpdatedBy(String updatedBy) {
		if (createdBy == null)
			setCreatedBy(updatedBy);
		else
			setLastModifiedBy(updatedBy);
	}

	public void setUpdatedAt(java.sql.Timestamp updatedAt) {
		if (createdOn == null)
			setCreatedOn(new DateTime(updatedAt));
		else
			setLastModifiedOn(new DateTime(updatedAt));
	}

	@Inject
	protected DomainObjectContainer container;

	@Inject
	protected ClockService clockService;

	@Inject
	protected ApplicationUsers applicationUserRepository;

}
