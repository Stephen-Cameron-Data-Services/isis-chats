/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
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
package au.com.scds.chats.dom.module.activity;

import java.util.List;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.DateTime;

@DomainService(repositoryFor = ActivityEvent.class)
@DomainServiceLayout(named = "Activities", menuOrder = "10")
public class Activities {

	// {{ createRecurringActivity (action)
    @Action(semantics = SemanticsOf.SAFE)
	@MemberOrder(sequence = "1")
	public RecurringActivity createRecurringActivity(@Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named="Activity name") final String name, @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named="Start date time") final DateTime startDateTime) {
		final RecurringActivity obj = container.newTransientInstance(RecurringActivity.class);
		obj.setName(name);
		obj.setStartDateTime(startDateTime);
		container.persistIfNotAlready(obj);
		container.flush();
		return obj;
	}

	// }}

	// listAllRecurringActivities (action)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "2")
	@CollectionLayout(paged = 20, render = RenderType.EAGERLY)
	public List<RecurringActivity> listAllRecurringActivities() {
		return container.allInstances(RecurringActivity.class);
	}

	// }}

	// findByRecurringActivityName (action)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "3")
	public List<RecurringActivity> findByRecurringActivityByName(@ParameterLayout(named = "Name") final String name) {
		return container.allMatches(new QueryDefault<>(RecurringActivity.class, "findByRecurringActivityByName", "name", name));
	}

	// {{ createOneOffActivity (action)
	@MemberOrder(sequence = "5")
	public ActivityEvent createOneOffActivity(@Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named="Activity name") final String name, @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named="Start date time") final DateTime startDateTime) {
		final ActivityEvent obj = container.newTransientInstance(ActivityEvent.class);
		obj.setName(name);
		obj.setStartDateTime(startDateTime);
		container.persistIfNotAlready(obj);
		container.flush();
		return obj;
	}

	// }}

	// findOneOffActivityByName (action)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "6")
	public List<ActivityEvent> findOneOffActivityByName(@ParameterLayout(named = "Name") final String name) {
		return container.allMatches(new QueryDefault<>(ActivityEvent.class, "findOneOffActivityByName", "name", name));
	}

	// listAllFutureActivities (action)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "10")
	public List<ActivityEvent> listAllFutureActivities() {
		return container.allMatches(new QueryDefault<>(ActivityEvent.class, "findAllFutureActivities", "currentDateTime", new DateTime()));
	}

	// listAllPastActivities (action)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "10")
	public List<ActivityEvent> listAllPastActivities() {
		return container.allMatches(new QueryDefault<>(ActivityEvent.class, "findAllPastActivities","currentDateTime", new DateTime()));
	}

	// injected services

	@javax.inject.Inject
	DomainObjectContainer container;


}
