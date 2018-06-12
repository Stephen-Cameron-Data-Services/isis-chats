package au.com.scds.chats.dom.admin;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.InvokeOn;
import org.apache.isis.applib.annotation.ViewModel;
import au.com.scds.chats.dom.general.Status;
import au.com.scds.chats.dom.volunteer.Volunteer;

@ViewModel
public class VolunteerExiter {

	private Volunteer volunteer;
	
	public String title(){
		return getVolunteer().title();
	}

	public Volunteer getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(Volunteer volunteer) {
		this.volunteer = volunteer;
	}

	public String getVolunteerName() {
		return getVolunteer().getFullName();
	}

	public Status getVolunteerStatus() {
		return getVolunteer().getStatus();
	}

	public String getRegionName() {
		return getVolunteer().getRegionName();
	}

	@Action(invokeOn=InvokeOn.OBJECT_AND_COLLECTION)
	public void changeToExited() {
		if (getVolunteer().getStatus() == Status.TO_EXIT)
			getVolunteer().setStatus(Status.EXITED);
		return;
	}
}
