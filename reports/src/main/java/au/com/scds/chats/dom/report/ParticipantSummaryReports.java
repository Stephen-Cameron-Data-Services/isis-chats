package au.com.scds.chats.dom.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;
import au.com.scds.chats.dom.participant.AgeGroup;
import au.com.scds.chats.dom.report.view.ParticipantCallOrAttendance;
import au.com.scds.chats.dom.report.view.ParticipantCallOrAttendanceSummary;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(menuBar = MenuBar.PRIMARY, named = "Reports", menuOrder = "70.0")
public class ParticipantSummaryReports {

	public List<ParticipantCallOrAttendance> participantInvolvementInPeriod(LocalDate startDate, LocalDate endDate,
			AgeGroup ageGroup) {

		switch (ageGroup) {
		case All:
			return container.allMatches(
					new QueryDefault<>(ParticipantCallOrAttendance.class, "allParticipantCallOrAttendanceForPeriod",
							"startDate", startDate.toDate(), "endDate", endDate.toDate()));
		case Under_Sixty_Five:
			return container.allMatches(new QueryDefault<>(ParticipantCallOrAttendance.class,
					"allParticipantCallOrAttendanceForPeriodAgedUnder", "startDate", startDate.toDate(), "endDate",
					endDate.toDate(), "lessThanAge", 65));
		case Sixty_Five_and_Over:
			return container.allMatches(new QueryDefault<>(ParticipantCallOrAttendance.class,
					"allParticipantCallOrAttendanceForPeriodAgedOver", "startDate", startDate.toDate(), "endDate",
					endDate.toDate(), "greaterThanAge", 64));
		default:
			return null;
		}
	}

	public Collection<ParticipantCallOrAttendanceSummary> participantInvolvementInPeriodSummary(LocalDate startDate,
			LocalDate endDate, AgeGroup ageGroup) {
		List<ParticipantCallOrAttendance> list;
		Map<String, ParticipantCallOrAttendanceSummary> sums = new HashMap<>();
		switch (ageGroup) {
		case All:
			list = container.allMatches(
					new QueryDefault<>(ParticipantCallOrAttendance.class, "allParticipantCallOrAttendanceForPeriod",
							"startDate", startDate.toDate(), "endDate", endDate.toDate()));
			break;
		case Under_Sixty_Five:
			list = container.allMatches(new QueryDefault<>(ParticipantCallOrAttendance.class,
					"allParticipantCallOrAttendanceForPeriodAgedUnder", "startDate", startDate.toDate(), "endDate",
					endDate.toDate(), "lessThanAge", 65));
			break;
		case Sixty_Five_and_Over:
			list = container.allMatches(new QueryDefault<>(ParticipantCallOrAttendance.class,
					"allParticipantCallOrAttendanceForPeriodAgedOver", "startDate", startDate.toDate(), "endDate",
					endDate.toDate(), "greaterThanAge", 64));
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
				key = time.getParticipantId().toString() + time.getAge().toString();
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
}
