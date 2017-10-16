package au.com.scds.chats.dom.dex.reference;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@DomainObject(objectType="chats.assessmentdomain")
public class AssessmentDomain implements Comparable<AssessmentDomain>{

    private AssessmentScoreType scoreType;
	private String domainCode;
	private String description;
	@Persistent(mappedBy="domain")
	private SortedSet<AssessmentDomainScore> scoreCodes = new TreeSet<>();

	public String title(){
		return getDomainCode();
	}
	
	@Property(hidden=Where.ALL_TABLES)
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public AssessmentScoreType getScoreType() {
		return scoreType;
	}

	public void setScoreType(AssessmentScoreType assessment) {
		this.scoreType = assessment;
	}

	@Property()
	@MemberOrder(sequence = "2")
	@Column(allowsNull = "false")
	@PrimaryKey()
	public String getDomainCode() {
		return domainCode;
	}

	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}

	@Property()
	@MemberOrder(sequence = "3")
	@Column(allowsNull = "false")
	public String getDescription() {
		return description;
	}

	public void setDescription(String applicableFor) {
		this.description = applicableFor;
	}

	@Property()
	@MemberOrder(sequence = "4")
	@CollectionLayout(render=RenderType.EAGERLY)
	public SortedSet<AssessmentDomainScore> getScoreCodes() {
		return scoreCodes;
	}

	public void setScoreCodes(SortedSet<AssessmentDomainScore> scoreCodes) {
		this.scoreCodes = scoreCodes;
	}

	@Override
	public int compareTo(AssessmentDomain o) {
		return ObjectContracts.compare(this, o, "domainCode");
	}
}
