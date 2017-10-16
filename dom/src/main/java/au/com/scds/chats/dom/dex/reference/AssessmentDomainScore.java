package au.com.scds.chats.dom.dex.reference;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@DomainObject(objectType="chats.assessmentdomainscore")
public class AssessmentDomainScore implements Comparable<AssessmentDomainScore>{

	private AssessmentDomain domain;
	private String scoreCode;
	private String description;
	
	public String title(){
		return getScoreCode();
	}

	@Property(hidden=Where.ALL_TABLES)
	@MemberOrder(sequence = "1")
	@Column(allowsNull="false")
	public AssessmentDomain getDomain() {
		return domain;
	}

	public void setDomain(AssessmentDomain domain) {
		this.domain = domain;
	}

	@Property()
	@MemberOrder(sequence = "2")
	@PrimaryKey()
	public String getScoreCode() {
		return scoreCode;
	}

	public void setScoreCode(String scoreCode) {
		this.scoreCode = scoreCode;
	}

	@Property()
	@MemberOrder(sequence = "3")
	@Column(name="scoreDescription", allowsNull = "false")
	public String getDescription() {
		return description;
	}

	public void setDescription(String applicableFor) {
		this.description = applicableFor;
	}

	@Override
	public int compareTo(AssessmentDomainScore o) {
		return ObjectContracts.compare(this,o,"scoreCode");
	}
}
