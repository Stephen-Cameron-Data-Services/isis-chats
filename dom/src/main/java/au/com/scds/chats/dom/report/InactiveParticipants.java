/*
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;

import au.com.scds.chats.dom.general.ChatsPerson;
import au.com.scds.chats.dom.activity.AgeGroup;
import au.com.scds.chats.dom.activity.ChatsParticipant;
import au.com.scds.chats.dom.activity.ParticipantsMenu;
import au.com.scds.chats.report.view.InactiveParticipant;

//Reports
@DomainService(objectType="InactiveParticipants", nature = NatureOfService.VIEW_MENU_ONLY)
public class InactiveParticipants {

	@Action
	public List<InactiveParticipant> findMostInactiveParticipants() {
		List<InactiveParticipant> list = repositoryService
				.allMatches(new QueryDefault<>(InactiveParticipant.class, "findInactiveParticipants"));
		Map<Long, InactiveParticipant> map = new HashMap<>();
		// search for the most recent attendance of each active participant
		for (InactiveParticipant p : list) {
			if (p.getDaysSinceLastAttended() != null) {
				if (!map.containsKey(p.getPersonId())) {
					map.put(p.getPersonId(), p);
				} else if (map.get(p.getPersonId()).getDaysSinceLastAttended() > p.getDaysSinceLastAttended()) {
					map.replace(p.getPersonId(), p);
				}
			}
		}
		ArrayList<InactiveParticipant> temp = new ArrayList<>();
		for(InactiveParticipant i : map.values()){
			temp.add(i);
		}
		return temp;
	}

	@Action
	public List<InactiveParticipant> findParticipantActivity(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Participant") ChatsParticipant participant) {
		if (participant == null)
			return null;
		ChatsPerson p = participant.getPerson();
		return repositoryService.allMatches(new QueryDefault<>(InactiveParticipant.class, "getParticipantActivity", "firstname",
				p.getFirstname(), "surname", p.getSurname(), "birthdate", p.getBirthdate()));
	}

	public List<ChatsParticipant> autoComplete0FindParticipantActivity(@MinLength(3) String search) {
		return participantsMenu.listActiveChatsParticipants(AgeGroup.All, search);
	}
	
	@Inject
	RepositoryService repositoryService;

	@Inject
	ParticipantsMenu participantsMenu;

}
