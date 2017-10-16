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
package au.com.scds.chats.dom.volunteer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;


@DomainService(nature=NatureOfService.DOMAIN, repositoryFor = VolunteerRole.class)
public class VolunteerRoles {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public List<VolunteerRole> listAllVolunteerRoles() {
		List<VolunteerRole> list = repositoryService.allMatches(new QueryDefault<>(VolunteerRole.class, "findAllVolunteerRoles"));
		return list;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2")
	public List<VolunteerRole> createVolunteerRole(final @ParameterLayout(named = "Role Name") String name) {
		final VolunteerRole obj = create(name);
		return listAllVolunteerRoles();
	}
	
	@Programmatic
	protected VolunteerRole create(String name){
		final VolunteerRole obj = new VolunteerRole();
		serviceRegistry.injectServicesInto(obj);
		obj.setName(name);
		repositoryService.persist(obj);
		return obj;		
	}

	@Programmatic
	public String nameForVolunteerRole(VolunteerRole volunteerRole) {
		return (volunteerRole != null) ? volunteerRole.getName() : null;
	}

	@Programmatic
	public List<String> allNames() {
		List<VolunteerRole> volunteerRoles = listAllVolunteerRoles();
		List<String> names = new ArrayList<String>();
		for (VolunteerRole a : volunteerRoles) {
			names.add(a.getName());
		}
		return names;
	}

	@Programmatic
	public VolunteerRole volunteerRoleForName(String name) {
		if (name == null)
			return null;
		else
			return repositoryService.firstMatch(new QueryDefault<>(VolunteerRole.class, "findVolunteerRoleByName", "name", name));
	}

	@Inject
	protected RepositoryService repositoryService;
	
	@Inject
	protected ServiceRegistry2 serviceRegistry;
	
	@Inject
	protected MessageService messageService;
}
