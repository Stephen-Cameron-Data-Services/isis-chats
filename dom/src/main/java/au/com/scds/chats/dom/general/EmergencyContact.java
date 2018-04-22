package au.com.scds.chats.dom.general;

import java.sql.Timestamp;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.services.timestamp.Timestampable;
import org.isisaddons.module.security.dom.tenancy.HasAtPath;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.ChatsDomainEntitiesService;
import au.com.scds.chats.dom.ChatsEntity;
import au.com.scds.chats.dom.general.names.Region;
import lombok.Getter;
import lombok.Setter;

@DomainObject()
@PersistenceCapable(identityType = IdentityType.DATASTORE, schema="chats", table="emergencycontact")
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class EmergencyContact implements ChatsEntity, Timestampable, HasAtPath{
	
	@Column(allowsNull = "false")
	@Getter
	@Setter
	private ChatsPerson person;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String name;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String address;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String phone;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String relationship;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String createdBy;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private DateTime createdOn;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private DateTime lastModifiedOn;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String lastModifiedBy;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private Region region;
	

	public String title() {
		return getName() + " (" + getRelationship() + ")";
	}
	

	@Override
	public void setUpdatedBy(String updatedBy) {
		chatsService.setUpdatedBy(this, updatedBy);
	}

	@Override
	public void setUpdatedAt(Timestamp updatedAt) {
		chatsService.setUpdatedAt(this, updatedAt);
	}

	@Override
	public String getAtPath() {
		return chatsService.getAtPath(this);
	}

	@Inject
	ChatsDomainEntitiesService chatsService;

}
