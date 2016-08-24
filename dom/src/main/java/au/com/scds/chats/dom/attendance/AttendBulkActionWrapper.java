package au.com.scds.chats.dom.attendance;

import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.NotPersistent;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.InvokeOn;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.actinvoc.ActionInvocationContext;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.general.names.TransportType;
import au.com.scds.chats.dom.general.names.TransportTypes;

@DomainObject(nature = Nature.VIEW_MODEL)
public class AttendBulkActionWrapper {

	private Attend attend;

	public AttendBulkActionWrapper() {
	}

	public AttendBulkActionWrapper(Attend attend) {
		this.attend = attend;
	}

	public String title() {
		return getAttend().title();
	}

	public Attend getAttend() {
		return attend;
	}

	public void setAttend(Attend attend) {
		this.attend = attend;
	}

	@Property()
	public String getParticipantName() {
		return getAttend().getParticipantName();
	}

	@Property(editing = Editing.DISABLED)
	public String getWasAttended() {
		return (getAttend().getAttended() ? "YES" : "NO");
	}

	@Property()
	public DateTime getStartDateTime() {
		return getAttend().getStartDateTime();
	}

	@Property()
	public DateTime getEndDateTime() {
		return getAttend().getEndDateTime();
	}

	@Property(editing = Editing.DISABLED)
	public String getAttendanceInterval() {
		return getAttend().getAttendanceInterval();
	}

	@Property(editing = Editing.DISABLED)
	public String getArrivingTransportType() {
		return getAttend().getArrivingTransportTypeName();
	}

	@Property(editing = Editing.DISABLED)
	public String getDepartingTransportType() {
		return getAttend().getDepartingTransportTypeName();
	}

	//TODO doesn't show as bulk action unless zero-arg
	@Action(invokeOn = InvokeOn.OBJECT_AND_COLLECTION)
	public AttendBulkActionWrapper changeTransportTypes(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Arriving Transport Type") String arriving,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Departing Transport Type") String departing) {
		getAttend().setArrivingTransportTypeName(arriving);
		getAttend().setDepartingTransportTypeName(departing);
		return actionInvocationContext.getInvokedOn().isCollection() ? null : this;
	}
	
	public String default0ChangeTransportTypes() {
		return getAttend().getArrivingTransportTypeName();
	}

	public String default1ChangeTransportTypes() {
		return getAttend().getDepartingTransportTypeName();
	}

	public List<String> choices0ChangeTransportTypes() {
		return transportTypes.allNames();
	}

	public List<String> choices1ChangeTransportTypes() {
		return transportTypes.allNames();
	}

	@Action(invokeOn = InvokeOn.OBJECT_AND_COLLECTION)
	public AttendBulkActionWrapper wasAttended() {
		getAttend().wasAttended();
		return actionInvocationContext.getInvokedOn().isCollection() ? null : this;
	}

	@Action(invokeOn = InvokeOn.OBJECT_AND_COLLECTION)
	public AttendBulkActionWrapper wasNotAttended() {
		getAttend().wasNotAttended();
		return actionInvocationContext.getInvokedOn().isCollection() ? null : this;
	}

	//TODO doesn't show as bulk action unless zero-arg
	@Action(invokeOn = InvokeOn.OBJECT_AND_COLLECTION)
	public AttendBulkActionWrapper changeDatesAndTimes(@ParameterLayout(named = "Start Date Time") DateTime start,
			@ParameterLayout(named = "End Date Time") DateTime end) {
		getAttend().updateDatesAndTimes(start, end);
		return actionInvocationContext.getInvokedOn().isCollection() ? null : this;
	}

	public DateTime default0ChangeDatesAndTimes() {
		return getAttend().getStartDateTime();
	}

	public DateTime default1ChangeDatesAndTimes() {
		return getAttend().getEndDateTime();
	}

	@Inject
	ActionInvocationContext actionInvocationContext;

	@Inject
	TransportTypes transportTypes;
}
