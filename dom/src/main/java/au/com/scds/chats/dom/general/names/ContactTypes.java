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

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;

@DomainService(repositoryFor = ContactType.class)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "100.2")
public class ContactTypes {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public List<ContactType> listAllContactTypes() {
		List<ContactType> list = container.allMatches(new QueryDefault<>(ContactType.class, "findAllContactTypes"));
		return list;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2")
	public List<ContactType> createContactType(final @ParameterLayout(named = "ContactType Name") String name) {
		final ContactType obj = container.newTransientInstance(ContactType.class);
		obj.setName(name);
		container.persistIfNotAlready(obj);
		container.flush();
		return listAllContactTypes();
	}

	@Programmatic
	public String nameForContactType(ContactType contactType) {
		return (contactType != null) ? contactType.getName() : null;
	}

	@Programmatic
	public List<String> allNames() {
		List<ContactType> contactTypes = listAllContactTypes();
		List<String> names = new ArrayList<String>();
		for (ContactType c : contactTypes) {
			names.add(c.getName());
		}
		return names;
	}

	@Programmatic
	public ContactType contactTypeForName(String name) {
		if (name == null)
			return null;
		else
			return container.firstMatch(new QueryDefault<>(ContactType.class, "findContactTypeByName", "name", name));
	}

	@javax.inject.Inject
	DomainObjectContainer container;
}
