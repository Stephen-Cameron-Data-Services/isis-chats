package au.com.scds.chats.dom.module.volunteer;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;

import au.com.scds.chats.dom.module.activity.Activity;
import au.com.scds.chats.dom.module.call.CalendarDayCallSchedule;

@DomainObject(objectType = "VTIMEFORCALLS")
@DomainObjectLayout(bookmarking = BookmarkPolicy.NEVER)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = {  "Admin" })
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "CALLS")
public class VolunteeredTimeForCalls extends VolunteeredTime {
	
	private CalendarDayCallSchedule callSchedule;

	@Property()
	@PropertyLayout(hidden=Where.ALL_TABLES)
	@MemberOrder(sequence = "2")
	@Column(allowsNull="false")
	public CalendarDayCallSchedule getCallSchedule() {
		return callSchedule;
	}

	public void setCallSchedule(CalendarDayCallSchedule callSchedule) {
		this.callSchedule = callSchedule;
	}

}
