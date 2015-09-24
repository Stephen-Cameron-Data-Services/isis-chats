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
import javax.jdo.annotations.InheritanceStrategy;
import com.google.common.eventbus.Subscribe;

import au.com.scds.chats.dom.module.volunteer.Volunteer;

import org.apache.isis.applib.AbstractSubscriber;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.bookmark.BookmarkService;

@javax.jdo.annotations.PersistenceCapable()
@javax.jdo.annotations.Inheritance(
        strategy = InheritanceStrategy.NEW_TABLE)
@DomainObject(
        objectType = "NoteLinkablePrimaryLinkForVolunteer"
)
public class NoteLinkablePrimaryLinkForVolunteer extends NoteLinkablePrimaryLink {

    @DomainService(nature = NatureOfService.DOMAIN)
    public static class InstantiationSubscriber extends AbstractSubscriber {
        @Programmatic
        @Subscribe
        public void on(final InstantiateEvent ev) {
            if(ev.getPolymorphicReference() instanceof Volunteer) {
                ev.setSubtype(NoteLinkablePrimaryLinkForVolunteer.class);
            }
        }
    }

    @Override
    public void setPolymorphicReference(final NoteLinkable polymorphicReference) {
        super.setPolymorphicReference(polymorphicReference);
        setVolunteer((Volunteer) polymorphicReference);
    }

    //region > volunteer (property)
    private Volunteer volunteer;

    @Column(
            allowsNull = "false",
            name = "volunteer_id"
    )
    @MemberOrder(sequence = "1")
    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(final Volunteer volunteer) {
        this.volunteer = volunteer;
    }
    //endregion

    //region > injected services
    @javax.inject.Inject
    private BookmarkService bookmarkService;
    //endregion

}
