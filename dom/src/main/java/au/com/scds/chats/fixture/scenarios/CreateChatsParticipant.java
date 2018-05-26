package au.com.scds.chats.fixture.scenarios;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.activity.ParticipantsMenu;
import au.com.scds.chats.dom.dex.DexReferenceData;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.fixture.generated.CallAllocation;
import au.com.scds.chats.fixture.generated.ChatsParticipant;
import au.com.scds.chats.fixture.generated.ChatsPerson;
import au.com.scds.chats.fixture.generated.ClientNote;
import au.com.scds.chats.fixture.generated.Country;
import au.com.scds.chats.fixture.generated.Disability;
import au.com.scds.chats.fixture.generated.Language;
import au.com.scds.chats.fixture.generated.ObjectFactory;
import au.com.scds.chats.fixture.generated.Participants;
import au.com.scds.chats.fixture.generated.Status;
import au.com.scds.chats.fixture.generated.Volunteer;

public class CreateChatsParticipant extends FixtureScript {

	public CreateChatsParticipant() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	private List<au.com.scds.chats.dom.activity.ChatsParticipant> participants = new ArrayList<>();

	@Override
	protected void execute(ExecutionContext ec) {

		try {
			// import object graph from XML
			InputStream is = this.getClass().getResourceAsStream("/au/com/scds/chats/fixture/participants.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
			Participants _participants = (Participants) JAXBIntrospector.getValue(jaxbUnmarshaller.unmarshal(is));
			for (ChatsParticipant _p : _participants.getParticipant()) {
				
				ChatsPerson _person = _p.getPerson();
				au.com.scds.chats.dom.activity.ChatsParticipant participant = participantMenu.create(
						_person.getFirstname(), _person.getSurname(),
						LocalDate.fromDateFields(_person.getDateOfBirth()), Sex.valueOf(_person.getSex()));
				
				participant.setMobility(_p.getMobility());
			    participant.setLimitingHealthIssues(_p.getLimitingHealthIssues());
			    participant.setOtherLimitingFactors(_p.getOtherLimitingFactors());
			    participant.setDriversLicence(_p.getDriversLicence());
			    participant.setDrivingAbility(_p.getDrivingAbility());
			    participant.setDrivingConfidence(_p.getDrivingConfidence());
			    participant.setPlaceOfOrigin(_p.getPlaceOfOrigin());
			    participant.setYearOfSettlement(_p.getYearOfSettlement());
			    participant.setCloseRelatives(_p.getCloseRelatives());
			    participant.setCloseRlFrCount(_p.getCloseRlFrCount());
			    participant.setProximityOfRelatives(_p.getProximityOfRelatives());
			    participant.setProximityOfFriends(_p.getProximityOfFriends());
			    participant.setInvolvementGC(_p.getInvolvementGC());
			    participant.setInvolvementIH(_p.getInvolvementIH());
			    participant.setLifeStory(_p.getLifeStory());
			    participant.setLifeExperiences(_p.getLifeExperiences());
			    participant.setHobbies(_p.getHobbies());
			    participant.setInterests(_p.getInterests());
			    participant.setLoneliness(_p.getLoneliness());
			    participant.setConsentToProvideDetails(_p.isConsentToProvideDetails());
			    participant.setConsentedForFutureContacts(_p.isConsentedForFutureContacts());
			    participant.setHasCarer(_p.isHasCarer());
			    participant.setCountryOfBirth(dexRefData.getCountryOfBirthForDescription(_p.getCountryOfBirth()));
			    participant.setLanguageSpokenAtHome(dexRefData.getLanguageSpokenAtHomeForDescription(_p.getLanguageSpokenAtHome()));
			    participant.setAboriginalOrTorresStraitIslanderOrigin(dexRefData.getAboriginalOrTorresStraitIslanderOriginForDescription(_p.getAboriginalOrTorresStraitIslanderOrigin()));
			    participant.setAccommodationType(dexRefData.getAccommodationTypeForDescription(_p.getAccommodationType()));
			    participant.setDvaCardStatus(dexRefData.getDVACardStatusForDescription(_p.getDvaCardStatus()));
			    participant.setHouseholdComposition(dexRefData.getHouseholdCompositionForDescription(_p.getHouseholdComposition()));
			    
			    for(String _note : _p.getClientNote()){
			    	participant.addClientNote(_note);
			    }
			    
			    for(String _disability : _p.getDisability()){
			    	participant.addDisability(dexRefData.getDisabilityForDescription(_disability));
			    }

				participants.add(participant);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public List<au.com.scds.chats.dom.activity.ChatsParticipant> getParticipants() {
		return this.participants;
	}

	@Inject
	ParticipantsMenu participantMenu;
	
	@Inject
	protected DexReferenceData dexRefData;

}
