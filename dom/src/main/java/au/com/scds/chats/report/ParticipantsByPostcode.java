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
package au.com.scds.chats.report;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;

import au.com.scds.chats.dom.general.ChatsPerson;
import au.com.scds.chats.dom.activity.ChatsParticipant;
import au.com.scds.chats.dom.activity.ParticipantsMenu;

public class ParticipantsByPostcode {

	/*public List<InactiveParticipant> inactiveParticipants(){
		return container.allMatches(new QueryDefault<>(InactiveParticipant.class,"findInactiveParticipants"));
	}
	
	public List<InactiveParticipant> participantActivity(@Parameter(optionality=Optionality.MANDATORY) Participant participant){
		Person p = participant.getPerson();
		return container.allMatches(new QueryDefault<>(InactiveParticipant.class,"getParticipantActivity","firstname",p.getFirstname(),"surname",p.getSurname(),"birthdate",p.getBirthdate()));
	}
	
	public List<Participant> choices0ParticipantActivity(){
		return participantsRepo.listActive();
	}*/
	
	/*@Inject
	DomainObjectContainer container;
	@Inject
	Participants participantsRepo;*/
}