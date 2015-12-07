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
package au.com.scds.chats.dom.module.general.names;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.util.ObjectContracts;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Queries({
	@Query(name = "findRegionByName", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.module.general.names.Region "
			+ "WHERE name == :name"), 
	@Query(name = "findAllRegions", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.module.general.names.Region "
			+ "ORDER BY name")})
public class Region extends ClassificationValue implements Comparable<Region>{
	
	@PropertyLayout(named="Region")
	@MemberOrder(sequence="1")
	@Override
	public String getName() {
		return super.getName();
	}
	
	public int compareTo(final Region other) {
		return ObjectContracts.compare(other, this, "name");
	}
}
