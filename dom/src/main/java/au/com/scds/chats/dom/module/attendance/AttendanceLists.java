package au.com.scds.chats.dom.module.attendance;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Column;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

import au.com.scds.chats.dom.module.activity.Activities;
import au.com.scds.chats.dom.module.activity.ActivityEvent;
import au.com.scds.chats.dom.module.participant.Participant;

@DomainService(repositoryFor = AttendanceList.class, nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(named = "Attendances", menuOrder = "40")
public class AttendanceLists {

	@Action()
	@ActionLayout(named = "Create Attendance List")
	@MemberOrder(sequence = "10")
	public AttendanceList createActivityAttendanceList(@ParameterLayout(named = "Activity") final ActivityEvent activityEvent) {
		AttendanceList attendanceList = container.newTransientInstance(AttendanceList.class);
		attendanceList.setParentActivity(activityEvent);
		container.persistIfNotAlready(attendanceList);
		container.flush();
		activityEvent.setAttendances(attendanceList);
		return attendanceList;
	}

	public List<ActivityEvent> choices0CreateActivityAttendanceList() {
		// TODO why not working, null pointer exception?
		return container.allMatches(new QueryDefault<>(ActivityEvent.class, "findActivitiesWithoutAttendanceList"));
		/*
		 * List<ActivityEvent> activities =
		 * container.allInstances(ActivityEvent.class); List<ActivityEvent> temp
		 * = new ArrayList<ActivityEvent>(); for (ActivityEvent e : activities)
		 * { if (e.getAttendances() == null) { temp.add(e); } } return temp;
		 */
	}

	@Programmatic
	public Attended createAttended(final ActivityEvent activity, final Participant participant, final Boolean attended) {
		if(activity == null || participant == null)
			return null;
		Attended attendance = container.newTransientInstance(Attended.class);
		attendance.setActivity(activity);
		attendance.setParticipant(participant);		
		attendance.setAttended(attended);
		container.persistIfNotAlready(attendance);
		container.flush();
		return attendance;
	}
	
	@Programmatic
	public void deleteAttended(Attended attended) {
		if(attended!= null)
			container.removeIfNotAlready(attended);
	}

	@Action()
	@MemberOrder(sequence = "20")
	@CollectionLayout(paged = 20)
	public List<AttendanceList> listAttendanceLists() {
		return container.allInstances(AttendanceList.class);
	}

	@javax.inject.Inject
	Activities activitiesRepo;

	@javax.inject.Inject
	DomainObjectContainer container;



}
