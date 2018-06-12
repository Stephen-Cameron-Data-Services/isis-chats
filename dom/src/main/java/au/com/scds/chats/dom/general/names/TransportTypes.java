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

import javax.inject.Inject;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = TransportType.class)
public class TransportTypes {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public List<TransportType> listAllTransportTypes() {
		List<TransportType> list = repositoryService
				.allMatches(new QueryDefault<>(TransportType.class, "findAllTransportTypes"));
		return list;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2")
	public TransportType createTransportType(final @ParameterLayout(named = "Transport Type Name") String name) {
		final TransportType obj = create(name);
		return obj;
	}

	@Programmatic
	public TransportType create(String name) {
		final TransportType obj = new TransportType();
		serviceRegistry.injectServicesInto(obj);
		obj.setName(name);
		repositoryService.persist(obj);
		return obj;
	}

	@Programmatic
	public String nameForTransportType(TransportType transportType) {
		return (transportType != null) ? transportType.getName() : null;
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

	public TransportType findOrCreateTransportType(String name) {
		TransportType type = this.transportTypeForName(name);
		if (type == null)
			type = create(name);
		return type;
	}

	@Programmatic
	public TransportType transportTypeForName(String name) {
		if (name == null)
			return null;
		else
			return repositoryService
					.firstMatch(new QueryDefault<>(TransportType.class, "findTransportTypeByName", "name", name));
	}

	@Inject
	protected RepositoryService repositoryService;

	@Inject
	protected ServiceRegistry2 serviceRegistry;
}
