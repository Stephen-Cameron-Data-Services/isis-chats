package au.com.scds.chats.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.joda.time.LocalDate;

import au.com.scds.chats.report.view.ActivityVolunteerVolunteeredTime;
import au.com.scds.chats.report.viewmodel.ActivityVolunteerVolunteeredTimeSummary;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.VolunteersMenu;

//Reports
@DomainService(objectType="VolunteerSummaryReports", nature = NatureOfService.VIEW_MENU_ONLY)
public class VolunteerSummaryReports {

	@Action()
	public List<ActivityVolunteerVolunteeredTime> findVolunteersActivityTimes(
			@ParameterLayout(named = "Volunteer") Volunteer identity,
			@ParameterLayout(named = "Start Date") LocalDate startDate,
			@ParameterLayout(named = "End Date") LocalDate endDate) {
		return repositoryService.allMatches(
				new QueryDefault<>(ActivityVolunteerVolunteeredTime.class, "allActivityVolunteerVolunteeredTimeForPeriod",
						"volunteerId", identity, "startDateTime", startDate.toDate(), "endDateTime", endDate.plusDays(1).toDate()));
	}

	public List<Volunteer> autoComplete0FindVolunteersActivityTimes(@MinLength(3) String search) {
		return volunteersRepo.listActiveVolunteers(search);
	}

	@Action()
	public List<ActivityVolunteerVolunteeredTime> listVolunteerInvolvementInPeriod(
			@ParameterLayout(named = "Start Date") LocalDate startDate,
			@ParameterLayout(named = "End Date") LocalDate endDate) {
		return repositoryService.allMatches(
				new QueryDefault<>(ActivityVolunteerVolunteeredTime.class, "allActivityVolunteeredTimeForPeriod",
						 "startDateTime", startDate.toDate(), "endDateTime", endDate.plusDays(1).toDate()));
	}

	@Action()
	public Collection<ActivityVolunteerVolunteeredTimeSummary> listVolunteerInvolvementInPeriodTotals(
			@ParameterLayout(named = "Start Date") LocalDate startDate,
			@ParameterLayout(named = "End Date") LocalDate endDate) {
		List<ActivityVolunteerVolunteeredTime> list = listVolunteerInvolvementInPeriod(startDate, endDate);
		Map<String, ActivityVolunteerVolunteeredTimeSummary> sums = new HashMap<>();
		// loop through the records and create a summary by person
		// but adjusting for transport
		String key = null;
		ActivityVolunteerVolunteeredTimeSummary summary;
		for (ActivityVolunteerVolunteeredTime time : list) {
			if (time.getMinutes() != null) {
				key = time.getVolunteerId().toString();
				if (!sums.containsKey(key)) {
					summary = new ActivityVolunteerVolunteeredTimeSummary(time);
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
	RepositoryService repositoryService;

	@Inject
	VolunteersMenu volunteersRepo;
}