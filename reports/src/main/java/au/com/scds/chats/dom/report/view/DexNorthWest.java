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
package au.com.scds.chats.dom.report.view;

import java.math.BigInteger;
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.ViewModel;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.WithApplicationTenancy;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

@ViewModel
@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(identityType = IdentityType.NONDURABLE, table = "DexNorthWest", extensions = {
		@Extension(vendorName = "datanucleus", key = "view-definition", value = "CREATE VIEW DexNorthWest  ( "
				+ " {this.id}, " + " {this.interactiondate}, " + " {this.minutes}, " + " {this.abbreviatedname}, "
				+ " {this.participantId}, " + " ) AS  select " + " id, " + " interactiondate, " + " minutes, "
				+ " abbreviatedname, " + " participant_id AS participantId " + " FROM " + " dex.datasource") })
@Queries({
		@Query(name = "allInPeriod", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.DexNorthWest pa "
				+ "WHERE pa.interactiondate >= :startDateTime && pa.interactiondate <= :endDateTime"), })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class DexNorthWest /* implements WithApplicationTenancy */ implements Comparable<DexNorthWest> {

	private Long id;
	private LocalDate interactiondate;
	private Integer minutes;
	private String abbreviatedname;
	private Long participantId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getInteractiondate() {
		return interactiondate;
	}

	public void setInteractiondate(LocalDate interactiondate) {
		this.interactiondate = interactiondate;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	public String getAbbreviatedname() {
		return abbreviatedname;
	}

	public void setAbbreviatedname(String abbreviatedname) {
		this.abbreviatedname = abbreviatedname;
	}

	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}

	@Override
	public int compareTo(DexNorthWest o) {
		return (int) (this.getId() - o.getId());
	}

}
