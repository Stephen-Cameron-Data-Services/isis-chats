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

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.DomainObject;
import org.joda.time.DateTime;

@DomainObject()
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "VolunteeredTimeForCalls")
@Queries({
	@Query(name = "findForVolunteer", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.volunteer.VolunteeredTimeForCalls WHERE volunteer == :volunteer ")})
public class VolunteeredTimeForCalls extends VolunteeredTime {
	
	public VolunteeredTimeForCalls(Volunteer volunteer, DateTime start, DateTime end){
		super(volunteer, start, end);
	}
}
