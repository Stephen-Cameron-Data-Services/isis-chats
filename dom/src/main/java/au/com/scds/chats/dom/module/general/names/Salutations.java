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
package au.com.scds.chats.dom.module.general.names;

import java.util.ArrayList;
import java.util.List;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;


@DomainService(repositoryFor = Salutation.class)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "100.10")
public class Salutations {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public List<Salutation> listAllSalutations() {
		List<Salutation> list = container.allMatches(new QueryDefault<>(Salutation.class, "findAllSalutations"));
		return list;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2")
	public List<Salutation> createSalutation(final @ParameterLayout(named = "Salutation Name") String name) {
		final Salutation obj = container.newTransientInstance(Salutation.class);
		obj.setName(name);
		container.persistIfNotAlready(obj);
		container.flush();
		return listAllSalutations();
	}

	@Programmatic
	public String nameForSalutation(Salutation salutation) {
		return (salutation != null) ? salutation.getName() : null;
	}

	@Programmatic
	public List<String> allNames() {
		List<Salutation> salutations = listAllSalutations();
		List<String> names = new ArrayList<String>();
		for (Salutation r : salutations) {
			names.add(r.getName());
		}
		return names;
	}

	@Programmatic
	public Salutation salutationForName(String name) {
		if (name == null)
			return null;
		else
			return container.firstMatch(new QueryDefault<>(Salutation.class, "findSalutationByName", "name", name));
	}

	@javax.inject.Inject
	DomainObjectContainer container;

}
