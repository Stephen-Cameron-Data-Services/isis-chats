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
package au.com.scds.chats.dom.module.note;

import java.util.List;
import javax.inject.Inject;
import javax.jdo.annotations.NotPersistent;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@DomainService(nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)
public class NoteLinkablePrimaryContributions {

	// SC create a note and make object primary linkable

	@ActionLayout(contributed = Contributed.AS_ACTION)
	@MemberOrder(name = "notes", sequence = "1")
	public Note addNote(@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Note title") final String title, final NoteLinkable noteLinkablePrimary) {
		Note aNote = notes.create(title);
		noteLinkablePrimaryLinks.createLink(aNote, noteLinkablePrimary);
		return aNote;
	}

	//

	// region > primary (contributed property)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(contributed = Contributed.AS_ASSOCIATION)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(hidden = Where.ALL_TABLES,named="Primary Related")
	@MemberOrder(sequence="2")
	public NoteLinkable primaryLinkable(final Note aNote) {
		final NoteLinkablePrimaryLink linkablePrimaryLink = noteLinkablePrimaryLinks.findByNote(aNote);
		return linkablePrimaryLink != null ? linkablePrimaryLink.getPolymorphicReference() : null;
	}

	// endregion

	// region > notes (derived collection)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(contributed = Contributed.AS_ASSOCIATION)
	public List<Note> notes(final NoteLinkable noteLinkable) {
		final List<NoteLinkablePrimaryLink> links = noteLinkablePrimaryLinks.findByLinkable(noteLinkable);
		return Lists.newArrayList(Iterables.transform(links, NoteLinkablePrimaryLink.Functions.GET_CASE));
	}

	// endregion

	/**
	 * SC this is not needed as we set the primary as owner of a Note //region >
	 * makePrimary (contributed action), resetPrimary (contributed action)
	 * 
	 * @Action( semantics = SemanticsOf.IDEMPOTENT ) public Note makePrimary(
	 *          final Note aNote, final NoteLinkable noteLinkable) { return
	 *          setPrimary(aNote, noteLinkable); }
	 * @Action( semantics = SemanticsOf.IDEMPOTENT ) public Note resetPrimary(
	 *          final Note aNote) { return setPrimary(aNote, null); }
	 * 
	 *          private Note setPrimaryLinkable(final Note aNote, final
	 *          NoteLinkable noteLinkable) { final NoteLinkablePrimaryLink
	 *          linkableLink = noteLinkablePrimaryLinks.findByNote(aNote);
	 *          if(linkableLink != null) {
	 *          container.removeIfNotAlready(linkableLink); } if(noteLinkable !=
	 *          null) { noteLinkablePrimaryLinks.createLink(aNote, noteLinkable);
	 *          } return aNote; }
	 * 
	 *          public boolean hideMakePrimary(final Note aNote, final
	 *          NoteLinkable noteLinkable) { // don't contribute to noteLinkable
	 *          return noteLinkable != null; }
	 * 
	 *          public List<NoteLinkable> choices1MakePrimary(final Note aNote) {
	 *          return noteLinkableContributions.linkables(aNote); } //endregion
	 */
	@Inject
	private Notes notes;
	@Inject
	private NoteLinkableContributions noteLinkableContributions;
	@Inject
	private NoteLinkablePrimaryLinks noteLinkablePrimaryLinks;
	@Inject
	private DomainObjectContainer container;

}
