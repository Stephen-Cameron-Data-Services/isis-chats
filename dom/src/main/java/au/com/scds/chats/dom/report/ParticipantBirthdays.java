package au.com.scds.chats.dom.report;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.general.ChatsPerson;
import au.com.scds.chats.report.view.MailMergeData;

import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(objectType="ParticipantBirthdays", nature = NatureOfService.VIEW_MENU_ONLY)
public class ParticipantBirthdays {

	@Action
	public List<MailMergeData> findParticipantsWithBirthdayBetween(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Period Start Date") LocalDate periodStart,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Period End Date") LocalDate periodEnd) {
		if (periodStart == null || periodEnd == null)
			return null;
		// reset start day to the day before the period of interest
		periodStart = periodStart.minusDays(1);
		List<MailMergeData> actives = repositoryService
				.allMatches(new QueryDefault<>(MailMergeData.class, "listActiveParticipantMailMergeData"));
		List<MailMergeData> valids = new ArrayList<>();
		for (MailMergeData p : actives) {
			if (ChatsPerson.getAgeAtDate(p.getBirthDate(), periodStart) < ChatsPerson.getAgeAtDate(p.getBirthDate(), periodEnd)) {
				valids.add(p);
			}
		}
		return valids;
	}

	public String validateFindParticipantsWithBirthdayBetween(LocalDate periodStart, LocalDate periodEnd) {
		if (periodStart.isBefore(periodEnd) || periodStart.equals(periodEnd)) {
			return null;
		} else {
			return "Period Start is after Period End";
		}
	}

	@Inject
	RepositoryService repositoryService;

}
