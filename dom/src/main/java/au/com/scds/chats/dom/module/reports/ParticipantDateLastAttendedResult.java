package au.com.scds.chats.dom.module.reports;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ViewModel;
import org.joda.time.DateTime;

//JDOQL Query Result Class
@ViewModel
public class ParticipantDateLastAttendedResult {
	
	ParticipantDateLastAttendedResult(String name, DateTime date){
		this.participantName = name;
		this.dateLastAttended = date;
	}
	
	// {{ ParticipantName (property)
	private String participantName;

	@MemberOrder(sequence = "1")
	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(final String participantName) {
		this.participantName = participantName;
	}
	// }}
	
	// {{ DaysSinceActive (property)
	private DateTime dateLastAttended;

	@MemberOrder(sequence = "2")
	public DateTime getDaysSinceActive() {
		return dateLastAttended;
	}

	public void setDaysSinceActive(final DateTime daysSinceActive) {
		this.dateLastAttended = daysSinceActive;
	}
	// }}
}
