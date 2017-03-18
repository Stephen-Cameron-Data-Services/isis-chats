package au.com.scds.chats.dom.report;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.report.view.MailMergeData;

import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(menuBar = MenuBar.PRIMARY, named = "Reports", menuOrder = "70.90")
public class ParticipantBirthdays {

	@Action
	public List<MailMergeData> findParticipantsWithBirthdayBetween(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Period Start Date") LocalDate periodStart,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Period End Date") LocalDate periodEnd) {
		if (periodStart == null || periodEnd == null)
			return null;
		// reset start day to the day before the period of interest
		periodStart = periodStart.minusDays(1);
		List<MailMergeData> actives = container
				.allMatches(new QueryDefault<>(MailMergeData.class, "listActiveParticipantMailMergeData"));
		List<MailMergeData> valids = new ArrayList<>();
		for (MailMergeData p : actives) {
			if (Person.getAgeAtDate(p.getBirthDate(), periodStart) < Person.getAgeAtDate(p.getBirthDate(), periodEnd)) {
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
	DomainObjectContainer container;

}
