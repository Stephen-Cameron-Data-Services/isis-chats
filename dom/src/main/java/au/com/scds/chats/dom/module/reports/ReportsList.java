package au.com.scds.chats.dom.module.reports;

import java.util.List;

import javax.inject.Inject;
import javax.jdo.Query;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

import au.com.scds.chats.dom.module.activity.RecurringActivity;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(named = "Reports", menuBar = MenuBar.PRIMARY, menuOrder = "70")
public class ReportsList {

	// {{ InactiveParticipantsByMonthsInactiveReport (action)
	@MemberOrder(sequence = "10")
	public List<ParticipantDateLastAttendedResult> ParticipantsByMonthsInactiveReport(@Parameter(optionality = Optionality.OPTIONAL) RecurringActivity activity) {
		Query query = isisJdoSupport.getJdoPersistenceManager().newQuery(
				"SELECT participant.fullname, max(activity.startDateTime) " + " FROM au.com.scds.chats.dom.module.attendance.Attended "
						+ " INTO au.com.scds.chats.dom.module.reports.ParticipantDateLastAttendedResult " + " GROUP BY participant " + " ORDER BY activity.startDateTime ");
		List<ParticipantDateLastAttendedResult> results = (List<ParticipantDateLastAttendedResult>) query.execute();
		return results;
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

	@Inject
	private IsisJdoSupport isisJdoSupport;

}
