package au.com.scds.chats.dom;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.incode.module.note.dom.api.notable.Notable;
import org.incode.module.note.dom.spi.calendarname.CalendarNameRepository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import au.com.scds.chats.dom.module.activity.ActivityEvent;
import au.com.scds.chats.dom.module.activity.RecurringActivity;
import au.com.scds.chats.dom.module.call.ScheduledCall;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.volunteer.Volunteer;

@DomainService(nature = NatureOfService.DOMAIN)
public class CalendarNameRepositoryForChats implements CalendarNameRepository {

	private final Map<Class<?>, List<String>> namesByClass = Maps.newHashMap();

	public CalendarNameRepositoryForChats() {
		setCalendarNames(RecurringActivity.class, "Recurring Activities");
		setCalendarNames(ActivityEvent.class, "Activities");
		setCalendarNames(Participant.class, "Participants");
		setCalendarNames(Volunteer.class, "Volunteers");
		setCalendarNames(ScheduledCall.class, "Scheduled Calls");
	}

	@Programmatic
	public void setCalendarNames(final Class<?> cls, final String... names) {
		namesByClass.put(cls, Lists.newArrayList(names));
	}

	@Override
	public Collection<String> calendarNamesFor(Notable notable) {
		return namesByClass.get(notable.getClass());
	}
}