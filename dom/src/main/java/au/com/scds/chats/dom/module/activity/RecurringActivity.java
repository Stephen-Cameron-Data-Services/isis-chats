package au.com.scds.chats.dom.module.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.*;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.module.general.Location;
import au.com.scds.chats.dom.module.general.names.ActivityType;
import au.com.scds.chats.dom.module.general.names.Region;
import au.com.scds.chats.dom.module.note.NoteLinkable;
import au.com.scds.chats.dom.module.participant.Participants;
import au.com.scds.chats.dom.module.participant.Participations;

/**
 * 
 * An <code>Activity</code> that is used to schedule individual
 * <code>ActivityEvent</code> objects.
 * 
 * The term 'activity' is used in this domain in both a generic and specific
 * way, the term 'event' is not used but is more descriptive of what is
 * occurring, that is a series of calendar events make up a recurring activity.
 * 
 * @author stevec
 * 
 */
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Queries({ @Query(name = "find", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.activity.RecurringActivity "),
		@Query(name = "findRecurringActivityByName", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.activity.RecurringActivity WHERE name.indexOf(:name) >= 0 ") })
@MemberGroupLayout(columnSpans = { 3, 3, 0, 6 }, left = { "General" }, middle = { "Scheduling", "Admin" })
@DomainObject(objectType = "RECURRING_ACTIVITY")
public class RecurringActivity extends Activity implements NoteLinkable {

	public RecurringActivity() {
		super();
	}
	
	//use for testing only
	public RecurringActivity(DomainObjectContainer container, Participants participantsRepo, Participations participationsRepo) {
		super(container,participantsRepo,participationsRepo,null,null,null);
	}

	@Override
	public String title() {
		return "Recurring Activity: " + getName();
	}

	private List<ActivityEvent> children;

	private Periodicity periodicity = Periodicity.WEEKLY;

	@Column(allowsNull = "true")
	@MemberOrder(name = "Scheduling", sequence = "1")
	public Periodicity getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(final Periodicity periodicity) {
		this.periodicity = periodicity;
	}

	// add parent start date time to Scheduling
	@MemberOrder(name = "Scheduling", sequence = "2")
	@Override
	public DateTime getStartDateTime() {
		return this.startDateTime;
	}

	/*
	 * @MemberOrder(name = "startDateTime", sequence = "1")
	 * 
	 * @ActionLayout(named = "Schedule Events") public void
	 * scheduleEvents(@ParameterLayout(named = "Schedule to:") Date endDate) {
	 * 
	 * }
	 */

	// endregion

	// region > ActivityEvents (collection)
	/**
	 * The collection of scheduled {@link ActivityEvent} objects associated with
	 * this {@link RecurringActivity}
	 */
	@Persistent(mappedBy = "parent")
	private SortedSet<ActivityEvent> events = new TreeSet<ActivityEvent>();

	@CollectionLayout(hidden = Where.EVERYWHERE)
	SortedSet<ActivityEvent> getActivityEvents() {
		return events;
	}

	void setActivityEvents(final SortedSet<ActivityEvent> activityEvents) {
		this.events = activityEvents;
	}

	/**
	 * Provides a list of currently scheduled activities sorted soonest to
	 * latest
	 * 
	 * @return
	 */
	@MemberOrder(sequence = "10")
	@CollectionLayout(render = RenderType.EAGERLY)
	public List<ActivityEvent> getFutureActivities() {
		ArrayList<ActivityEvent> temp = new ArrayList<>();
		for (ActivityEvent event : getActivityEvents()) {
			if (event.getStartDateTime().isAfterNow()) {
				temp.add(event);
			}
		}
		Collections.reverse(temp);
		return temp;
	}

	@MemberOrder(sequence = "20")
	public List<ActivityEvent> getCompletedActivities() {
		ArrayList<ActivityEvent> temp = new ArrayList<>();
		for (ActivityEvent event : getActivityEvents()) {
			if (event.getStartDateTime().isBeforeNow()) {
				temp.add(event);
			}
		}
		return temp;
	}

	@MemberOrder(name = "startdatetime", sequence = "1")
	@ActionLayout(named = "Add Next Event")
	public RecurringActivity addNextScheduledActivity() {
		ActivityEvent obj = container.newTransientInstance(ActivityEvent.class);
		obj.setParentActivity(this);
		DateTime last = (events.size() > 0) ? events.first().getStartDateTime() : new DateTime();
		obj.setStartDateTime(last.plus(getPeriodicity().getDuration()));
		events.add(obj);
		container.persistIfNotAlready(obj);
		container.flush();
		return this;
	}

	/*
	 * public ActivityEvent getLastScheduledActivity() { // events are sorted by
	 * date, most future to most past List<ActivityEvent> events =
	 * getFutureActivities(); if (events.size() > 0) { return
	 * events.get(events.size() - 1); } else { return null; } }
	 * 
	 * public ActivityEvent getFirstScheduledActivity() { List<ActivityEvent>
	 * events = getFutureActivities(); if (events.size() > 0) { return
	 * events.get(0); } else { return null; } }
	 */
}
