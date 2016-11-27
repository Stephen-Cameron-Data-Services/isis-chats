
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
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.StartAndFinishDateTime;
import au.com.scds.chats.dom.participant.Participant;

/**
 * A Call is a logged call to a Participant.
 * 
 * @author stevec
 */
@PersistenceCapable(table = "telephonecall", identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy = DiscriminatorStrategy.VALUE_MAP, column = "classifier", value = "_CALL")
public abstract class Call extends StartAndFinishDateTime {

	private Participant participant;
	private String summaryNotes;

	public Call() {
	}

	public Call(DomainObjectContainer container) {
		this.container = container;
	}

	public String title() {
		return "Call to: " + getParticipant().getFullName();
	}

	@Property(editing=Editing.DISABLED)
	@Column(allowsNull = "false")
	public Participant getParticipant() {
		return participant;
	}

	void setParticipant(final Participant participant) {
		this.participant = participant;
	}

	@Column(allowsNull = "true", jdbcType = "CLOB")
	public String getSummaryNotes() {
		return summaryNotes;
	}
	
	public void setSummaryNotes(String notes) {
		this.summaryNotes = notes;
	}

	@NotPersistent
	public String getTrimmedSummaryNotes() {
		if (getSummaryNotes() != null) {
			return (getSummaryNotes().length() > 50) ? getSummaryNotes().substring(0, 49).concat("...")
					: getSummaryNotes();
		} else {
			return null;
		}
	}

	@Action()
	public Call startCall() {
		setStartDateTime(clockService.nowAsDateTime());
		setEndDateTime(null);
		return this;
	}

	@Action()
	public Call endCall() {
		setEndDateTime(clockService.nowAsDateTime());
		return this;
	}

	public String disableEndCall() {
		if (getStartDateTime() == null) {
			return "Start Time has not been set";
		} else {
			return null;
		}
	}

	@Inject()
	ClockService clockService;

	@Inject()
	DomainObjectContainer container;

}
