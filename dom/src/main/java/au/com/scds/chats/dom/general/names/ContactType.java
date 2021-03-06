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

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Queries({
	@Query(name = "findContactTypeByName", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.general.names.ContactType "
			+ "WHERE name == :name"), 
	@Query(name = "findAllContactTypes", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.general.names.ContactType "
			+ "ORDER BY name")})
public class ContactType extends ClassificationValue{

	@Property()
	@PropertyLayout(named="Contact Type")
	@MemberOrder(sequence = "1")
	@Override
	public String getName() {
		return super.getName();
	}
}
