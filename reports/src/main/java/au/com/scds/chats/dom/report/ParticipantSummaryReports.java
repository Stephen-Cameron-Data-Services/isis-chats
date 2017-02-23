package au.com.scds.chats.dom.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;
import au.com.scds.chats.dom.participant.AgeGroup;
import au.com.scds.chats.dom.participant.ParticipantIdentity;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.report.view.ParticipantCallOrAttendance;
import au.com.scds.chats.dom.report.viewmodel.ParticipantCallOrAttendanceSummary;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(menuBar = MenuBar.PRIMARY, named = "Reports", menuOrder = "70.0")
public class ParticipantSummaryReports {

	@Action()
	@ActionLayout(named="Find Participant's Calls And Attendances")
	public List<ParticipantCallOrAttendance> findParticipantsCallsAndAttendances(
			@ParameterLayout(named = "Participant") ParticipantIdentity identity,
			@ParameterLayout(named = "Start Date") LocalDate startDate,
			@ParameterLayout(named = "End Date") LocalDate endDate) {
		return container.allMatches(
				new QueryDefault<>(ParticipantCallOrAttendance.class, "allCallOrAttendanceForParticipantInPeriod",
						"participantId", identity.getParticipantId(), "startDate", startDate.toDate(), "endDate", endDate.plusDays(1).toDate()));
	}

	public List<ParticipantIdentity> choices0FindParticipantsCallsAndAttendances() {
		return participantsRepo.listAllParticipantIdentities();
	}

	@Action()
	public List<ParticipantCallOrAttendance> listParticipantInvolvementInPeriod(
			@ParameterLayout(named = "Start Date") LocalDate startDate,
			@ParameterLayout(named = "End Date") LocalDate endDate,
			@ParameterLayout(named = "Age Group") AgeGroup ageGroup) {

		switch (ageGroup) {
		case All:
			return container.allMatches(
					new QueryDefault<>(ParticipantCallOrAttendance.class, "allParticipantCallOrAttendanceInPeriod",
							"startDate", startDate.toDate(), "endDate", endDate.plusDays(1).toDate()));
		case Under_Sixty_Five:
			return container.allMatches(new QueryDefault<>(ParticipantCallOrAttendance.class,
					"allParticipantCallOrAttendanceInPeriodAgedUnder", "startDate", startDate.toDate(), "endDate",
					endDate.plusDays(1).toDate(), "lessThanAge", 65));
		case Sixty_Five_and_Over:
			return container.allMatches(new QueryDefault<>(ParticipantCallOrAttendance.class,
					"allParticipantCallOrAttendanceInPeriodAgedOver", "startDate", startDate.toDate(), "endDate",
					endDate.plusDays(1).toDate(), "greaterThanAge", 64));
		default:
			return null;
		}
	}

	@Action()
	public Collection<ParticipantCallOrAttendanceSummary> listParticipantInvolvementInPeriodTotals(
			@ParameterLayout(named = "Start Date") LocalDate startDate,
			@ParameterLayout(named = "End Date") LocalDate endDate,
			@ParameterLayout(named = "Age Group") AgeGroup ageGroup) {
		List<ParticipantCallOrAttendance> list;
		Map<String, ParticipantCallOrAttendanceSummary> sums = new HashMap<>();
		switch (ageGroup) {
		case All:
			list = container.allMatches(
					new QueryDefault<>(ParticipantCallOrAttendance.class, "allParticipantCallOrAttendanceInPeriod",
							"startDate", startDate.toDate(), "endDate", endDate.plusDays(1).toDate()));
			break;
		case Under_Sixty_Five:
			list = container.allMatches(new QueryDefault<>(ParticipantCallOrAttendance.class,
					"allParticipantCallOrAttendanceInPeriodAgedUnder", "startDate", startDate.toDate(), "endDate",
					endDate.plusDays(1).toDate(), "lessThanAge", 65));
			break;
		case Sixty_Five_and_Over:
			list = container.allMatches(new QueryDefault<>(ParticipantCallOrAttendance.class,
					"allParticipantCallOrAttendanceInPeriodAgedOver", "startDate", startDate.toDate(), "endDate",
					endDate.plusDays(1).toDate(), "greaterThanAge", 64));
			break;
		default:
			list = new ArrayList<>();
		}
		// loop through the records and create a summary by person
		// but adjusting for transport
		String key = null;
		ParticipantCallOrAttendanceSummary summary;
		for (ParticipantCallOrAttendance time : list) {
			if (time.getMinutes() != null) {
				// make the key the participant_id and age class, will all
				// selected there may be some that turn 65 in the period.
				key = time.getParticipantId().toString() + ((time.getAge() > 64) ? ">64" : "<65");
				if (!sums.containsKey(key)) {
					summary = new ParticipantCallOrAttendanceSummary(time);
					sums.put(key, summary);
				} else {
					summary = sums.get(key);
				}
				summary.addTime(time);
			}
		}
		return sums.values();
	}

	@Inject
	DomainObjectContainer container;

	@Inject
	Participants participantsRepo;
}
