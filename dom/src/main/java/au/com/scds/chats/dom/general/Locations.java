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

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.isisaddons.wicket.gmap3.cpt.service.LocationLookupService;

@DomainService(repositoryFor = Location.class, nature=NatureOfService.DOMAIN)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "100.9")
public class Locations {
	

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public List<Location> listAllLocations() {
		List<Location> list = repositoryService.allMatches(new QueryDefault<>(Location.class, "findAllLocations"));
		return list;
	}

	@MemberOrder(sequence = "2")
	public List<Location> createLocation(final @ParameterLayout(named = "Location Name") String name) {
		final Location obj = new Location();
		serviceRegistry.injectServicesInto(obj);
		obj.setName(name);
		repositoryService.persist(obj);
		return listAllLocations();
	}
	
	@Programmatic()
	public Location createNewLocation(String name) {
		final Location obj = new Location();
		serviceRegistry.injectServicesInto(obj);
		obj.setName(name);
		repositoryService.persist(obj);
		return obj;
	}
	
	@Programmatic()
	public Address createAddress() {
		final Address obj = new Address();
		serviceRegistry.injectServicesInto(obj);
		repositoryService.persist(obj);
		return obj;
	}
	
	@Programmatic()
	public TransportHub createTransportHub() {
		final TransportHub obj = new TransportHub();
		serviceRegistry.injectServicesInto(obj);
		repositoryService.persist(obj);
		return obj;
	}
	
	@Programmatic()
	public List<Address> listAllNamedAddressLocations() {
		List<Address> addresses = repositoryService.allMatches(new QueryDefault<>(Address.class, "findAllNamedAddresses"));
		return addresses;
	}
	
	@Programmatic()
	public List<TransportHub> listAllTransportHubs() {
		List<TransportHub> hubs = repositoryService.allMatches(new QueryDefault<>(TransportHub.class, "findAllTransportHubs"));
		return hubs;
	}
	
	public List<TransportHub> listAllNamedTransportHubs() {
		List<TransportHub> hubs = repositoryService.allMatches(new QueryDefault<>(TransportHub.class, "findAllNamedTransportHubs"));
		return hubs;
	}

	@Programmatic
	public String nameForLocation(Location location) {
		return (location != null) ? location.getName() : null;
	}

	@Programmatic
	public List<String> allNames() {
		List<Location> locations = listAllLocations();
		List<String> names = new ArrayList<String>();
		for (Location l : locations) {
			names.add(l.getName());
		}
		return names;
	}

	@Programmatic
	public Location locationForName(String name) {
		if (name == null)
			return null;
		else
			return repositoryService.firstMatch(new QueryDefault<>(Location.class, "findLocationByName", "name", name));
	}
	
	@Programmatic 
	public org.isisaddons.wicket.gmap3.cpt.applib.Location locationOfAddressViaGmapLookup(String address){
		return locationLookupService.lookup(address);
	}

	@Inject
	RepositoryService repositoryService;
	
	@Inject
	protected ServiceRegistry2 serviceRegistry;
	
	@Inject
	protected MessageService messageService;
	
	@Inject
	private LocationLookupService locationLookupService;
}
