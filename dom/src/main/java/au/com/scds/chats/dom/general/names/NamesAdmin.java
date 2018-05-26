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
package au.com.scds.chats.dom.general.names;

import java.util.List;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;

@DomainService(objectType="NamesAdmin", nature=NatureOfService.VIEW_MENU_ONLY)
public class NamesAdmin {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
    public Names nameLists() {
        return new Names();
    }
}
