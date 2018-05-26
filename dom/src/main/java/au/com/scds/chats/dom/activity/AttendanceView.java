package au.com.scds.chats.dom.activity;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.ViewModel;
import org.joda.time.DateTime;

@DomainObject(objectType = "chats.attendview", nature = Nature.VIEW_MODEL)
public class AttendanceView {

	private ChatsAttendance attend;

	public AttendanceView() {
	}

	public ChatsAttendance getAttend() {
		return attend;
	}

	public String title() {
		return getAttend().title();
	}

	public void setAttend(ChatsAttendance attend) {
		this.attend = attend;
	}

	public DateTime getStartDateTime() {
		return getAttend().getStart();
	}

	public DateTime getEndDateTime() {
		return getAttend().getEnd();
	}

	public String getIntervalLength() {
		return getAttend().getIntervalLengthFormatted();
	}

	public String getParticipantName() {
		return getAttend().getParticipantName();
	}

	public String getActivityName() {
		return getAttend().getActivityName();
	}

	public String getWasAttended() {
		return getAttend().getWasAttended();
	}

	public String getArrivingTransportType() {
		return getAttend().getArrivingTransportTypeName();
	}

	public String getDepartingTransportType() {
		return getAttend().getDepartingTransportTypeName();
	}

	public String getRegionName() {
		return getAttend().getRegion().getName();
	}

}