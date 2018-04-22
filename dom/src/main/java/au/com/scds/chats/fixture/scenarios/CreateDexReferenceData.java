package au.com.scds.chats.fixture.scenarios;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import au.com.scds.chats.dom.dex.reference.AboriginalOrTorresStraitIslanderOrigin;
import au.com.scds.chats.dom.dex.reference.AbstractDexReferenceItem;
import au.com.scds.chats.dom.dex.reference.AccommodationType;
import au.com.scds.chats.dom.dex.reference.Ancestry;
import au.com.scds.chats.dom.dex.reference.AssessmentPhase;
import au.com.scds.chats.dom.dex.reference.Country;
import au.com.scds.chats.dom.dex.reference.DVACardStatus;
import au.com.scds.chats.dom.dex.reference.Disability;
import au.com.scds.chats.dom.dex.reference.ExitReason;
import au.com.scds.chats.dom.dex.reference.ExtraItem;
import au.com.scds.chats.dom.dex.reference.Gender;
import au.com.scds.chats.dom.dex.reference.HouseholdComposition;
import au.com.scds.chats.dom.dex.reference.IncomeFrequency;
import au.com.scds.chats.dom.dex.reference.Language;
import au.com.scds.chats.dom.dex.reference.MainSourceOfIncome;
import au.com.scds.chats.dom.dex.reference.MigrationVisaCategory;
import au.com.scds.chats.dom.dex.reference.MoneyBusinessCommunityEducationWorkshop;
import au.com.scds.chats.dom.dex.reference.ParentingAgreement;
import au.com.scds.chats.dom.dex.reference.ParticipationType;
import au.com.scds.chats.dom.dex.reference.ReasonForAssistance;
import au.com.scds.chats.dom.dex.reference.ReferralPurpose;
import au.com.scds.chats.dom.dex.reference.ReferralSource;
import au.com.scds.chats.dom.dex.reference.ReferralType;
import au.com.scds.chats.dom.dex.reference.ScoreType;
import au.com.scds.chats.dom.dex.reference.Section60ICertificateType;
import au.com.scds.chats.dom.dex.reference.State;
import au.com.scds.chats.fixture.generated.dex.*;

public class CreateDexReferenceData extends FixtureScript {

	public CreateDexReferenceData() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	@Override
	protected void execute(ExecutionContext ec) {

		try {
			// import object graph from XML
			InputStream is = this.getClass().getResourceAsStream("/au/com/scds/chats/fixture/DEXReferenceData.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
			DEXReferenceDataType _refData = (DEXReferenceDataType) JAXBIntrospector
					.getValue(jaxbUnmarshaller.unmarshal(is));
			for (DEXReferenceDataType.ReferenceDataItems _items : _refData.getReferenceDataItems()) {
				for (RefenceDataItemType _item : _items.getItem()) {

					AbstractDexReferenceItem item = null;

					switch (_items.getCodeType()) {

					case "AboriginalOrTorresStraitIslanderOrigin":
						item = new AboriginalOrTorresStraitIslanderOrigin(
								"AboriginalOrTorresStraitIslanderOrigin_" + _item.getCodeValue(),
								_item.getCodeDescription(), _item.getOrderNumber());
						break;
					case "AccommodationType":
						item = new AccommodationType("AccommodationType_" + _item.getCodeValue(),
								_item.getCodeDescription(), _item.getOrderNumber());
						break;
					case "Ancestry":
						item = new Ancestry("Ancestry_" + _item.getCodeValue(), _item.getCodeDescription(),
								_item.getOrderNumber());
						break;
					case "AssessmentPhase":
						item = new AssessmentPhase("AssessmentPhase_" + _item.getCodeValue(),
								_item.getCodeDescription(), _item.getOrderNumber());
						break;
					case "Country":
						item = new Country("Country_" + _item.getCodeValue(), _item.getCodeDescription(),
								_item.getOrderNumber());
						break;
					case "Disability":
						item = new Disability("Disability_" + _item.getCodeValue(), _item.getCodeDescription(),
								_item.getOrderNumber());
						break;
					case "DVACardStatus":
						item = new DVACardStatus("DVACardStatus_" + _item.getCodeValue(), _item.getCodeDescription(),
								_item.getOrderNumber());
						break;
					case "ExitReason":
						item = new ExitReason("ExitReason_" + _item.getCodeValue(), _item.getCodeDescription(),
								_item.getOrderNumber());
						break;
					case "ExtraItem":
						item = new ExtraItem("ExtraItem_" + _item.getCodeValue(), _item.getCodeDescription(),
								_item.getOrderNumber());
						break;
					case "Gender":
						item = new Gender("Gender_" + _item.getCodeValue(), _item.getCodeDescription(),
								_item.getOrderNumber());
						break;
					case "HouseholdComposition":
						item = new HouseholdComposition("HouseholdComposition_" + _item.getCodeValue(),
								_item.getCodeDescription(), _item.getOrderNumber());
						break;
					case "IncomeFrequency":
						item = new IncomeFrequency("IncomeFrequency_" + _item.getCodeValue(), _item.getCodeDescription(),
								_item.getOrderNumber());
						break;
					case "Language":
						item = new Language("Language_" + _item.getCodeValue(), _item.getCodeDescription(),
								_item.getOrderNumber());
						break;
					case "MainSourceOfIncome":
						item = new MainSourceOfIncome("MainSourceOfIncome_" + _item.getCodeValue(),
								_item.getCodeDescription(), _item.getOrderNumber());
						break;
					case "MigrationVisaCategory":
						item = new MigrationVisaCategory("MigrationVisaCategory_" + _item.getCodeValue(), _item.getCodeDescription(),
								_item.getOrderNumber());
						break;
					case "MoneyBusinessCommunityEducationWorkshop":
						item = new MoneyBusinessCommunityEducationWorkshop(
								"MoneyBusinessCommunityEducationWorkshop_" + _item.getCodeValue(),
								_item.getCodeDescription(), _item.getOrderNumber());
						break;
					case "ParentingAgreement":
						item = new ParentingAgreement("ParentingAgreement_" + _item.getCodeValue(),
								_item.getCodeDescription(), _item.getOrderNumber());
						break;
					case "ParticipationType":
						item = new ParticipationType("ParticipationType_" + _item.getCodeValue(),
								_item.getCodeDescription(), _item.getOrderNumber());
						break;
					case "ReasonForAssistance":
						item = new ReasonForAssistance("ReasonForAssistance_" + _item.getCodeValue(),
								_item.getCodeDescription(), _item.getOrderNumber());
						break;
					case "ReferralPurpose":
						item = new ReferralPurpose("ReferralPurpose_" + _item.getCodeValue(), _item.getCodeDescription(),
								_item.getOrderNumber());
						break;
					case "ReferralSource":
						item = new ReferralSource("ReferralSource_" + _item.getCodeValue(), _item.getCodeDescription(),
								_item.getOrderNumber());
						break;
					case "ReferralType":
						item = new ReferralType("ReferralType_" + _item.getCodeValue(), _item.getCodeDescription(),
								_item.getOrderNumber());
						break;
					case "ScoreType":
						item = new ScoreType("ScoreType_" + _item.getCodeValue(), _item.getCodeDescription(),
								_item.getOrderNumber());
						break;
					case "Section60ICertificateType":
						item = new Section60ICertificateType("Section60ICertificateType_" + _item.getCodeValue(),
								_item.getCodeDescription(), _item.getOrderNumber());
						break;
					case "State":
						item = new State("State_" + _item.getCodeValue(), _item.getCodeDescription(),
								_item.getOrderNumber());
						break;
					}

					if (item != null) {
						repositoryService.persistAndFlush(item);
					}
				}
			}
			for (DEXReferenceDataType.AssessmentReferenceDataItems _items : _refData
					.getAssessmentReferenceDataItems()) {
				for (AssessmentRefenceDataItemType _item : _items.getAssessmentReferenceDataItem()) {
					System.out.println(_item.getScoreType());
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
