package au.com.scds.chats.integtest;
/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
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

import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;

import au.com.scds.chats.dom.activity.ChatsParticipant;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.general.Status;
import au.com.scds.chats.fixture.scenarios.CreateChatsActivities;
import au.com.scds.chats.fixture.scenarios.CreateChatsParticipant;
import au.com.scds.chats.fixture.scenarios.CreateDexReferenceData;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.xactn.TransactionService;
import org.joda.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

public class Participants_IntegTest extends IntegTestAbstract {

	@Inject
	FixtureScripts fixtureScripts;
	@Inject
	TransactionService transactionService;

	List<ChatsParticipant> participants = null;

	@Before
	public void setUp() throws Exception {
		// given
		CreateDexReferenceData fs1 = new CreateDexReferenceData ();
		CreateChatsParticipant fs2 = new CreateChatsParticipant();
		fixtureScripts.runFixtureScript(fs1, null);
		fixtureScripts.runFixtureScript(fs2, null);
		transactionService.nextTransaction();
		participants = fs2.getParticipants();
		assertThat(participants).isNotNull();
	}

	public static class Participants extends Participants_IntegTest {

		@Test
		public void accessible() throws Exception {
			
			ChatsParticipant p = participants.get(0);
			assertThat(p).isNotNull();
			
			assertThat(p.getPerson()).isNotNull();
			assertThat(p.getPerson().getFirstname()).isEqualTo("c:firstname");
			assertThat(p.getPerson().getSurname()).isEqualTo("c:surname");
			assertThat(p.getPerson().getBirthdate()).isEqualTo(new LocalDate("2001-01-01"));
			assertThat(p.getPerson().getSex()).isEqualTo(Sex.MALE);
			
			assertThat(p.getStatus()).isEqualTo(Status.ACTIVE);
			assertThat(p.getMobility()).isEqualTo("c:mobility");
			assertThat(p.getLimitingHealthIssues()).isEqualTo("c:limitingHealthIssues");
			assertThat(p.getOtherLimitingFactors()).isEqualTo("c:otherLimitingFactors");
			assertThat(p.getDriversLicence()).isEqualTo("c:driversLicence");
			assertThat(p.getDrivingAbility()).isEqualTo("c:drivingAbility");
			assertThat(p.getDrivingConfidence()).isEqualTo("c:drivingConfidence");
			assertThat(p.getPlaceOfOrigin()).isEqualTo("c:placeOfOrigin");
			assertThat(p.getYearOfSettlement()).isEqualTo(100);
			assertThat(p.getCloseRelatives()).isEqualTo("c:closeRelatives");
			assertThat(p.getCloseRlFrCount()).isEqualTo(10);
			assertThat(p.getProximityOfRelatives()).isEqualTo("c:proximityOfRelatives");
			assertThat(p.getProximityOfFriends()).isEqualTo("c:proximityOfFriends");
			assertThat(p.getInvolvementGC()).isEqualTo("c:involvementGC");
			assertThat(p.getInvolvementIH()).isEqualTo("c:involvementIH");
			assertThat(p.getLifeStory()).isEqualTo("c:lifeStory");
			assertThat(p.getLifeExperiences()).isEqualTo("c:lifeExperiences");
			assertThat(p.getHobbies()).isEqualTo("c:hobbies");
			assertThat(p.getInterests()).isEqualTo("c:interests");
			assertThat(p.getLoneliness()).isEqualTo("c:loneliness");
			assertThat(p.isConsentToProvideDetails()).isEqualTo(true);
			assertThat(p.isConsentedForFutureContacts()).isEqualTo(true);
			assertThat(p.isHasCarer()).isEqualTo(true);
			assertThat(p.getCountryOfBirth().getName()).isEqualTo("Country_1101");
			assertThat(p.getLanguageSpokenAtHome().getName()).isEqualTo("Language_1201");
			assertThat(p.getAboriginalOrTorresStraitIslanderOrigin().getName()).isEqualTo("AboriginalOrTorresStraitIslanderOrigin_ABORIGINAL");
			assertThat(p.getAccommodationType().getName()).isEqualTo("AccommodationType_CLIENTOWNED");
			assertThat(p.getDvaCardStatus().getName()).isEqualTo("DVACardStatus_GOLD");
			assertThat(p.getHouseholdComposition().getName()).isEqualTo("HouseholdComposition_FAMILY");
			
			assertThat(p.getClientNotes().first().getNote()).isEqualTo("c:clientNote");
			assertThat(p.getDisabilities().get(0).getName()).isEqualTo("Disability_LEARNING");	
		}
	}
}