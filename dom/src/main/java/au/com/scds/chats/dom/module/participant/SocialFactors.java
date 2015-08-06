package au.com.scds.chats.dom.module.participant;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.joda.time.LocalDate;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@DomainObject(objectType = "SOCIAL-FACTORS")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class SocialFactors {

	public String title() {
		return "Social Factors of Participant: " + parent.getPerson().getFullname();
	}

	// {{ ParentParticipant (property)
	private Participant parent;

	@Column(allowsNull = "false")
	@Property(editing=Editing.DISABLED)
	@MemberOrder(sequence = "100")
	public Participant getParentParticipant() {
		return parent;
	}

	public void setParentParticipant(final Participant parent) {
		if (this.parent == null && parent != null)
			this.parent = parent;
	}

	// }}

	// {{ LimitingHealthIssues (property)
	private String limitingHealthIssues;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "1")
	public String getLimitingHealthIssues() {
		return limitingHealthIssues;
	}

	public void setLimitingHealthIssues(final String limitingHealthIssues) {
		this.limitingHealthIssues = limitingHealthIssues;
	}

	// }}

	// {{ OtherLimitingFactors (property)
	private String otherLimitingFactors;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "2")
	public String getOtherLimitingFactors() {
		return otherLimitingFactors;
	}

	public void setOtherLimitingFactors(final String otherLimitingFactors) {
		this.otherLimitingFactors = otherLimitingFactors;
	}

	// }}

	// {{ DriversLicence (property)
	private String driversLicence;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "3")
	public String getDriversLicence() {
		return driversLicence;
	}

	public void setDriversLicence(final String driversLicence) {
		this.driversLicence = driversLicence;
	}

	// }}

	// {{ DrivingAbility (property)
	private String drivingAbility;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "4")
	public String getDrivingAbility() {
		return drivingAbility;
	}

	public void setDrivingAbility(final String drivingAbility) {
		this.drivingAbility = drivingAbility;
	}

	// }}

	// {{ DrivingConfidence (property)
	private String drivingConfidence;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "5")
	public String getDrivingConfidence() {
		return drivingConfidence;
	}

	public void setDrivingConfidence(final String drivingConfidence) {
		this.drivingConfidence = drivingConfidence;
	}

	// }}

	// {{ PlaceOfOrigin (property)
	private String placeOfOrigin;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "6")
	public String getPlaceOfOrigin() {
		return placeOfOrigin;
	}

	public void setPlaceOfOrigin(final String placeOfOrigin) {
		this.placeOfOrigin = placeOfOrigin;
	}

	// }}

	// {{ DateofSettlement (property)
	private LocalDate dateOfSettlement;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "7")
	public LocalDate getDateofSettlement() {
		return dateOfSettlement;
	}

	public void setDateofSettlement(final LocalDate dateOfSettlement) {
		this.dateOfSettlement = dateOfSettlement;
	}

	// }}

	// {{ closeRelatives (property)
	private String closeRelatives;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "8")
	public String getCloseRelatives() {
		return closeRelatives;
	}

	public void setCloseRelatives(final String closeRelatives) {
		this.closeRelatives = closeRelatives;
	}

	// }}

	// {{ closeRelativeAndFriendCount (property)
	private Integer closeRlFrCount;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "9")
	public Integer getCloseRelativeAndFriendCount() {
		return closeRlFrCount;
	}

	public void setCloseRelativeAndFriendCount(
			final Integer closeRelativeAndFriendCount) {
		this.closeRlFrCount = closeRelativeAndFriendCount;
	}

	// }}

	// {{ ProximityOfRelatives (property)
	private String proximityOfRelatives;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "10")
	public String getProximityOfRelatives() {
		return proximityOfRelatives;
	}

	public void setProximityOfRelatives(final String proximityOfRelatives) {
		this.proximityOfRelatives = proximityOfRelatives;
	}

	// }}

	// {{ ProximityOfFriends (property)
	private String proximityOfFriends;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "11")
	public String getProximityOfFriends() {
		return proximityOfFriends;
	}

	@Column(allowsNull = "true")
	public void setProximityOfFriends(final String proximityOfFriends) {
		this.proximityOfFriends = proximityOfFriends;
	}

	// }}

	// {{ InvolvementInGroupsClubs (property)
	private String involvementGC;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "12")
	public String getInvolvementInGroupsClubs() {
		return involvementGC;
	}

	public void setInvolvementInGroupsClubs(final String involvement) {
		this.involvementGC = involvement;
	}

	// }}

	// {{ involvementInInterestsHobbies (property)
	private String involvementIH;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "13")
	public String getInvolvementInInterestsHobbies() {
		return involvementIH;
	}

	public void setInvolvementInInterestsHobbies(
			final String involvmentInInterestsHobbies) {
		this.involvementIH = involvmentInInterestsHobbies;
	} // }}

	// {{ injected dependencies
	@javax.inject.Inject
	@SuppressWarnings("unused")
	private DomainObjectContainer container;
	// }}

}
