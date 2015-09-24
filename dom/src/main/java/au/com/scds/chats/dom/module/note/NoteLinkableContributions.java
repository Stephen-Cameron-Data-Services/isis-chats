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
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

@DomainService(
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY
)
public class NoteLinkableContributions {

    @ActionLayout(
            contributed = Contributed.AS_ACTION
    )
    public Note addToNote(final Note aNote, final NoteLinkable noteLinkable) {
        noteLinkableLinks.createLink(aNote, noteLinkable);
        return aNote;
    }
    
    public String disableAddToNote(final Note aNote, final NoteLinkable noteLinkable) {
        if (aNote != null) {
            // in effect, don't contribute to Note
            return "To add to a note as related, navigate to object and select 'add to note'";
        } else {
            return null;
        }
    }

    public List<Note> choices0AddToNote(final Note aNote, final NoteLinkable noteLinkable) {
        final List<Note> noteList = Lists.newArrayList(notes.listAll());
        final List<Note> currentNotes = choices0RemoveFromNote(null, noteLinkable);
        noteList.removeAll(currentNotes);
        return noteList;
    }

    private static Predicate<Note> notSameAs(final Note aNote) {
        return new Predicate<Note>() {
            @Override
            public boolean apply(final Note input) {
                return input == aNote;
            }
        };
    }
	
    public static class RemoveFromNoteDomainEvent extends ActionDomainEvent<NoteLinkableContributions> {
        public RemoveFromNoteDomainEvent(final NoteLinkableContributions source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
        public Note getNote() {
            return (Note) getArguments().get(0);
        }
        public NoteLinkable getLinkable() {
            return (NoteLinkable) getArguments().get(1);
        }
    }

    @Action(
            domainEvent = RemoveFromNoteDomainEvent.class
    )
    @ActionLayout(
            contributed = Contributed.AS_ACTION
    )
    public Note removeFromNote(final Note aNote, final NoteLinkable noteLinkable) {
        final List<NoteLinkableLink> linkableLinks = noteLinkableLinks.findByNote(aNote);
        for (NoteLinkableLink linkableLink : linkableLinks) {
            if(linkableLink.getPolymorphicReference() == noteLinkable) {
                container.removeIfNotAlready(linkableLink);
                break;
            }
        }
        return aNote;
    }

    public String disableRemoveFromNote(final Note aNote, final NoteLinkable noteLinkable) {
        if (noteLinkable != null) {
            return choices0RemoveFromNote(null, noteLinkable).isEmpty()? "Not contained in any note": null;
        } else {
            return choices1RemoveFromNote(aNote).isEmpty()? "No linkables to remove": null;
        }
    }

    public List<Note> choices0RemoveFromNote(final Note aNote, final NoteLinkable noteLinkable) {
        final List<NoteLinkableLink> linkableLinks = noteLinkableLinks.findByLinkable(noteLinkable);
        return Lists.newArrayList(Iterables.transform(linkableLinks, NoteLinkableLink.Functions.GET_CASE));
    }
    public List<NoteLinkable> choices1RemoveFromNote(final Note aNote) {
        final List<NoteLinkableLink> linkableLinks = noteLinkableLinks.findByNote(aNote);
        return Lists.newArrayList(Iterables.transform(linkableLinks, NoteLinkableLink.Functions.GET_CONTENT));
    }

    public String validateRemoveFromNote(final Note aNote, final NoteLinkable noteLinkable) {
        final List<NoteLinkableLink> linkableLinks = noteLinkableLinks.findByNote(aNote);
        for (NoteLinkableLink linkableLink : linkableLinks) {
            if(linkableLink.getPolymorphicReference() == noteLinkable) {
                return null;
            }
        }
        return "Not contained within note";
    }


    //region > linkables (derived collection)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            contributed = Contributed.AS_ASSOCIATION
    )
    @PropertyLayout(named="Related")
    public List<NoteLinkable> linkables(final Note aNote) {
        final List<NoteLinkableLink> links = noteLinkableLinks.findByNote(aNote);
        return Lists.newArrayList(
                Iterables.transform(links, NoteLinkableLink.Functions.GET_CONTENT)
        );
    }
    //endregion

    //region > notes (derived collection)
    /*@Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            contributed = Contributed.AS_ASSOCIATION
    )
    public List<Note> notes(final NoteLinkable noteLinkable) {
        final List<NoteLinkableLink> links = noteLinkableLinks.findByLinkable(noteLinkable);
        return Lists.newArrayList(
                Iterables.transform(links, NoteLinkableLink.Functions.GET_CASE)
        );
    }*/
    //endregion

    //region > title (derived property)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            contributed = Contributed.AS_ASSOCIATION
    )
    @PropertyLayout(
            hidden = Where.OBJECT_FORMS
    )
    public String title(final NoteLinkable noteLinkable) {
        return container.titleOf(noteLinkable);
    }
    //endregion

    @Inject
    private Notes notes;
    @Inject
    private NoteLinkableLinks noteLinkableLinks;
    @Inject
    private NoteLinkablePrimaryLinks noteLinkablePrimaryLinks;
    @Inject
    private DomainObjectContainer container;

}
