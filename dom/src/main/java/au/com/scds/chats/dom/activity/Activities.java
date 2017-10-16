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

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;

/**
 * <user>
 * <p>
 * Container for <link>Activity<link> domain entities.
 * </p>
 * </user>
 * 
 * @author stevec
 *
 */
@DomainService(objectType = "chats.activities", repositoryFor = ActivityEvent.class)
@DomainServiceLayout(named = "Activities", menuOrder = "10")
public class Activities {

	public Activities() {
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public RecurringActivity createRecurringActivity(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Activity name") final String name,
			@Parameter(optionality = Optionality.OPTIONAL, maxLength = 25, regexPattern = "^[\\p{IsAlphabetic}\\p{IsDigit}]+$", regexPatternReplacement = "Must be Alpha-Numeric") @ParameterLayout(named = "DEX 'Case' Name") final String abbreviatedName,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Start date time") final DateTime startDateTime) {
		Activity a = findActivity(name, startDateTime);
		if (a != null) {
			messageService.informUser("Activity with same Name, Start Date Time and Region exists");
			if (a instanceof RecurringActivity) {
				return ((RecurringActivity) a);
			} else {
				return null;
			}
		}
		if (findActivity(name, startDateTime) != null) {
			messageService.informUser("Activity with same Name, Start Date Time and Region exists");
			return null;
		}
		final RecurringActivity obj = new RecurringActivity();
		serviceRegistry.injectServicesInto(obj);
		obj.setName(name);
		if (abbreviatedName != null) {
			obj.setAbbreviatedName(abbreviatedName);
		} else {
			obj.setAbbreviatedName(name.replaceAll("[^\\p{Alnum}]", ""));
			if (obj.getAbbreviatedName().length() > 25)
				obj.setAbbreviatedName(obj.getAbbreviatedName().substring(0, 25));
		}
		obj.setStartDateTime(startDateTime);
		repositoryService.persist(obj);
		return obj;
	}

	@Programmatic
	public RecurringActivity createRecurringActivity(final String name, final DateTime startDateTime,
			final Region region) {
		
		final RecurringActivity obj = new RecurringActivity();
		serviceRegistry.injectServicesInto(obj);
		obj.setName(name);
		obj.setStartDateTime(startDateTime);
		obj.setRegion(region);
		repositoryService.persist(obj);
		return obj;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "5")
	public List<RecurringActivity> listAllRecurringActivities() {
		return repositoryService.allInstances(RecurringActivity.class);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "3")
	public List<RecurringActivity> findRecurringActivityByName(@ParameterLayout(named = "Name") final String name) {
		return repositoryService
				.allMatches(new QueryDefault<>(RecurringActivity.class, "findRecurringActivityByName", "name", name));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2")
	public ActivityEvent createOneOffActivity(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Activity name") final String name,
			@Parameter(optionality = Optionality.OPTIONAL, maxLength = 25, regexPattern = "^[\\p{IsAlphabetic}\\p{IsDigit}]+$", regexPatternReplacement = "Must be Alpha-Numeric") @ParameterLayout(named = "DEX 'Case' Id") final String abbreviatedName,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Start date time") final DateTime startDateTime) {
		Activity a = findActivity(name, startDateTime);
		if (a != null) {
			messageService.informUser("Activity with same Name, Start Date Time and Region exists");
			if (a instanceof ActivityEvent) {
				return ((ActivityEvent) a);
			} else {
				return null;
			}
		}
		// create the new ActivityEvent
		final ActivityEvent obj = new ActivityEvent();
		serviceRegistry.injectServicesInto(obj);
		obj.setName(name);
		if (abbreviatedName != null) {
			obj.setAbbreviatedName(abbreviatedName);
		} else {
			obj.setAbbreviatedName(name.replaceAll("[^\\p{Alnum}]", ""));
			if (obj.getAbbreviatedName().length() > 25)
				obj.setAbbreviatedName(obj.getAbbreviatedName().substring(0, 25));
		}
		obj.setStartDateTime(startDateTime);
		repositoryService.persist(obj);
		return obj;
	}

	// find any kind of Activity
	@Programmatic
	public Activity findActivity(String name, DateTime startDateTime) {
		// find the region of the current user
		if (userService != null) {
			UserMemento user = userService.getUser();
			Region region = null;
			if (user != null) {
				ApplicationUser appUser = userRepository.findByUsername(user.getName());
				if (appUser != null) {
					String regionName = AbstractChatsDomainEntity.regionNameOfApplicationUser(appUser);
					region = regionsRepo.regionForName(regionName);
				}
			}
			// see if there is already an existing Activity
			List<Activity> activities = repositoryService.allMatches(new QueryDefault<>(Activity.class,
					"findActivityByUpperCaseName", "name", name.trim().toUpperCase()));
			for (Activity activity : activities) {
				if (activity.getStartDateTime().getDayOfYear() == startDateTime.getDayOfYear()
						&& activity.getRegion().equals(region)) {
					return activity;
				}
			}
		}
		return null;
	}

	// find a specifically ParentedActivity
	@Programmatic
	public ParentedActivityEvent findParentedActivity(String name, DateTime startDateTime) {
		// find the region of the current user
		UserMemento user = userService.getUser();
		Region region = null;
		if (user != null) {
			ApplicationUser appUser = userRepository.findByUsername(user.getName());
			if (appUser != null) {
				String regionName = AbstractChatsDomainEntity.regionNameOfApplicationUser(appUser);
				region = regionsRepo.regionForName(regionName);
			}
		}
		// see if there is already an existing ParentedActivityEvent
		List<ParentedActivityEvent> activities = repositoryService.allMatches(new QueryDefault<>(ParentedActivityEvent.class,
				"findParentedActivityByUpperCaseName", "name", name.trim().toUpperCase()));
		for (ParentedActivityEvent activity : activities) {
			if (activity.getStartDateTime().equals(startDateTime) && activity.getRegion().equals(region)) {
				return activity;
			}
		}
		return null;
	}

	@Programmatic
	public ParentedActivityEvent createParentedActivity(RecurringActivity parent, final DateTime startDateTime,
			final DateTime endDateTime) {
		if (parent == null || startDateTime == null) {
			return null;
		}
		if (findParentedActivity(parent.getName(), startDateTime) != null) {
			messageService.informUser("Parented Activity with same Name, Start Date Time and Region exists");
			return null;
		}
		final ParentedActivityEvent obj = new ParentedActivityEvent();
		serviceRegistry.injectServicesInto(obj);
		obj.setName(parent.getName());
		if (parent.getAbbreviatedName() != null) {
			obj.setAbbreviatedName(parent.getAbbreviatedName());
		} else {
			obj.setAbbreviatedName(parent.getName().replaceAll("\\s", ""));
		}
		obj.setParentActivity(parent);
		obj.setStartDateTime(startDateTime);
		if (endDateTime != null) {
			obj.setEndDateTime(endDateTime);
		}
		repositoryService.persist(obj);
		return obj;
	}

	// used for data-migration
	@Programmatic
	public ActivityEvent createOneOffActivity(String name, DateTime startDateTime, Region region) {
		final ActivityEvent obj = new ActivityEvent();
		serviceRegistry.injectServicesInto(obj);
		obj.setName(name);
		obj.setAbbreviatedName("TO-DO");
		obj.setStartDateTime(startDateTime);
		obj.setRegion(region);
		repositoryService.persist(obj);
		return obj;
	}

	@Programmatic
	public ActivityEvent findOrCreateOneOffActivity(String name, DateTime time, Region region) {
		if (name == null || name.trim().length() == 0)
			return null;
		if (time == null)
			return null;
		if (region == null)
			return null;
		for (ActivityEvent activity : findActivityByName(name)) {
			if (activity.getStartDateTime().equals(time)) {
				return activity;
			}
		}
		return createOneOffActivity(name, time, region);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "4")
	public List<ActivityEvent> findActivityByName(@ParameterLayout(named = "Name") final String name) {
		return repositoryService.allMatches(new QueryDefault<>(ActivityEvent.class, "findActivityByName", "name", name));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "6")
	public List<ActivityEvent> listAllFutureActivities() {
		return repositoryService.allMatches(
				new QueryDefault<>(ActivityEvent.class, "findAllFutureActivities", "currentDateTime", new DateTime()));
	}

	@Programmatic
	public List<ActivityEvent> listAllPastActivities() {
		return repositoryService.allMatches(
				new QueryDefault<>(ActivityEvent.class, "findAllPastActivities", "currentDateTime", new DateTime()));
	}

	@Programmatic
	public List<ActivityEvent> allActivities() {
		return repositoryService.allMatches(new QueryDefault<>(ActivityEvent.class, "findActivities"));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "7")
	public List<ActivityEvent> listActivitiesInPeriod(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Start Period") LocalDate start,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "End Period") LocalDate end) {
		return repositoryService.allMatches(new QueryDefault<>(ActivityEvent.class, "findActivitiesInPeriod", "startDateTime",
				start.toDateTimeAtStartOfDay(), "endDateTime", end.toDateTime(new LocalTime(23, 59))));
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
	protected UserService userService;

	@Inject
	protected RepositoryService repositoryService;
	
	@Inject
	protected ServiceRegistry2 serviceRegistry;
	
	@Inject
	protected MessageService messageService;
}
