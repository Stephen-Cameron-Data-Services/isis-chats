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

import java.util.Date;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.ViewModel;
import org.joda.time.DateTime;

@DomainObject(objectType = "chats.scheduledcallview", nature = Nature.VIEW_MODEL)
public class ChatsScheduledCallView {

	private ChatsScheduledCall call;

	public ChatsScheduledCall getScheduledCall() {
		return call;
	}

	public void setScheduledCall(ChatsScheduledCall call2) {
		this.call = call2;
	}

	public String getSummaryNotes() {
		return getScheduledCall().getSummaryNotes();
	}

	public Date getStartDateTime() {
		DateTime start = getScheduledCall().getStart();
		return (start != null) ? start.toDate() : null;
	}

	public String getIntervalLength() {
		return getScheduledCall().getIntervalLengthFormatted();
	}
}
