/*
 *
 *  Copyright 2015 Stephen Cameron Data Services
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package au.com.scds.chats.dom;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.incode.module.note.dom.api.notable.Notable;
import org.incode.module.note.dom.spi.calendarname.CalendarNameRepository;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEventable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.activity.RecurringActivity;
import au.com.scds.chats.dom.call.ScheduledCall;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.volunteer.Volunteer;

@DomainService(nature = NatureOfService.DOMAIN)
public class CalendarNameRepositoryForChats implements CalendarNameRepository {

	private static final Map<Class<?>, List<String>> namesByClass = Maps.newHashMap();

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
	
	public static String calendarNameFor(CalendarEventable eventable){
		List<String> names = namesByClass.get(eventable.getClass());
		return (names != null && names.get(0) != null) ? names.get(0) : null ;
	}
}