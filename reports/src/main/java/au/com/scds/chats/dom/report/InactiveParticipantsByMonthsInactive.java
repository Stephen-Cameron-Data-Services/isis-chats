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
package au.com.scds.chats.dom.report;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.query.QueryDefault;

import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.report.view.InactiveParticipant;

@DomainService(nature=NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(menuBar = MenuBar.PRIMARY, named = "Reports", menuOrder = "70.5")
public class InactiveParticipantsByMonthsInactive {

	public List<InactiveParticipant> findMostInactiveParticipants(){
		return container.allMatches(new QueryDefault<>(InactiveParticipant.class,"findInactiveParticipants"));
	}
	
	public List<InactiveParticipant> findParticipantActivity(@Parameter(optionality=Optionality.MANDATORY) Participant participant){
		Person p = participant.getPerson();
		return container.allMatches(new QueryDefault<>(InactiveParticipant.class,"getParticipantActivity","firstname",p.getFirstname(),"surname",p.getSurname(),"birthdate",p.getBirthdate()));
	}
	
	public List<Participant> choices0FindParticipantActivity(){
		return participantsRepo.listActive();
	}
	
	@Inject
	DomainObjectContainer container;
	@Inject
	Participants participantsRepo;
	
}
