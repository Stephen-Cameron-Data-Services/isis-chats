/*
 *
 *  Copyright 2015 Stephen Cameron Data Services
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package au.com.scds.chats.dom.participant;

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

	private Participant participant;
	private String lifeStory;
	private String experiences;
	private String hobbies;
	private String interests;

	public String title() {
		return "Life History of Participant: " + getParticipant().getPerson().getFullname();
	}

	@Property()
	@PropertyLayout()
	@MemberOrder(sequence = "100")
	@Column(allowsNull = "false")
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		if (getParticipant() == null && participant != null)
			this.participant = participant;
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
