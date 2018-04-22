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

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import au.com.scds.chats.dom.activity.AgeGroup;
import au.com.scds.chats.dom.activity.ChatsParticipant;
import au.com.scds.chats.dom.activity.ParticipantMenu;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.VolunteerMenu;

@DomainService(objectType = "chats.calls", nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = ChatsScheduledCall.class)
@DomainServiceLayout(named = "Calls", menuOrder = "50")
public class CallsMenu {

	@Action(semantics = SemanticsOf.IDEMPOTENT)
	public ChatsScheduledCall create(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "ChatsParticipant") final ChatsParticipant participant,
			@Parameter(optionality = Optionality.MANDATORY) final Volunteer volunteer,
			@Parameter(optionality = Optionality.OPTIONAL) final DateTime dateTime) throws Exception {
		return createScheduledCall(volunteer, participant, dateTime);
	}

	public List<ChatsParticipant> autoComplete0Create(@MinLength(3) String search) {
		return participantsRepo.listActiveChatsParticipants(AgeGroup.All, search);
	}

	public List<Volunteer> choices1Create() {
		return volunteersRepo.listActiveVolunteers();
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<ChatsScheduledCall> findScheduledCalls(
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "ChatsParticipant") final ChatsParticipant participant,
			@Parameter(optionality = Optionality.OPTIONAL) final Volunteer volunteer) {
		if (volunteer != null && participant != null) {
			return repositoryService.allMatches(
					new QueryDefault<>(ChatsScheduledCall.class, "findScheduledCallsByChatsParticipantAndVolunteer",
							"participant", participant, "volunteer", volunteer));
		} else if (volunteer != null) {
			return repositoryService.allMatches(new QueryDefault<>(ChatsScheduledCall.class,
					"findScheduledCallsByVolunteer", "volunteer", volunteer));
		} else if (participant != null) {
			return repositoryService.allMatches(new QueryDefault<>(ChatsScheduledCall.class,
					"findScheduledCallsByChatsParticipant", "participant", participant));
		} else {
			return repositoryService.allMatches(new QueryDefault<>(ChatsScheduledCall.class, "findScheduledCalls"));
		}
	}

	public List<ChatsParticipant> autoComplete0FindScheduledCalls(@MinLength(3) String search) {
		return participantsRepo.listActiveChatsParticipants(AgeGroup.All, search);
	}

	public List<Volunteer> autoComplete1FindScheduledCalls(@MinLength(3) String search) {
		return volunteersRepo.listActiveVolunteers(search);
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<ChatsScheduledCall> listCallsInPeriod(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Start Period") LocalDate start,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "End Period") LocalDate end) {
		return repositoryService.allMatches(new QueryDefault<>(ChatsScheduledCall.class, "findCallsInPeriod",
				"start", start.toDateTimeAtStartOfDay(), "end", end.toDateTime(new LocalTime(23, 59))));
	}

	@Programmatic
	public List<ChatsScheduledCall> findScheduledCallsForChatsParticipant(final ChatsParticipant participant) {
		if (participant == null)
			throw new IllegalArgumentException("participant is a mandatory argument");
		return repositoryService.allMatches(new QueryDefault<>(ChatsScheduledCall.class,
				"findScheduledCallsByChatsParticipant", "participant", participant));
	}

	@Programmatic
	public ChatsScheduledCall createScheduledCall(final Volunteer volunteer, final ChatsParticipant participant,
			final DateTime dateTime) throws Exception {
		if (volunteer == null)
			throw new IllegalArgumentException("volunteer is a mandatory argument");
		if (participant == null)
			throw new IllegalArgumentException("participant is a mandatory argument");
		if (dateTime == null)
			throw new IllegalArgumentException("dateTime is a mandatory argument");
		ChatsScheduledCall call = new ChatsScheduledCall(findOrCreateChatsCaller(volunteer),
				findOrCreateChatsCallee(participant), dateTime);
		serviceRegistry.injectServicesInto(call);
		call.setRegion(participant.getRegion());
		repositoryService.persist(call);
		return call;
	}

	@Programmatic
	public void deleteCall(ChatsScheduledCall call) {
		repositoryService.remove(call);
	}

	@Programmatic
	private ChatsCallee findOrCreateChatsCallee(ChatsParticipant participant) {
		ChatsCallee contactee = repositoryService.uniqueMatch(
				new QueryDefault<>(ChatsCallee.class, "findForParticipant", "participant", participant));
		if (contactee == null) {
			contactee = new ChatsCallee(participant);
			repositoryService.persist(contactee);
		}
		return contactee;
	}
	
	public List<ChatsCaller> listAllChatsCallers(){
		return repositoryService.allInstances(ChatsCaller.class);
	}
	
	public List<ChatsCallee> listAllChatsCallees(){
		return repositoryService.allInstances(ChatsCallee.class);
	}
	

	@Programmatic
	private ChatsCaller findOrCreateChatsCaller(Volunteer volunteer) {
		ChatsCaller contactor = repositoryService
				.uniqueMatch(new QueryDefault<>(ChatsCaller.class, "findForVolunteer", "volunteer", volunteer));
		if (contactor == null) {
			contactor = new ChatsCaller(volunteer);
			repositoryService.persist(contactor);
		}
		return contactor;
	}

	@Programmatic
	public ChatsCallAllocation createChatsCallAllocation(Volunteer volunteer,
			ChatsParticipant participant) {
		if (volunteer == null)
			throw new IllegalArgumentException("volunteer is a mandatory argument");
		if (participant == null)
			throw new IllegalArgumentException("participant is a mandatory argument");
		ChatsCallAllocation allocation = new ChatsCallAllocation(this.findOrCreateChatsCaller(volunteer), 
				this.findOrCreateChatsCallee(participant));
		serviceRegistry.injectServicesInto(allocation);
		repositoryService.persist(allocation);
		volunteer.getCallAllocations().add(allocation);
		participant.getCallAllocations().add(allocation);
		return allocation;
	}

	@Programmatic
	public void deleteChatsCallAllocation(ChatsCallAllocation allocation) {
		repositoryService.remove(allocation);
	}

	@Inject
	protected VolunteerMenu volunteersRepo;
	
	@Inject
	protected CallsMenu callsRepo;

	@Inject
	protected ParticipantMenu participantsRepo;

	@Inject
	protected RepositoryService repositoryService;

	@Inject
	protected ServiceRegistry2 serviceRegistry;

	@Inject
	protected MessageService messageService;
}
