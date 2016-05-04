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
package au.com.scds.chats.dom.general.names;

import java.util.ArrayList;
import java.util.List;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = ActivityType.class)
// @DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration",
// menuOrder = "100.1")
public class ActivityTypes {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public List<ActivityType> listAllActivityTypes() {
		List<ActivityType> list = container.allMatches(new QueryDefault<>(ActivityType.class, "findAllActivityTypes"));
		return list;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2")
	public List<ActivityType> createActivityType(final @ParameterLayout(named = "Activity Type Name") String name) {
		final ActivityType obj = create(name);
		return listAllActivityTypes();
	}

	@Programmatic
	public ActivityType create(String name) {
		final ActivityType obj = container.newTransientInstance(ActivityType.class);
		obj.setName(name);
		container.persistIfNotAlready(obj);
		container.flush();
		return obj;
	}

	@Programmatic
	public String nameForActivityType(ActivityType activityType) {
		return (activityType != null) ? activityType.getName() : null;
	}

	@Programmatic
	public List<String> allNames() {
		List<ActivityType> activityTypes = listAllActivityTypes();
		List<String> names = new ArrayList<String>();
		for (ActivityType a : activityTypes) {
			names.add(a.getName());
		}
		return names;
	}

	@Programmatic
	public ActivityType activityTypeForName(String name) {
		if (name == null)
			return null;
		else
			return container.firstMatch(new QueryDefault<>(ActivityType.class, "findActivityTypeByName", "name", name));
	}

	@javax.inject.Inject
	DomainObjectContainer container;

}
