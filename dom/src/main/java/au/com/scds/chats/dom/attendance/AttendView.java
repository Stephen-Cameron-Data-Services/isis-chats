package au.com.scds.chats.dom.attendance;

import org.apache.isis.applib.annotation.ViewModel;
import org.joda.time.DateTime;

@ViewModel
// use this to avoid bulk updates appearing
public class AttendView {

	private Attend attend;

	public AttendView() {
	}

	public Attend getAttend() {
		return attend;
	}

	public String title() {
		return getAttend().title();
	}

	public void setAttend(Attend attend) {
		this.attend = attend;
	}

	public DateTime getStartDateTime() {
		return getAttend().getStartDateTime();
	}

	public DateTime getEndDateTime() {
		return getAttend().getEndDateTime();
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
		return getAttend().getRegionName();
	}

}
