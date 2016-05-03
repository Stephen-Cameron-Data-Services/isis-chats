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
package au.com.scds.chats.dom.activity;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.services.i18n.TranslatableString;

@DomainObject(objectType = "PROVIDER")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Queries({ @Query(name = "listAllProviders", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.activity.Provider "),
		@Query(name = "findProvidersByName", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.activity.Provider " + "WHERE name.indexOf(:name) >= 0 ") })
@Unique(name = "Provider_name_UNQ", members = { "name" })
public class Provider {

	private String name;
	
	public TranslatableString title() {
		return TranslatableString.tr("Provider: {name}", "name", getName());
	}

	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false", length = 40)
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
