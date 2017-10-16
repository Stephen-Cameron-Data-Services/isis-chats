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
package au.com.scds.chats.dom.note;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.NatureOfService;
import org.incode.module.note.dom.impl.notablelink.NotableLink;
import org.incode.module.note.dom.impl.notablelink.NotableLinkRepository;
import org.incode.module.note.dom.impl.note.T_addNote;
import org.incode.module.note.dom.impl.note.T_notes;
import org.incode.module.note.dom.impl.note.T_removeNote;

import au.com.scds.chats.dom.activity.ActivityEvent;


@PersistenceCapable(identityType = IdentityType.DATASTORE, schema="chats", table="notablelinkforactivityevent")
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@DomainObject()
public class NoteableLinkForActivityEvent extends NotableLink {

    private ActivityEvent object;
    @Column( allowsNull = "false", name = "object_id" )
    public ActivityEvent getObject() {                                         
        return object;
    }
    public void setObject(final ActivityEvent object) {
        this.object = object;
    }

    public Object getNotable() {                                                    
        return getObject();
    }
    protected void setNotable(final Object object) {
        setObject((ActivityEvent) object);
    }

    @DomainService(nature = NatureOfService.DOMAIN)
    public static class SubtypeProvider
                extends NotableLinkRepository.SubtypeProviderAbstract {             
        public SubtypeProvider() {
            super(ActivityEvent.class, NoteableLinkForActivityEvent.class);
        }
    }

    @Mixin
    public static class _notes extends T_notes<ActivityEvent> {                    
        public _notes(final ActivityEvent notable) {
            super(notable);
        }
    }
    @Mixin
    public static class _addNote extends T_addNote<ActivityEvent> {
        public _addNote(final ActivityEvent notable) {
            super(notable);
        }
    }
    @Mixin
    public static class _removeNote extends T_removeNote<ActivityEvent> {
        public _removeNote(final ActivityEvent notable) {
            super(notable);
        }
    }
}
