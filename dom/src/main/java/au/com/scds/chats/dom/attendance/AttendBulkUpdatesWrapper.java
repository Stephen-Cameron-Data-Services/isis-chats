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
package au.com.scds.chats.dom.attendance;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.InvokeOn;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.actinvoc.ActionInvocationContext;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.StartAndFinishDateTime;
import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.general.names.TransportType;
import au.com.scds.chats.dom.general.names.TransportTypes;
import au.com.scds.chats.dom.participant.Participant;

@DomainObject(nature=Nature.INMEMORY_ENTITY)
public class AttendBulkUpdatesWrapper{

	private Attend wrapped;
	
	public Attend getWrapped() {
		return wrapped;
	}

	public void setWrapped(Attend wrapped) {
		this.wrapped = wrapped;
	}

	public String title() {
		return getWrapped().title();
	}

	public String iconName() {
		return getWrapped().getWasAttended();
	}

	public String getParticipant() {
		return getWrapped().getParticipantName();
	}
	
	public DateTime getStartDateTime() {
		return getWrapped().getStartDateTime();
	}
	
	public DateTime getEndDateTime() {
		return getWrapped().getEndDateTime();
	}
	
	public String getIntervalLength() {
		return getWrapped().getIntervalLength();
	}

	public String getWasAttended() {
		return getWrapped().getWasAttended();
	}

	@Action(invokeOn=InvokeOn.COLLECTION_ONLY)
	public void wasAttended() {
		getWrapped().wasAttended();
		return;
	}

	@Action(invokeOn=InvokeOn.COLLECTION_ONLY)
	public void wasNotAttended() {
		getWrapped().wasNotAttended();
		return;
	}
	
	@Action(invokeOn=InvokeOn.COLLECTION_ONLY)
	public void updateDatesAndTimesFromActivity() {
		getWrapped().updateDatesAndTimesFromActivity();
	}
}
