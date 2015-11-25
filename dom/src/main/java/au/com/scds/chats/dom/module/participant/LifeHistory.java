package au.com.scds.chats.dom.module.participant;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Version;
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
import org.apache.isis.applib.annotation.Where;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;

@DomainObject(objectType = "LIFE-HISTORY")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
@PersistenceCapable(identityType = IdentityType.DATASTORE)
public class LifeHistory extends AbstractChatsDomainEntity {

	private Participant parent;
	private String lifeStory;
	private String experiences;
	private String hobbies;
	private String interests;

	public String title() {
		return "Life History of Participant: " + parent.getPerson().getFullname();
	}

	@Property()
	@PropertyLayout()
	@MemberOrder(sequence = "100")
	@Column(allowsNull = "false")
	public Participant getParentParticipant() {
		return parent;
	}

	public void setParentParticipant(Participant participant) {
		if (this.parent == null && participant != null)
			this.parent = participant;
	}

	@Property()
	@PropertyLayout(multiLine = 10, labelPosition = LabelPosition.TOP)
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "true")
	public String getLifeStory() {
		return lifeStory;
	}

	public void setLifeStory(final String lifeStory) {
		this.lifeStory = lifeStory;
	}

	@Property()
	@PropertyLayout(multiLine = 10, labelPosition = LabelPosition.TOP)
	@MemberOrder(sequence = "2")
	@Column(allowsNull = "true")
	public String getLifeExperiences() {
		return experiences;
	}

	public void setLifeExperiences(final String experiences) {
		this.experiences = experiences;
	}

	@Property()
	@PropertyLayout(multiLine = 2, labelPosition = LabelPosition.TOP)
	@MemberOrder(sequence = "3")
	@Column(allowsNull = "true")
	public String getHobbies() {
		return hobbies;
	}

	public void setHobbies(final String hobbies) {
		this.hobbies = hobbies;
	}

	@Property()
	@PropertyLayout(multiLine = 2, labelPosition = LabelPosition.TOP)
	@MemberOrder(sequence = "4")
	@Column(allowsNull = "true")
	public String getInterests() {
		return interests;
	}

	public void setInterests(final String interests) {
		this.interests = interests;
	}

	@Inject
	@SuppressWarnings("unused")
	private DomainObjectContainer container;
}
