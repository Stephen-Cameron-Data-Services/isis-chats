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

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = TransportType.class)
// @DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration",
// menuOrder = "100.12")
public class TransportTypes {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public List<TransportType> listAllTransportTypes() {
		List<TransportType> list = container
				.allMatches(new QueryDefault<>(TransportType.class, "findAllTransportTypes"));
		return list;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2")
	public List<TransportType> createTransportType(final @ParameterLayout(named = "Transport Type Name") String name) {
		final TransportType obj = create(name);
		return listAllTransportTypes();
	}

	@Programmatic
	public TransportType create(String name) {
		final TransportType obj = container.newTransientInstance(TransportType.class);
		obj.setName(name);
		container.persistIfNotAlready(obj);
		container.flush();
		return obj;
	}

	@Programmatic
	public String nameForTransportType(TransportType activityType) {
		return (activityType != null) ? activityType.getName() : null;
	}

	@Programmatic
	public List<String> allNames() {
		List<TransportType> transportTypes = listAllTransportTypes();
		List<String> names = new ArrayList<String>();
		for (TransportType a : transportTypes) {
			names.add(a.getName());
		}
		return names;
	}

	@Programmatic
	public TransportType transportTypeForName(String name) {
		if (name == null)
			return null;
		else
			return container
					.firstMatch(new QueryDefault<>(TransportType.class, "findTransportTypeByName", "name", name));
	}

	@javax.inject.Inject
	DomainObjectContainer container;
}
