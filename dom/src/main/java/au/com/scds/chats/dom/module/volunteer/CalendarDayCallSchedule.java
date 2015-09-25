package au.com.scds.chats.dom.module.volunteer;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEvent;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEventable;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * A manager of ScheduledCall objects for a specific Calendar day, usually for a
 * specific Volunteer .
 * 
 * @author Stephen Cameron Data Services
 * 
 */
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "id")
@Queries({ @Query(name = "find", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.volunteer.CalendarDayCallSchedule "),
		@Query(name = "findByVolunteer", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.volunteer.CalendarDayCallSchedule WHERE allocatedVolunteer == :volunteer ") })
@DomainObject(objectType = "CALENDAR_DAY_CALL_SCHEDULE")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class CalendarDayCallSchedule implements CalendarEventable, Comparable<CalendarDayCallSchedule> {

	public String title() {
		return "Total: " + getTotalCalls() + "; Completed: " + getCompletedCalls();
	}

	// {{ CalendarDay (property)
	private LocalDate calendarDate;

	@Column(allowsNull = "false")
	@MemberOrder(sequence = "1")
	public LocalDate getCalendarDate() {
		return calendarDate;
	}

	public void setCalendarDate(final LocalDate calendarDate) {
		this.calendarDate = calendarDate;
	}

	// }}

	// {{ AllocatedVolunteer (property)
	private Volunteer allocatedVolunteer;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "2")
	public Volunteer getAllocatedVolunteer() {
		return allocatedVolunteer;
	}

	void setAllocatedVolunteer(final Volunteer allocatedVolunteer) {
		this.allocatedVolunteer = allocatedVolunteer;
	}

	// }}

	// {{ TotalCalls (property)
	private Integer totalCalls = 0;

	@Column(allowsNull = "false")
	@MemberOrder(sequence = "3")
	public Integer getTotalCalls() {

		return totalCalls;
	}

	private void setTotalCalls(Integer total) {
		this.totalCalls = total;
	}

	// }}

	// {{ CompletedCalls (property)
	// tracks a count of calls in this schedule for display in calendar
	private Integer completedCalls = 0;

	@Column(allowsNull = "false")
	@MemberOrder(sequence = "4")
	public Integer getCompletedCalls() {
		return completedCalls;
	}

	private void setCompletedCalls(Integer completed) {
		this.completedCalls = completed;
	}

	// }}

	// {{ ScheduledCalls (property)
	private SortedSet<ScheduledCall> scheduledCalls = new TreeSet<>();

	@CollectionLayout(paged = 20, render = RenderType.EAGERLY)
	public SortedSet<ScheduledCall> getScheduledCalls() {
		return scheduledCalls;
	}

	// }}

	// ACTIONS
	public synchronized ScheduledCall scheduleCall(final LocalTime time) throws Exception {
		if (time == null) {
			throw new IllegalArgumentException("time parameter is mandatory");
		}
		ScheduledCall call = callScheduler.createScheduledCall(this, time);
		call.setAllocatedVolunteer(getAllocatedVolunteer());
		scheduledCalls.add(call);
		setTotalCalls(getTotalCalls() + 1);
		if (getTotalCalls() != scheduledCalls.size())
			throw new Exception("Error: total call count and scheduledCalls.size() are different");
		return call;
	}

	public synchronized ScheduledCall completeCall(ScheduledCall call, Boolean isComplete) throws Exception {
		if (call == null)
			return null;
		if (isComplete == null)
			return null;
		if (scheduledCalls.contains(call)) {
			if (!call.getIsCompleted() && isComplete) {
				call.setIsCompletedWithSchedule(this, true);
				setCompletedCalls(getCompletedCalls() + 1);
			} else if (call.getIsCompleted() && !isComplete) {
				call.setIsCompletedWithSchedule(this, false);
				setCompletedCalls(getCompletedCalls() - 1);
			}
		}
		return call;
	}

	public synchronized void removeCall(ScheduledCall call) {
		if (call != null && scheduledCalls.contains(call)) {
			if (call.getIsCompleted()) {
				// TODO show message that call is completed
			} else {
				setTotalCalls(getTotalCalls() - 1);
				scheduledCalls.remove(call);
			}
		}
		return;
	}

	@Override
	@Programmatic
	public String getCalendarName() {
		return "calendarDate";
	}

	@Override
	@Programmatic
	public CalendarEvent toCalendarEvent() {
		// TODO Auto-generated method stub
		return new CalendarEvent(getCalendarDate().toDateTimeAtStartOfDay(), getCalendarName(), title().toString());
	}

	@Override
	public int compareTo(CalendarDayCallSchedule o) {
		return this.getCalendarDate().compareTo(o.getCalendarDate());
	}

	@javax.inject.Inject()
	CallSchedules callScheduler;
}
