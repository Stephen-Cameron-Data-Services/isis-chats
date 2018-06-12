package au.com.scds.chats.dom.volunteer.calls;

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
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.security.UserMemento;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.user.UserService;
import org.isisaddons.module.security.dom.user.ApplicationUser;
import org.isisaddons.module.security.dom.user.ApplicationUserRepository;

import au.com.scds.chats.dom.call.CalendarDayCallSchedule;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.general.Status;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.VolunteerRoles;

@DomainService(nature=NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(menuOrder = "30")
public class CallsVolunteers {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public Volunteer findVolunteer() {
		return container.firstMatch(new QueryDefault<>(Volunteer.class, "findVolunteerByApplicationUsername", "username", userService.getUser().getName()));
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2")
	public CalendarDayCallSchedule createSchedule(){
		Volunteer volunteer = findVolunteer();
		if(volunteer != null){
			return volunteer.buildSchedule(clockService.now());	
		}else{
			return null;
		}
	}
	
	@Inject
	protected DomainObjectContainer container;
	
	@Inject()
	protected ClockService clockService;

	@Inject
	protected Persons persons;

	@Inject
	protected Participants participantsRepo;

	@Inject
	protected VolunteerRoles volunteerRoles;

	@Inject
	protected Regions regionsRepo;

	@Inject
	protected ApplicationUserRepository userRepository;

	@Inject
	protected UserService userService;
}
