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


import java.util.Date;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.services.i18n.TranslatableString;

import au.com.scds.eventschedule.base.impl.activity.ActivityEvent;

@DomainObject(objectType="chats.participationview", nature=Nature.VIEW_MODEL)
public class ParticipationView implements Comparable<ParticipationView>{

	private ActivityEvent activity;
	private Date startDateTime;
	
	public TranslatableString title(){
		return getActivity().title();
	}
	
	public ActivityEvent getActivity() {
		return activity;
	}
	public void setActivity(ActivityEvent activity) {
		this.activity = activity;
	}
	
	public Date getStartDateTime() {
		return startDateTime;
	}
	
	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}
	
	@Override
	public int compareTo(ParticipationView o) {
		return this.getActivity().compareTo(o.getActivity());
	}

}