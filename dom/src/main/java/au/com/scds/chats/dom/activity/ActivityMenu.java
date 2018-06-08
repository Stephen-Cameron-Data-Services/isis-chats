/*
 *
 *  Copyright 2015 Stephen Cameron Data Services
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
package au.com.scds.chats.dom.activity;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.security.UserMemento;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.user.UserService;
import org.isisaddons.module.security.dom.user.ApplicationUser;
import org.isisaddons.module.security.dom.user.ApplicationUserRepository;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import au.com.scds.chats.dom.ChatsDomainEntitiesService;
import au.com.scds.chats.dom.ChatsEntity;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.eventschedule.base.impl.activity.ActivityEvent;

@DomainService(nature=NatureOfService.VIEW_MENU_ONLY, 
objectType = "ChatsActivityMenu", repositoryFor = ActivityEvent.class)
public class ActivityMenu {

	public ActivityMenu() {
	}

	@Action(semantics = SemanticsOf.SAFE)
	public ChatsRecurringActivity createChatsRecurringActivity(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Activity name") final String name,
			@Parameter(optionality = Optionality.OPTIONAL, maxLength = 25, regexPattern = "^[\\p{IsAlphabetic}\\p{IsDigit}]+$", regexPatternReplacement = "Must be Alpha-Numeric") @ParameterLayout(named = "DEX 'Case' Name") final String abbreviatedName,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Start date time") final DateTime startDateTime) {
		ActivityEvent activity = findActivity(name, startDateTime);
		if (activity != null) {
			messageService.informUser("Activity with same Name, Start Date Time and Region exists");
			if (activity instanceof ChatsRecurringActivity) {
				return ((ChatsRecurringActivity) activity);
			} else {
				return null;
			}
		}
		if (findActivity(name, startDateTime) != null) {
			messageService.informUser("Activity with same Name, Start Date Time and Region exists");
			return null;
		}
		final ChatsRecurringActivity obj = new ChatsRecurringActivity(null, name, "Recurring", startDateTime, null);
		serviceRegistry.injectServicesInto(obj);
		obj.setName(name);
		if (abbreviatedName != null) {
			obj.setCodeName(abbreviatedName);
		} else {
			obj.setCodeName(name.replaceAll("[^\\p{Alnum}]", ""));
			if (obj.getCodeName().length() > 25)
				obj.setCodeName(obj.getCodeName().substring(0, 25));
		}
		repositoryService.persist(obj);
		return obj;
	}

	@Programmatic
	public ChatsRecurringActivity createChatsRecurringActivity(final String name, final DateTime startDateTime,
			final Region region) {
		
		final ChatsRecurringActivity obj = new ChatsRecurringActivity(null, name, "Recurring", startDateTime, null);
		serviceRegistry.injectServicesInto(obj);
		obj.setRegion(region);
		repositoryService.persist(obj);
		return obj;
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<ChatsRecurringActivity> listAllRecurringActivities() {
		return repositoryService.allInstances(ChatsRecurringActivity.class);
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<ChatsRecurringActivity> findChatsRecurringActivityByName(@ParameterLayout(named = "Name") final String name) {
		return repositoryService
				.allMatches(new QueryDefault<>(ChatsRecurringActivity.class, "findRecurringActivityByName", "name", name));
	}

	@Action(semantics = SemanticsOf.SAFE)
	public ChatsActivity createOneOffActivity(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Activity name") final String name,
			@Parameter(optionality = Optionality.OPTIONAL, maxLength = 25, regexPattern = "^[\\p{IsAlphabetic}\\p{IsDigit}]+$", regexPatternReplacement = "Must be Alpha-Numeric") @ParameterLayout(named = "DEX 'Case' Id") final String abbreviatedName,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Start date time") final DateTime startDateTime) {
		ActivityEvent activity = findActivity(name, startDateTime);
		if (activity != null) {
			messageService.informUser("Activity with same Name, Start Date Time and Region exists");
			if (activity instanceof ChatsActivity) {
				return ((ChatsActivity) activity);
			} else {
				return null;
			}
		}
		// create the new ActivityEvent
		final ChatsActivity obj = new ChatsActivity(null, name, "Recurring", startDateTime, null);
		serviceRegistry.injectServicesInto(obj);
		obj.setName(name);
		if (abbreviatedName != null) {
			obj.setCodeName(abbreviatedName);
		} else {
			obj.setCodeName(name.replaceAll("[^\\p{Alnum}]", ""));
			if (obj.getCodeName().length() > 25)
				obj.setCodeName(obj.getCodeName().substring(0, 25));
		}
		repositoryService.persist(obj);
		return obj;
	}

	// find any kind of Activity
	@Programmatic
	public ActivityEvent findActivity(String name, DateTime startDateTime) {
		// find the region of the current user
		if (userService != null) {
			UserMemento user = userService.getUser();
			Region region = null;
			if (user != null) {
				ApplicationUser appUser = userRepository.findByUsername(user.getName());
				if (appUser != null) {
					String regionName = chatsEntities.regionNameOfApplicationUser(appUser);
					region = regionsRepo.regionForName(regionName);
				}
			}
			// see if there is already an existing Activity
			List<ActivityEvent> activities = repositoryService.allMatches(new QueryDefault<>(ActivityEvent.class,
					"findActivityByUpperCaseName", "name", name.trim().toUpperCase()));
			for (ActivityEvent activity : activities) {
				if (activity.getStart().getDayOfYear() == startDateTime.getDayOfYear()
						&& ((ChatsEntity) activity).getRegion().equals(region)) {
					return activity;
				}
			}
		}
		return null;
	}

	// find a specifically ParentedActivity
	@Programmatic
	public ChatsParentedActivity findParentedActivity(String name, DateTime startDateTime) {
		// find the region of the current user
		UserMemento user = userService.getUser();
		Region region = null;
		if (user != null) {
			ApplicationUser appUser = userRepository.findByUsername(user.getName());
			if (appUser != null) {
				String regionName = chatsEntities.regionNameOfApplicationUser(appUser);
				region = regionsRepo.regionForName(regionName);
			}
		}
		// see if there is already an existing ChatsParentedActivity
		List<ChatsParentedActivity> activities = repositoryService.allMatches(new QueryDefault<>(ChatsParentedActivity.class,
				"findParentedActivityByUpperCaseName", "name", name.trim().toUpperCase()));
		for (ChatsParentedActivity activity : activities) {
			if (activity.getStart().equals(startDateTime) && activity.getRegion().equals(region)) {
				return activity;
			}
		}
		return null;
	}

	@Programmatic
	public ChatsParentedActivity createParentedActivity(ChatsRecurringActivity recurringActivity, final DateTime startDateTime,
			final DateTime endDateTime) {
		if (recurringActivity == null || startDateTime == null) {
			return null;
		}
		if (findParentedActivity(recurringActivity.getName(), startDateTime) != null) {
			messageService.informUser("Parented Activity with same Name, Start Date Time and Region exists");
			return null;
		}
		final ChatsParentedActivity obj = new ChatsParentedActivity(recurringActivity, recurringActivity.getName(), "Recurring", startDateTime, null);
		serviceRegistry.injectServicesInto(obj);
		if (recurringActivity.getCodeName() != null) {
			obj.setCodeName(recurringActivity.getCodeName());
		} else {
			obj.setCodeName(recurringActivity.getName().replaceAll("\\s", ""));
		}
		if (endDateTime != null) {
			obj.updateEndDateTime(endDateTime);
		}
		repositoryService.persist(obj);
		return obj;
	}
	
	@Programmatic
	public ChatsAttendance createAttendance(ActivityEvent event, ChatsParticipant participant) {
		ChatsAttendance attendance = new ChatsAttendance(event, participant);
		serviceRegistry.injectServicesInto(attendance);
		repositoryService.persistAndFlush(attendance);
		return attendance;
	}


	@Action(semantics = SemanticsOf.SAFE)
	public List<ActivityEvent> findActivityByName(@ParameterLayout(named = "Name") final String name) {
		return repositoryService.allMatches(new QueryDefault<>(ActivityEvent.class, "findActivityByUpperCaseName", "name", name.toUpperCase()));
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<ActivityEvent> listAllFutureActivities() {
		return repositoryService.allMatches(
				new QueryDefault<>(ActivityEvent.class, "findActivitiesAfter", "date", new DateTime()));
	}

	@Programmatic
	public List<ActivityEvent> listAllPastActivities() {
		return repositoryService.allMatches(
				new QueryDefault<>(ActivityEvent.class, "findActivitiesBefore", "date", new DateTime()));
	}

	@Programmatic
	public List<ActivityEvent> allActivities() {
		return repositoryService.allMatches(new QueryDefault<>(ActivityEvent.class, "findActivities"));
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<ActivityEvent> listActivitiesInPeriod(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Start Period") LocalDate start,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "End Period") LocalDate end) {
		return repositoryService.allMatches(new QueryDefault<>(ActivityEvent.class, "findActivitiesBetween", "start",
				start.toDateTimeAtStartOfDay(), "end", end.toDateTime(new LocalTime(23, 59))));
	}

	public String validateListActivitiesInPeriod(LocalDate start, LocalDate end) {
		if (end.isBefore(start))
			return "End Date is before Start Date";
		else
			return null;
	}

	@Inject
	protected Regions regionsRepo;

	@Inject
	protected ApplicationUserRepository userRepository;
	
	@Inject
	protected ChatsDomainEntitiesService chatsEntities;

	@Inject
	protected UserService userService;

	@Inject
	protected RepositoryService repositoryService;
	
	@Inject
	protected ServiceRegistry2 serviceRegistry;
	
	@Inject
	protected MessageService messageService;
}
