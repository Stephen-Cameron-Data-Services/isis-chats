package au.com.scds.chats.dom.dex;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;

import au.com.scds.chats.dom.dex.reference.AboriginalOrTorresStraitIslanderOrigin;
import au.com.scds.chats.dom.dex.reference.AccommodationType;
import au.com.scds.chats.dom.dex.reference.Ancestry;
import au.com.scds.chats.dom.dex.reference.AssessmentPhase;
import au.com.scds.chats.dom.dex.reference.AssessmentScoreType;
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

@DomainService(nature=NatureOfService.DOMAIN)
public class DexReferenceData {

	@Action()
	public List<AboriginalOrTorresStraitIslanderOrigin> allAboriginalOrTorresStraitIslanderOrigin(){
		return container.allMatches(new QueryDefault(AboriginalOrTorresStraitIslanderOrigin.class, "all"));
	}

	@Action()
	public List<AccommodationType> allAccommodationType(){
		return container.allMatches(new QueryDefault(AccommodationType.class, "all"));
	}

	@Action()
	public List<Ancestry> allAncestry(){
		return container.allMatches(new QueryDefault(Ancestry.class, "all"));
	}

	@Action()
	public List<AssessmentPhase> allAssessmentPhase(){
		return container.allMatches(new QueryDefault(AssessmentPhase.class, "all"));
	}

	@Action()
	public List<Country> allCountry(){
		return container.allMatches(new QueryDefault(Country.class, "all"));
	}

	@Action()
	public List<Disability> allDisability(){
		return container.allMatches(new QueryDefault(Disability.class, "all"));
	}

	@Action()
	public List<DVACardStatus> allDVACardStatus(){
		return container.allMatches(new QueryDefault(DVACardStatus.class, "all"));
	}

	@Action()
	public List<ExitReason> allExitReason(){
		return container.allMatches(new QueryDefault(ExitReason.class, "all"));
	}

	@Action()
	public List<ExtraItem> allExtraItem(){
		return container.allMatches(new QueryDefault(ExtraItem.class, "all"));
	}

	@Action()
	public List<Gender> allGender(){
		return container.allMatches(new QueryDefault(Gender.class, "all"));
	}

	@Action()
	public List<HouseholdComposition> allHouseholdComposition(){
		return container.allMatches(new QueryDefault(HouseholdComposition.class, "all"));
	}

	@Action()
	public List<IncomeFrequency> allIncomeFrequency(){
		return container.allMatches(new QueryDefault(IncomeFrequency.class, "all"));
	}

	@Action()
	public List<Language> allLanguage(){
		return container.allMatches(new QueryDefault(Language.class, "all"));
	}

	@Action()
	public List<MainSourceOfIncome> allMainSourceOfIncome(){
		return container.allMatches(new QueryDefault(MainSourceOfIncome.class, "all"));
	}

	@Action()
	public List<MigrationVisaCategory> allMigrationVisaCategory(){
		return container.allMatches(new QueryDefault(MigrationVisaCategory.class, "all"));
	}

	@Action()
	public List<MoneyBusinessCommunityEducationWorkshop> allMoneyBusinessCommunityEducationWorkshop(){
		return container.allMatches(new QueryDefault(MoneyBusinessCommunityEducationWorkshop.class, "all"));
	}

	@Action()
	public List<ParentingAgreement> allParentingAgreement(){
		return container.allMatches(new QueryDefault(ParentingAgreement.class, "all"));
	}

	@Action()
	public List<ParticipationType> allParticipationType(){
		return container.allMatches(new QueryDefault(ParticipationType.class, "all"));
	}

	@Action()
	public List<ReasonForAssistance> allReasonForAssistance(){
		return container.allMatches(new QueryDefault(ReasonForAssistance.class, "all"));
	}

	@Action()
	public List<ReferralPurpose> allReferralPurpose(){
		return container.allMatches(new QueryDefault(ReferralPurpose.class, "all"));
	}

	@Action()
	public List<ReferralSource> allReferralSource(){
		return container.allMatches(new QueryDefault(ReferralSource.class, "all"));
	}

	@Action()
	public List<ReferralType> allReferralType(){
		return container.allMatches(new QueryDefault(ReferralType.class, "all"));
	}

	@Action()
	public List<ScoreType> allScoreType(){
		return container.allMatches(new QueryDefault(ScoreType.class, "all"));
	}

	@Action()
	public List<Section60ICertificateType> allSection60ICertificateType(){
		return container.allMatches(new QueryDefault(Section60ICertificateType.class, "all"));
	}

	@Action()
	public List<State> allState(){
		return container.allMatches(new QueryDefault(State.class, "all"));
	}
	
	@Action()
	public List<AssessmentScoreType> allAssessmentScoreType(){
		return container.allMatches(new QueryDefault(AssessmentScoreType.class, "all"));
	}
	
	@Inject
	private DomainObjectContainer container;
}
