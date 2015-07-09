package au.com.scds.chats.dom.modules.reports;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;

@DomainService(nature = NatureOfService.VIEW)
@DomainServiceLayout(named = "Reports", menuBar = MenuBar.PRIMARY, menuOrder = "40.1")
public class ReportsList {

	// {{ InactiveParticipantsByMonthsInactiveReport (action)
	@MemberOrder(sequence = "10")
	public InactiveParticipantsByMonthsInactive InactiveParticipantsByMonthsInactiveReport() {
		return new InactiveParticipantsByMonthsInactive();
	}

	// }}

	// {{ MailMergeInputDataReport (action)
	@MemberOrder(sequence = "20")
	public MailMergeInputData MailMergeInputDataReport() {
		return new MailMergeInputData();
	}

	// }}

	// {{ ParticipantActivityByMonthReport (action)
	@MemberOrder(sequence = "30")
	public ParticipantActivityByMonth ParticipantActivityByMonthReport() {
		return new ParticipantActivityByMonth();
	}

	// }}

	// {{ ParticipantsByPostcodeReport (action)
	@MemberOrder(sequence = "40")
	public ParticipantsByPostcode ParticipantsByPostcodeReport() {
		return new ParticipantsByPostcode();
	}

	// }}

	// {{ ParticipationRatesReport (action)
	@MemberOrder(sequence = "50")
	public ParticipationRates ParticipationRatesReport() {
		return new ParticipationRates();
	}

	// }}

	// {{ TripAttendeeListReport (action)
	@MemberOrder(sequence = "60")
	public TripAttendeeList TripAttendeeListReport() {
		return new TripAttendeeList();
	}

	// }}

	// {{ VolunteerActivityByMonthReport (action)
	@MemberOrder(sequence = "70")
	public VolunteerActivityByMonth VolunteerActivityByMonthReport() {
		return new VolunteerActivityByMonth();
	}
	// }}

}
