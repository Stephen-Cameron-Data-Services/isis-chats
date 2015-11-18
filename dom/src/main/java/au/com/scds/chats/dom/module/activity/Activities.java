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
	
	public Activities(){}

	//used for testing
    public Activities(DomainObjectContainer container) {
		this.container = container;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public RecurringActivity createRecurringActivity(@Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named="Activity name") final String name, @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named="Start date time") final DateTime startDateTime) {
		final RecurringActivity obj = container.newTransientInstance(RecurringActivity.class);
		obj.setName(name);
		obj.setStartDateTime(startDateTime);
		container.persistIfNotAlready(obj);
		container.flush();
		return obj;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2")
	public List<RecurringActivity> listAllRecurringActivities() {
		return container.allInstances(RecurringActivity.class);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "3")
	public List<RecurringActivity> findRecurringActivityByName(@ParameterLayout(named = "Name") final String name) {
		return container.allMatches(new QueryDefault<>(RecurringActivity.class, "findRecurringActivityByName", "name", name));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "5")
	public OneOffActivity createOneOffActivity(@Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named="Activity name") final String name, @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named="Start date time") final DateTime startDateTime) {
		final OneOffActivity obj = container.newTransientInstance(OneOffActivity.class);
		obj.setName(name);
		obj.setStartDateTime(startDateTime);
		container.persistIfNotAlready(obj);
		container.flush();
		return obj;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "6")
	public List<OneOffActivity> findOneOffActivityByName(@ParameterLayout(named = "Name") final String name) {
		return container.allMatches(new QueryDefault<>(OneOffActivity.class, "findOneOffActivityByName", "name", name));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "10")
	public List<ActivityEvent> listAllFutureActivities() {
		return container.allMatches(new QueryDefault<>(ActivityEvent.class, "findAllFutureActivities", "currentDateTime", new DateTime()));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "11")
	public List<ActivityEvent> listAllPastActivities() {
		return container.allMatches(new QueryDefault<>(ActivityEvent.class, "findAllPastActivities","currentDateTime", new DateTime()));
	}

	@javax.inject.Inject
	DomainObjectContainer container;

}
