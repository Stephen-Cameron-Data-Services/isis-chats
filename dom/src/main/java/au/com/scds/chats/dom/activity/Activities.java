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
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import au.com.scds.chats.dom.general.names.Region;

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
@DomainService(repositoryFor = ActivityEvent.class)
@DomainServiceLayout(named = "Activities", menuOrder = "10")
public class Activities {
	
	public Activities() {
	}

	// used for testing
	public Activities(DomainObjectContainer container) {
		this.container = container;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public RecurringActivity createRecurringActivity(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Activity name") final String name,
			@Parameter(optionality = Optionality.MANDATORY, maxLength = 25) @ParameterLayout(named = "Abbreviated name") final String abbreviatedName,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Start date time") final DateTime startDateTime) {
		final RecurringActivity obj = container.newTransientInstance(RecurringActivity.class);
		obj.setName(name);
		obj.setAbbreviatedName(abbreviatedName);
		obj.setStartDateTime(startDateTime);
		container.persistIfNotAlready(obj);
		container.flush();
		return obj;
	}

	@Programmatic
	public RecurringActivity createRecurringActivity(final String name, final DateTime startDateTime,
			final Region region) {
		final RecurringActivity obj = container.newTransientInstance(RecurringActivity.class);
		obj.setName(name);
		obj.setStartDateTime(startDateTime);
		obj.setRegion(region);
		container.persistIfNotAlready(obj);
		container.flush();
		return obj;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "5")
	public List<RecurringActivity> listAllRecurringActivities() {
		return container.allInstances(RecurringActivity.class);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "3")
	public List<RecurringActivity> findRecurringActivityByName(@ParameterLayout(named = "Name") final String name) {
		return container
				.allMatches(new QueryDefault<>(RecurringActivity.class, "findRecurringActivityByName", "name", name));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2")
	public ActivityEvent createOneOffActivity(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Activity name") final String name,
			@Parameter(optionality = Optionality.MANDATORY, maxLength = 25) @ParameterLayout(named = "Abbreviated name") final String abbreviatedName,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Start date time") final DateTime startDateTime) {
		final ActivityEvent obj = container.newTransientInstance(ActivityEvent.class);
		obj.setName(name);
		obj.setAbbreviatedName(abbreviatedName);
		obj.setStartDateTime(startDateTime);
		container.persistIfNotAlready(obj);
		container.flush();
		return obj;
	}

	// used for data-migration
	@Programmatic
	public ActivityEvent createOneOffActivity(String name, DateTime startDateTime, Region region) {
		final ActivityEvent obj = container.newTransientInstance(ActivityEvent.class);
		obj.setName(name);
		obj.setAbbreviatedName("TO-DO");
		obj.setStartDateTime(startDateTime);
		obj.setRegion(region);
		container.persistIfNotAlready(obj);
		container.flush();
		return obj;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "4")
	public List<ActivityEvent> findActivityByName(@ParameterLayout(named = "Name") final String name) {
		return container.allMatches(new QueryDefault<>(ActivityEvent.class, "findActivityByName", "name", name));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "6")
	public List<ActivityEvent> listAllFutureActivities() {
		return container.allMatches(
				new QueryDefault<>(ActivityEvent.class, "findAllFutureActivities", "currentDateTime", new DateTime()));
	}

	//@Action(semantics = SemanticsOf.SAFE)
	//@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	//@MemberOrder(sequence = "7")
	@Programmatic
	public List<ActivityEvent> listAllPastActivities() {
		return container.allMatches(
				new QueryDefault<>(ActivityEvent.class, "findAllPastActivities", "currentDateTime", new DateTime()));
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "7")
	public List<ActivityEvent> listActivitiesInPeriod(
			@Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named="Start Period") LocalDate start, 
			@Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named="End Period")  LocalDate end) {
		return container.allMatches(
				new QueryDefault<>(ActivityEvent.class, "findActivitiesInPeriod", "startDateTime", start.toDateTimeAtStartOfDay(), "endDateTime", end.toDateTime(new LocalTime(23,59))));
	}

	@javax.inject.Inject
	DomainObjectContainer container;

}
