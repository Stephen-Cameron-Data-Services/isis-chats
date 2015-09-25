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
import javax.annotation.PostConstruct;
import org.isisaddons.module.poly.dom.PolymorphicAssociationLink;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.bookmark.Bookmark;
import org.apache.isis.applib.services.bookmark.BookmarkService;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = NoteLinkableLink.class
)
public class NoteLinkableLinks {

    //region > init
    PolymorphicAssociationLink.Factory<Note,NoteLinkable,NoteLinkableLink,NoteLinkableLink.InstantiateEvent> linkFactory;

    @PostConstruct
    public void init() {
        linkFactory = container.injectServicesInto(
                new PolymorphicAssociationLink.Factory<>(
                        this,
                        Note.class,
                        NoteLinkable.class,
                        NoteLinkableLink.class,
                        NoteLinkableLink.InstantiateEvent.class
                ));

    }
    //endregion

    //region > findByNote (programmatic)
    @Programmatic
    public List<NoteLinkableLink> findByNote(final Note aNote) {
        return container.allMatches(
                new QueryDefault<>(NoteLinkableLink.class,
                        "findByNote",
                        "note", aNote));
    }
    //endregion

    //region > findByLinkable (programmatic)
    @Programmatic
    public List<NoteLinkableLink> findByLinkable(final NoteLinkable noteLinkable) {
        if(noteLinkable == null) {
            return null;
        }
        final Bookmark bookmark = bookmarkService.bookmarkFor(noteLinkable);
        if(bookmark == null) {
            return null;
        }
        return container.allMatches(
                new QueryDefault<>(NoteLinkableLink.class,
                        "findByLinkable",
                        "linkableObjectType", bookmark.getObjectType(),
                        "linkableIdentifier", bookmark.getIdentifier()));
    }
    //endregion

    //region > createLink
    @Programmatic
    public void createLink(final Note aNote, final NoteLinkable linkable) {
        linkFactory.createLink(aNote, linkable);
    }
    //endregion

    //region > injected services

    @javax.inject.Inject
    private DomainObjectContainer container;

    @javax.inject.Inject
    private BookmarkService bookmarkService;

    //endregion

}
