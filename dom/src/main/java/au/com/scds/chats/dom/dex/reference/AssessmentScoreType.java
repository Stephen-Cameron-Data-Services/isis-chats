package au.com.scds.chats.dom.dex.reference;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.RenderType;

@PersistenceCapable(identityType = IdentityType.APPLICATION, schema = "chats", table = "assessmentscoretype")
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Query(name = "all", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.dex.reference.AssessmentScoreType")
@DomainObject()
public class AssessmentScoreType {

	private String scoreType;
	private String applicableFor;
	@Persistent(mappedBy = "scoreType")
	private SortedSet<AssessmentDomain> domains = new TreeSet<>();

	public String title() {
		return getScoreType();
	}

	@Property()
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	@PrimaryKey()
	public String getScoreType() {
		return scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	@Property()
	@MemberOrder(sequence = "2")
	@Column(allowsNull = "false")
	public String getApplicableFor() {
		return applicableFor;
	}

	public void setApplicableFor(String applicableFor) {
		this.applicableFor = applicableFor;
	}

	@Property()
	@MemberOrder(sequence = "3")
	@CollectionLayout(render = RenderType.EAGERLY)
	public SortedSet<AssessmentDomain> getDomains() {
		return domains;
	}

	public void setDomains(SortedSet<AssessmentDomain> domains) {
		this.domains = domains;
	}
}