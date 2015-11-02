package au.com.scds.chats.dom.module.call;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participants;
import au.com.scds.chats.dom.module.volunteer.Volunteer;
import au.com.scds.chats.dom.module.volunteer.Volunteers;

/**
 * 
 * @author Steve Cameron Data Services
 * 
 */
@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = CalendarDayCallSchedule.class)
@DomainServiceLayout(named="Calls", menuOrder = "50") 
public class CallSchedules {

	/*@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "1")
	public List<CalendarDayCallSchedule> listDailyCallSchedules() {
		return container.allInstances(CalendarDayCallSchedule.class);
	}*/

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "2")
	public List<CalendarDayCallSchedule> listDailyCallSchedulesForVolunteer(final Volunteer volunteer) {
		return container.allMatches(new QueryDefault<>(CalendarDayCallSchedule.class, "findByVolunteer", "volunteer", volunteer));
	}
	
	public List<Volunteer> choices0ListDailyCallSchedulesForVolunteer(){
		return volunteers.listAll();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "1")
	public List<CalendarDayCallSchedule> listDailyCallSchedulesForActiveVolunteer(@Parameter(optionality = Optionality.MANDATORY) final Volunteer volunteer) {
		return container.allMatches(new QueryDefault<>(CalendarDayCallSchedule.class, "findByVolunteer", "volunteer", volunteer));
	}
	
	public List<Volunteer> choices0ListDailyCallSchedulesForActiveVolunteer(){
		return volunteers.listActive();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "3")
	public List<ScheduledCall> listCallsToActiveParticipant(final Participant participant) {
		return container.allMatches(new QueryDefault<>(ScheduledCall.class, "findScheduledCallsByParticipant", "participant", participant));
	}

	public List<Participant> choices0ListCallsToActiveParticipant(){
		return participants.listActive();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "4")
	public List<ScheduledCall> listCallsToParticipant(final Participant participant) {
		return container.allMatches(new QueryDefault<>(ScheduledCall.class, "findScheduledCallsByParticipant", "participant", participant));
	}

	public List<Participant> choices0ListCallsToParticipant(){
		return participants.listAll();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "5")
	public List<ScheduledCall> listCallsByVolunteer(final Volunteer volunteer) {
		return container.allMatches(new QueryDefault<>(ScheduledCall.class, "findScheduledCallsByVolunteer", "volunteer", volunteer));
	}
	
	public List<Volunteer> choices0ListCallsByVolunteer(){
		return volunteers.listAll();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "6")
	public List<ScheduledCall> listCallsToParticipantByVolunteer(final Participant participant, final Volunteer volunteer) {
		return container.allMatches(new QueryDefault<>(ScheduledCall.class, "findScheduledCallsByParticipantAndVolunteer", "participant", participant, "volunteer", volunteer));
	}
	
	public List<Participant> choices0ListCallsToParticipantByVolunteer(){
		return participants.listAll();
	}
	
	public List<Volunteer> choices1ListCallsToParticipantByVolunteer(){
		return volunteers.listAll();
	}

	@Programmatic
	public CalendarDayCallSchedule createCalendarDayCallSchedule(final @Parameter(optionality = Optionality.MANDATORY) LocalDate date, final Volunteer volunteer) {
		CalendarDayCallSchedule schedule = container.newTransientInstance(CalendarDayCallSchedule.class);
		schedule.setCalendarDate(date);
		schedule.setAllocatedVolunteer(volunteer);
		container.persistIfNotAlready(schedule);
		container.flush();
		return schedule;
	}

	@Programmatic
	public ScheduledCall createScheduledCall(final Volunteer volunteer, final Participant participant, final DateTime dateTime) throws Exception {
		// see if there is a Schedule for this Volunteer on this day
		if (dateTime == null) {
			throw new IllegalArgumentException("dateTime is a mandatory argument");
		}
		CalendarDayCallSchedule sched = null;
		if (volunteer != null) {
			List<CalendarDayCallSchedule> schedules = listDailyCallSchedulesForVolunteer(volunteer);
			for (CalendarDayCallSchedule s : schedules) {
				if (s.getCalendarDate().equals(dateTime.toLocalDate())) {
					sched = s;
					break;
				}
			}
		}else{
			throw new IllegalArgumentException("volunteer is a mandatory argument");
		}
		if (sched == null) {
			sched = createCalendarDayCallSchedule(dateTime.toLocalDate(), volunteer);
		}
		// add a new call
		// TODO should an exception be trapped here?
		ScheduledCall call = sched.scheduleCall(dateTime.toLocalTime());
		call.setParticipant(participant);
		call.setIsCompleted(false);
		return call;
	}

	@Programmatic
	// Gets called by the CalendarDayCallSchedule to actually create a
	// ScheduledCall
	// This is done to allow maintenance of the aggregate counts in the Schedule
	ScheduledCall createScheduledCall(CalendarDayCallSchedule callSchedule, LocalTime time) {
		ScheduledCall call = container.newTransientInstance(ScheduledCall.class);
		call.setCallSchedule(callSchedule);
		call.setScheduledDateTime(callSchedule.getCalendarDate().toDateTime(time));
		container.persistIfNotAlready(call);
		container.flush();
		return call;
	}

	@Inject
	public DomainObjectContainer container;
	
	@Inject
	public Volunteers volunteers;
	
	@Inject
	public Participants participants;
}
