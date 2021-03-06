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

import org.apache.isis.applib.annotation.ViewModel;
import org.joda.time.DateTime;

@ViewModel()
public class ScheduledCallView {

	private ScheduledCall call;

	public ScheduledCall getScheduledCall() {
		return call;
	}

	public void setScheduledCall(ScheduledCall call) {
		this.call = call;
	}

	public String getSummaryNotes() {
		return getScheduledCall().getSummaryNotes();
	}

	public Date getStartDateTime() {
		DateTime start = getScheduledCall().getStartDateTime();
		return (start != null) ? start.toDate() : null;
	}

	public String getIntervalLength() {
		return getScheduledCall().getIntervalLengthFormatted();
	}
}
