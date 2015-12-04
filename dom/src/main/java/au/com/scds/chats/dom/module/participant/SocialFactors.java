package au.com.scds.chats.dom.module.participant;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.LabelPosition;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;

@DomainObject(objectType = "SOCIAL-FACTORS")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General", "Involvement", "Friends and Relatives" }, middle = { "Limitations", "Driving", "Admin" })
@PersistenceCapable(identityType = IdentityType.DATASTORE)
public class SocialFactors extends AbstractChatsDomainEntity {

	private Participant participant;
	private String limitingHealthIssues;
	private String otherLimitingFactors;
	private String driversLicence;
	private String drivingAbility;
	private String drivingConfidence;
	private String placeOfOrigin;
	private LocalDate dateOfSettlement;
	private String closeRelatives;
	private Integer closeRlFrCount;
	private String proximityOfRelatives;
	private String proximityOfFriends;
	private String involvementGC;
	private String involvementIH;

	public String title() {
		return "Social Factors of Participant: " + getParticipant().getPerson().getFullname();
	}

	@Property()
	@PropertyLayout()
	@MemberOrder(sequence = "100")
	@Column(allowsNull = "false")
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant parent) {
		if (getParticipant() == null && parent != null)
			this.participant = parent;
	}

	@Property()
	@PropertyLayout(multiLine = 3, labelPosition = LabelPosition.TOP)
	@MemberOrder(name = "Limitations", sequence = "1")
	@Column(allowsNull = "true")
	public String getLimitingHealthIssues() {
		return limitingHealthIssues;
	}

	public void setLimitingHealthIssues(final String limitingHealthIssues) {
		this.limitingHealthIssues = limitingHealthIssues;
	}

	@Property()
	@PropertyLayout(multiLine = 3, labelPosition = LabelPosition.TOP)
	@MemberOrder(name = "Limitations", sequence = "2")
	@Column(allowsNull = "true")
	public String getOtherLimitingFactors() {
		return otherLimitingFactors;
	}

	public void setOtherLimitingFactors(final String otherLimitingFactors) {
		this.otherLimitingFactors = otherLimitingFactors;
	}

	@Property()
	@PropertyLayout(multiLine = 3, labelPosition = LabelPosition.TOP)
	@MemberOrder(name = "Driving", sequence = "3")
	@Column(allowsNull = "true")
	public String getDriversLicence() {
		return driversLicence;
	}

	public void setDriversLicence(final String driversLicence) {
		this.driversLicence = driversLicence;
	}

	@Property()
	@PropertyLayout(labelPosition = LabelPosition.TOP)
	@MemberOrder(name = "Driving", sequence = "4")
	@Column(allowsNull = "true")
	public String getDrivingAbility() {
		return drivingAbility;
	}

	public void setDrivingAbility(final String drivingAbility) {
		this.drivingAbility = drivingAbility;
	}

	@Property()
	@PropertyLayout(labelPosition = LabelPosition.TOP)
	@MemberOrder(name = "Driving", sequence = "5")
	@Column(allowsNull = "true")
	public String getDrivingConfidence() {
		return drivingConfidence;
	}

	public void setDrivingConfidence(final String drivingConfidence) {
		this.drivingConfidence = drivingConfidence;
	}

	@Property()
	@PropertyLayout(labelPosition = LabelPosition.TOP)
	@MemberOrder(sequence = "6")
	@Column(allowsNull = "true")
	public String getPlaceOfOrigin() {
		return placeOfOrigin;
	}

	public void setPlaceOfOrigin(final String placeOfOrigin) {
		this.placeOfOrigin = placeOfOrigin;
	}

	@Property()
	@PropertyLayout(labelPosition = LabelPosition.TOP)
	@MemberOrder(sequence = "7")
	@Column(allowsNull = "true")
	public LocalDate getDateOfSettlement() {
		return dateOfSettlement;
	}

	public void setDateOfSettlement(final LocalDate dateOfSettlement) {
		this.dateOfSettlement = dateOfSettlement;
	}

	@Property()
	@PropertyLayout(multiLine = 3, labelPosition = LabelPosition.TOP)
	@MemberOrder(name = "Friends and Relatives", sequence = "8")
	@Column(allowsNull = "true")
	public String getCloseRelatives() {
		return closeRelatives;
	}

	public void setCloseRelatives(final String closeRelatives) {
		this.closeRelatives = closeRelatives;
	}

	@Property()
	@PropertyLayout(labelPosition = LabelPosition.TOP)
	@MemberOrder(name = "Friends and Relatives", sequence = "9")
	@Column(allowsNull = "true")
	public Integer getCloseRelativeAndFriendCount() {
		return closeRlFrCount;
	}

	public void setCloseRelativeAndFriendCount(Integer count) {
		this.closeRlFrCount = count;
	}

	@Property()
	@PropertyLayout(labelPosition = LabelPosition.TOP)
	@MemberOrder(name = "Friends and Relatives", sequence = "10")
	@Column(allowsNull = "true")
	public String getProximityOfRelatives() {
		return proximityOfRelatives;
	}

	public void setProximityOfRelatives(final String proximityOfRelatives) {
		this.proximityOfRelatives = proximityOfRelatives;
	}

	@Property()
	@PropertyLayout(labelPosition = LabelPosition.TOP)
	@MemberOrder(name = "Friends and Relatives", sequence = "11")
	@Column(allowsNull = "true")
	public String getProximityOfFriends() {
		return proximityOfFriends;
	}

	public void setProximityOfFriends(final String proximityOfFriends) {
		this.proximityOfFriends = proximityOfFriends;
	}

	@Property()
	@PropertyLayout(multiLine = 3, labelPosition = LabelPosition.TOP)
	@MemberOrder(name = "Involvement", sequence = "12")
	@Column(allowsNull = "true")
	public String getInvolvementInGroupsClubs() {
		return involvementGC;
	}

	public void setInvolvementInGroupsClubs(final String involvement) {
		this.involvementGC = involvement;
	}

	@Property()
	@PropertyLayout(multiLine = 3, labelPosition = LabelPosition.TOP)
	@MemberOrder(name = "Involvement", sequence = "13")
	@Column(allowsNull = "true")
	public String getInvolvementInInterestsHobbies() {
		return involvementIH;
	}

	public void setInvolvementInInterestsHobbies(final String involvmentInInterestsHobbies) {
		this.involvementIH = involvmentInInterestsHobbies;
	}
}
