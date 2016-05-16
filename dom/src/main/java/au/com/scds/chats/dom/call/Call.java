
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
package au.com.scds.chats.dom.call;

import java.text.DecimalFormat;

import javax.inject.Inject;
import javax.jdo.annotations.*;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.util.ObjectContracts;
import org.incode.module.note.dom.api.notable.Notable;
//import org.incode.module.note.dom.api.notable.Notable;
import org.joda.time.DateTime;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.volunteer.Volunteer;

/**
 * AbstractCall is a logged call to a Participant.
 * 
 * @author stevec
 */
@PersistenceCapable(table = "telephonecall", identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy = DiscriminatorStrategy.VALUE_MAP, column = "classifier", value = "_CALL")
public abstract class Call extends AbstractChatsDomainEntity  {

	private Participant participant;
	private DateTime startDateTime;
	private DateTime endDateTime;
	private String summaryNotes;

	private static DecimalFormat hoursFormat = new DecimalFormat("#,##0.00");

	public Call() {
	}

	public Call(DomainObjectContainer container) {
		this.container = container;
	}

	public String title() {
		return "Call to: " + getParticipant().getFullName();
	}

	@Property(hidden = Where.REFERENCES_PARENT)
	@PropertyLayout()
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "true")
	public Participant getParticipant() {
		return participant;
	}

	void setParticipant(final Participant participant) {
		this.participant = participant;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Use 'Start Call' to set")
	@PropertyLayout(hidden = Where.PARENTED_TABLES)
	@MemberOrder(sequence = "4")
	@Column(allowsNull = "true")
	public DateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(final DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Use 'End Call' to set")
	@PropertyLayout(hidden = Where.PARENTED_TABLES)
	@MemberOrder(sequence = "5")
	@Column(allowsNull = "true")
	public DateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(final DateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	@Property(editing = Editing.DISABLED, notPersisted = true)
	@PropertyLayout(named = "Call Length in Hours", describedAs = "The interval that the participant attended the activity in hours")
	@MemberOrder(sequence = "6")
	@NotPersistent
	public String getCallLength() {
		if (getStartDateTime() != null && getEndDateTime() != null) {
			Period per = new Period(getStartDateTime().toLocalDateTime(), getEndDateTime().toLocalDateTime());
			Float hours = ((float) per.toStandardMinutes().getMinutes()) / 60;
			return hoursFormat.format(hours);
		} else
			return null;
	}

	@Property()
	@MemberOrder(sequence = "7")
	@Column(allowsNull = "true", length = 1000)
	public String getSummaryNotes() {
		return summaryNotes;
	}

	public void setSummaryNotes(String notes) {
		this.summaryNotes = notes;
	}

	@Programmatic
	public Integer getCallIntervalInMinutes() {
		if (getStartDateTime() != null && getEndDateTime() != null) {
			Period per = new Period(getStartDateTime().toLocalDateTime(), getEndDateTime().toLocalDateTime());
			return per.toStandardMinutes().getMinutes();
		} else
			return null;
	}

	@Inject()
	ClockService clockService;

	@Inject()
	DomainObjectContainer container;

}
