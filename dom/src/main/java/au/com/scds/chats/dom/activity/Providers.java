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
 */package au.com.scds.chats.dom.activity;

import java.util.ArrayList;
import java.util.List;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;

@DomainService(repositoryFor = Provider.class)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "80.1")
public class Providers {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public List<Provider> listAllProviders() {
		return container.allInstances(Provider.class);
	}

	/*
	 * @Action(semantics = SemanticsOf.SAFE)
	 * 
	 * @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	 * 
	 * @MemberOrder(sequence = "2") public List<ActivityProvider> findByName(
	 * 
	 * @ParameterLayout(named = "Name") final String name) { return
	 * container.allMatches(new QueryDefault<>(ActivityProvider.class,
	 * "findByName", "name", name)); }
	 */

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "3")
	public Provider createProvider(final @ParameterLayout(named = "Name") String name) {
		final Provider obj = container.newTransientInstance(Provider.class);
		obj.setName(name);
		container.persistIfNotAlready(obj);
		return obj;
	}

	@javax.inject.Inject
	DomainObjectContainer container;
}
