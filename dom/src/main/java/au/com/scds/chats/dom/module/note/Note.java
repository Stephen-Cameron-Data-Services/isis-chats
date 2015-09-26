/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
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
/*
 * The Note module is a copy of the Case Polymorphic Link functionality
 * in the Apache Isis Addons 'poly' module
 * 
 * See: https://github.com/isisaddons/isis-module-poly
 */
package au.com.scds.chats.dom.module.note;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.jdo.annotations.Column;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.LabelPosition;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

import au.com.scds.chats.dom.AbstractDomainEntity;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
/*
 * @javax.jdo.annotations.Version( strategy=VersionStrategy.VERSION_NUMBER,
 * column="version")
 */
@javax.jdo.annotations.Queries({ @javax.jdo.annotations.Query(name = "find", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.note.Note "),
		@javax.jdo.annotations.Query(name = "findByTitle", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.note.Note " + "WHERE title.indexOf(:title) >= 0 ") })
@DomainObject(objectType = "Note")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class Note extends AbstractDomainEntity implements NoteLinkable, Comparable<Note> {

	// region > identificatiom
	@PropertyLayout(hidden = Where.ALL_TABLES)
	public String title() {
		return "Note: " + getSubject();
	}

	// endregion

	// region > name (property)

	private String subject;

	@Column(allowsNull = "false", length = 40)
	@MemberOrder(sequence = "1")
	public String getSubject() {
		return subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	// endregion

	// region > updateTitle (action)
	/*
	 * public Note updateTitle(
	 * 
	 * @Parameter(maxLength = 40)
	 * 
	 * @ParameterLayout(named = "New title") final String title) {
	 * setTitle(title); return this; }
	 * 
	 * public String default0UpdateTitle() { return getTitle(); }
	 */
	// endregion

	// {{ Content (property)
	private String content;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "3")
	@PropertyLayout(multiLine = 20, labelPosition = LabelPosition.TOP, hidden = Where.ALL_TABLES)
	public String getContent() {
		return content;
	}

	public void setContent(final String contents) {
		this.content = contents;
	}

	// }}

	// {{ IsActionable (property)

	private Boolean isActionable;

	@Column(allowsNull = "false", defaultValue = "false")
	@MemberOrder(sequence = "4")
	public Boolean getIsActionable() {
		return isActionable;
	}

	public void setIsActionable(final Boolean isActionable) {
		this.isActionable = isActionable;
	}

	// }}

	// region > compareTo

	@Override
	public int compareTo(final Note other) {
		return ObjectContracts.compare(this, other, "createdDateTime", "title");
	}

	// endregion

	// region > injected services

	@javax.inject.Inject
	@SuppressWarnings("unused")
	private DomainObjectContainer container;

	@javax.inject.Inject
	@SuppressWarnings("unused")
	private NoteLinkableLinks caseContentLinks;
	// endregion

}
