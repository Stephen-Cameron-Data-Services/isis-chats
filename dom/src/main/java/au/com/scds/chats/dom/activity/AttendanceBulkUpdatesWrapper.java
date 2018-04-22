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


import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.InvokeOn;
import org.apache.isis.applib.annotation.Nature;


import org.joda.time.DateTime;


@DomainObject(objectType = "AttendBulkUpdatesWrapper", nature = Nature.INMEMORY_ENTITY)
public class AttendanceBulkUpdatesWrapper {

	private ChatsAttendance wrapped;

	public ChatsAttendance getWrapped() {
		return wrapped;
	}

	public void setWrapped(ChatsAttendance wrapped) {
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
		return getWrapped().getStart();
	}

	public DateTime getEndDateTime() {
		return getWrapped().getEnd();
	}

	public String getIntervalLength() {
		return getWrapped().getIntervalLengthFormatted();
	}
	
	public String getArrivingTransportType() {
		return getWrapped().getArrivingTransportTypeName();
	}
	
	public String getDepartingTransportType() {
		return getWrapped().getDepartingTransportTypeName();
	}

	public String getWasAttended() {
		return getWrapped().getWasAttended();
	}

	@Action(invokeOn = InvokeOn.COLLECTION_ONLY)
	public void wasAttended() {
		if (getWrapped() != null)
			getWrapped().wasAttended();
		return;
	}

	@Action(invokeOn = InvokeOn.COLLECTION_ONLY)
	public void wasNotChatsAttendanceed() {
		if (getWrapped() != null)
			getWrapped().wasAttended();
	}

	@Action(invokeOn = InvokeOn.COLLECTION_ONLY)
	public void updateDatesAndTimesFromActivity() {
		if (getWrapped() != null)
			getWrapped().updateDatesAndTimesFromActivity();
	}

	@Action(invokeOn = InvokeOn.COLLECTION_ONLY)
	public void removeChatsAttendanceance() {
		setWrapped(null);
	}
}
