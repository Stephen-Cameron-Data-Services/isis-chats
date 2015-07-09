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
package au.com.scds.chats.dom.modules.participant;

import java.util.List;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;

@DomainService(repositoryFor = Participant.class)
@DomainServiceLayout(menuOrder = "20")
public class Participants {

    //region > listAll (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<Participant> listAll() {
        return container.allInstances(Participant.class);
    }
    //endregion
    
    //region > listActive (action)
    /*@Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<Participant> listActive() {
        //return container.allInstances(Participant.class);
        return container.allMatches(
                new QueryDefault<>(
                        Participant.class,
                        "findActive",
                        "status", Status.ACTIVE));
    }*/
    //endregion
    
    //region > listExited (action)
    /*@Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "2")
    public List<Participant> listExited() {
        //return container.allInstances(Participant.class);
        return container.allMatches(
                new QueryDefault<>(
                        Participant.class,
                        "findExited",
                        "status", "exited"));
    }*/
    //endregion

    //region > findByName (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "3")
    public List<Participant> findByName(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        Participant.class,
                        "findByName",
                        "name", name));
    }
    //endregion

    //region > create (action)
    @MemberOrder(sequence = "4")
    public Participant create(
            final @ParameterLayout(named="Full Name") String name) {
        final Participant obj = container.newTransientInstance(Participant.class);
        obj.setFullname(name);
        container.persistIfNotAlready(obj);
        return obj;
    }
    //endregion
    
    //region > injected services

    @javax.inject.Inject 
    DomainObjectContainer container;

    //endregion
}