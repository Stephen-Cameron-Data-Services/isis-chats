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

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.PropertyLayout;

import au.com.scds.chats.dom.general.names.ClassificationValue;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Unique(name = "Suburb_UNQ", members = { "name", "postcode" })
@Queries({
		@Query(name = "findSuburbByName", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.general.Suburb " + "WHERE name == :name"),
		@Query(name = "findSuburbNamesLike", language = "JDOQL", value = "SELECT name "
				+ "FROM au.com.scds.chats.dom.general.Suburb " + "WHERE name.startsWith(:name)"),
		@Query(name = "findAllSuburbs", language = "JDOQL", value = "SELECT "
				+ "FROM  au.com.scds.chats.dom.general.Suburb " + "ORDER BY name") })

public class Suburb {

	private String name;
	private Integer postcode;

	public Suburb() {
	}

	// use for testing only
	public Suburb(String name, Integer postcode) {
		this.name = name;
	}
	
	public String title(){
		return getName() + " (" + getPostcode() + ")";
	}

	@PropertyLayout(named = "Suburb")
	@MemberOrder(sequence = "1")
	@Column(allowsNull="false")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@MemberOrder(sequence = "2")
	@Column(allowsNull = "false")
	public Integer getPostcode() {
		return postcode;
	}

	public void setPostcode(Integer postcode) {
		this.postcode = postcode;
	}
}
