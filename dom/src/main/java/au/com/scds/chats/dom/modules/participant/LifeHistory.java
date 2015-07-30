package au.com.scds.chats.dom.modules.participant;

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
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.TitleBuffer;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@DomainObject(objectType = "LIFE-HISTORY")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class LifeHistory {

	public String title() {
		return "Life History of Participant: " + parent.getPerson().getFullname() ;
	}
	
	// {{ ParentParticipant (property)
	private Participant parent;

	@Column(allowsNull = "false")
	@Property(editing=Editing.DISABLED)
	@MemberOrder(sequence = "100")
	public Participant getParentParticipant() {
		return parent;
	}
	
	public void setParentParticipant(Participant participant) {
		if (this.parent == null && participant != null)
			this.parent = participant;
	}

	// {{ LifeStory (property)
	private String lifeStory;

	@Column(allowsNull="true")
	@MemberOrder(sequence = "1")
	public String getLifeStory() {
		return lifeStory;
	}

	public void setLifeStory(final String lifeStory) {
		this.lifeStory = lifeStory;
	}

	// }}

	// {{ LifeExperiences (property)
	private String experiences;

	@Column(allowsNull="true")
	@MemberOrder(sequence = "2")
	public String getLifeExperiences() {
		return experiences;
	}

	public void setLifeExperiences(final String experiences) {
		this.experiences = experiences;
	}

	// }}

	// {{ Hobbies (property)
	private String hobbies;

	@Column(allowsNull="true")
	@MemberOrder(sequence = "3")
	public String getHobbies() {
		return hobbies;
	}

	public void setHobbies(final String hobbies) {
		this.hobbies = hobbies;
	}

	// }}

	// {{ Interests (property)
	private String interests;

	@Column(allowsNull="true")
	@MemberOrder(sequence = "4")
	public String getInterests() {
		return interests;
	}

	public void setInterests(final String interests) {
		this.interests = interests;
	}

	// }}

	@javax.inject.Inject
	@SuppressWarnings("unused")
	private DomainObjectContainer container;



}
