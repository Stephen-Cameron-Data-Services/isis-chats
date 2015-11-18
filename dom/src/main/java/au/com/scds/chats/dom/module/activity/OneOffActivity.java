package au.com.scds.chats.dom.module.activity;

import java.util.List;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Programmatic;
import org.incode.module.note.dom.api.notable.Notable;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEvent;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEventable;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.module.call.CalendarDayCallSchedule;
import au.com.scds.chats.dom.module.participant.Participants;
import au.com.scds.chats.dom.module.volunteer.Volunteer;
import au.com.scds.chats.dom.module.volunteer.VolunteeredTimeForActivity;
import au.com.scds.chats.dom.module.volunteer.VolunteeredTimeForCalls;

/**
 * OneOffEvents are individual Activities that appear on a calendar.
 * 
 * There activities are the same as ActivityEvents but have no parent Recurring
 * Activity.
 * 
 * Note that participation and attendance are handled separately, the later via
 * Attended records.
 * 
 */
@DomainObject(objectType = "ONE-OFF_ACTIVITY")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Location", "Admin" })
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "ONEOFF")
@Queries({
		@Query(name = "findOneOffActivityByName", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.activity.OneOffActivity WHERE name.indexOf(:name) >= 0 ") })
// @Unique(name = "Activity_name_UNQ", members = { "name"
// })
public class OneOffActivity extends Activity implements Notable, CalendarEventable {

	@Override
	public String title() {
		return "One-off Activity: " + getName();
	}

	public OneOffActivity() {
		super();
	}

	// for mock testing
	public OneOffActivity(DomainObjectContainer container, Participants participants) {
		super();
		this.container = container;
		this.participantsRepo = participants;
	}

	@Programmatic
	public String getCalendarName() {
		return "One-off Activity";
	}

	@Programmatic
	public CalendarEvent toCalendarEvent() {
		return new CalendarEvent(getStartDateTime().withTimeAtStartOfDay(), getCalendarName(), title());
	}

	@Action()
	@ActionLayout()
	@MemberOrder(name = "volunteeredtime", sequence = "1")
	public OneOffActivity addVolunteeredTime(Volunteer volunteer, DateTime startDateTime, DateTime endDateTime) {
		VolunteeredTimeForActivity time = volunteersRepo.createVolunteeredTimeForActivity(volunteer, this, startDateTime, endDateTime);
		addVolunteeredTime(time);
		return this;
	}
	
	public List<Volunteer> choices0AddVolunteeredTime(){
		return volunteersRepo.listActive();
	}

}
