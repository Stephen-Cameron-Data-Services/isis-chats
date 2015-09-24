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

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import com.google.common.base.Function;
import org.isisaddons.module.poly.dom.PolymorphicAssociationLink;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Programmatic;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Inheritance(
        strategy = InheritanceStrategy.NEW_TABLE)
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findByNote", language = "JDOQL",
                value = "SELECT "
                        + "FROM au.com.scds.chats.dom.module.note.NoteLinkablePrimaryLink "
                        + "WHERE note == :note"),
        @javax.jdo.annotations.Query(
                name = "findByLinkable", language = "JDOQL",
                value = "SELECT "
                        + "FROM au.com.scds.chats.dom.module.note.NoteLinkablePrimaryLink "
                        + "WHERE linkableObjectType == :linkableObjectType "
                        + "   && linkableIdentifier == :linkableIdentifier "),
        @javax.jdo.annotations.Query(
                name = "findByNoteAndLinkable", language = "JDOQL",
                value = "SELECT "
                        + "FROM au.com.scds.chats.dom.module.note.NoteLinkablePrimaryLink "
                        + "WHERE note == :note "
                        + "   && linkableObjectType == :linkableObjectType "
                        + "   && linkableIdentifier == :linkableIdentifier ")
})
@javax.jdo.annotations.Unique(name="NoteLinkablePrimaryLink_note_linkable_UNQ", members = {"note","linkableObjectType","linkableIdentifier"})
@DomainObject(
        objectType = "NoteLinkablePrimaryLink"
)
public abstract class NoteLinkablePrimaryLink extends PolymorphicAssociationLink<Note, NoteLinkable, NoteLinkablePrimaryLink> {

    public static class InstantiateEvent
            extends PolymorphicAssociationLink.InstantiateEvent<Note, NoteLinkable, NoteLinkablePrimaryLink> {

        public InstantiateEvent(final Object source, final Note aNote, final NoteLinkable linkable) {
            super(NoteLinkablePrimaryLink.class, source, aNote, linkable);
        }
    }

    //region > constructor
    public NoteLinkablePrimaryLink() {
        super("{subject} primary {polymorphicReference}");
    }
    //endregion

    //region > SubjectPolymorphicReferenceLink API
    @Override
    @Programmatic
    public Note getSubject() {
        return getNote();
    }

    @Override
    @Programmatic
    public void setSubject(final Note subject) {
        setNote(subject);
    }

    @Override
    @Programmatic
    public String getPolymorphicObjectType() {
        return getLinkableObjectType();
    }

    @Override
    @Programmatic
    public void setPolymorphicObjectType(final String polymorphicObjectType) {
        setLinkableObjectType(polymorphicObjectType);
    }

    @Override
    @Programmatic
    public String getPolymorphicIdentifier() {
        return getLinkableIdentifier();
    }

    @Override
    @Programmatic
    public void setPolymorphicIdentifier(final String polymorphicIdentifier) {
        setLinkableIdentifier(polymorphicIdentifier);
    }
    //endregion

    //region > note (property)
    @NotPersistent // because we (have to) have a non-standard name for this field
    private Note _note;
    @Column(
            allowsNull = "false",
            name = "note_id"
    )
    public Note getNote() {
        return _note;
    }

    public void setNote(final Note aNote) {
        this._note = aNote;
    }
    //endregion

    //region > linkableObjectType (property)
    private String linkableObjectType;

    @Column(allowsNull = "false", length = 255)
    public String getLinkableObjectType() {
        return linkableObjectType;
    }

    public void setLinkableObjectType(final String linkableObjectType) {
        this.linkableObjectType = linkableObjectType;
    }
    //endregion

    //region > linkableIdentifier (property)
    private String linkableIdentifier;

    @Column(allowsNull = "false", length = 255)
    public String getLinkableIdentifier() {
        return linkableIdentifier;
    }

    public void setLinkableIdentifier(final String linkableIdentifier) {
        this.linkableIdentifier = linkableIdentifier;
    }
    //endregion

    public static class Functions {
        public static Function<NoteLinkablePrimaryLink, Note> GET_CASE = new Function<NoteLinkablePrimaryLink, Note>() {
            @Override
            public Note apply(final NoteLinkablePrimaryLink input) {
                return input.getSubject();
            }
        };
        public static Function<NoteLinkablePrimaryLink, NoteLinkable> GET_CONTENT = new Function<NoteLinkablePrimaryLink, NoteLinkable>() {
            @Override
            public NoteLinkable apply(final NoteLinkablePrimaryLink input) {
                return input.getPolymorphicReference();
            }
        };
    }

}
