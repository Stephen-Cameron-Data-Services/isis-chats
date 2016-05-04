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

@DomainService(nature = NatureOfService.DOMAIN,repositoryFor = Region.class)
//@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "100.8")
public class Regions {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public List<Region> listAllRegions() {
		List<Region> list = container.allMatches(new QueryDefault<>(Region.class, "findAllRegions"));
		return list;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2")
	public List<Region> createRegion(final @ParameterLayout(named = "Region Name") String name) {
		final Region obj = container.newTransientInstance(Region.class);
		obj.setName(name);
		container.persistIfNotAlready(obj);
		container.flush();
		return listAllRegions();
	}

	@Programmatic
	public String nameForRegion(Region region) {
		return (region != null) ? region.getName() : null;
	}

	@Programmatic
	public List<String> allNames() {
		List<Region> regions = listAllRegions();
		List<String> names = new ArrayList<String>();
		for (Region r : regions) {
			names.add(r.getName());
		}
		return names;
	}

	@Programmatic
	public Region regionForName(String name) {
		if (name == null)
			return null;
		else
			return container.firstMatch(new QueryDefault<>(Region.class, "findRegionByName", "name", name));
	}

	@javax.inject.Inject
	DomainObjectContainer container;
}
