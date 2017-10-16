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
package au.com.scds.chats.dom.general;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Suburb.class)
public class Suburbs {

	@Action(semantics = SemanticsOf.SAFE)
	public Suburb createSuburb(final @ParameterLayout(named = "Activity Type Name") String name, Integer postcode) {
		final Suburb obj = create(name, postcode);
		return obj;
	}

	@Action(semantics = SemanticsOf.SAFE)
	public Suburb findSuburb(String name, Integer postcode) {
		if (name == null && postcode == null)
			return null;
		else if (postcode == null)
			return repositoryService.firstMatch(new QueryDefault<>(Suburb.class, "findSuburbsWithNamesLike", "name", name));
		else
			return repositoryService.firstMatch(new QueryDefault<>(Suburb.class, "findSuburbByNameAndPostcode", "name", name,
					"postcode", postcode));
	}

	@Programmatic
	public Suburb findSuburb(String suburb, String postcode) {
		Suburb s = null;
		try {
			return findSuburb(suburb, Integer.parseInt(postcode));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<Suburb> listAllSuburbs() {
		List<Suburb> list = repositoryService.allMatches(new QueryDefault<>(Suburb.class, "findAllSuburbs"));
		return list;
	}

	@Programmatic
	public Suburb create(String name, Integer postcode) {
		final Suburb obj = new Suburb();
		serviceRegistry.injectServicesInto(obj);
		obj.setName(name);
		obj.setPostcode(postcode);
		repositoryService.persist(obj);
		return obj;
	}

	@Programmatic
	public String nameForSuburb(Suburb Suburb) {
		return (Suburb != null) ? Suburb.getName() : null;
	}

	@Programmatic
	public List<String> allNames() {
		List<Suburb> Suburbs = listAllSuburbs();
		List<String> names = new ArrayList<String>();
		for (Suburb a : Suburbs) {
			names.add(a.getName());
		}
		return names;
	}

	@Programmatic
	public List<String> listSuburbNamesLike(String search) {
		if (search == null)
			return null;
		else
			return repositoryService.allMatches(new QueryDefault<>(String.class, "findSuburbNamesLike", "name", search));
	}

	@Inject
	protected RepositoryService repositoryService;
	
	@Inject
	protected ServiceRegistry2 serviceRegistry;
}