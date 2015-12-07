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
package au.com.scds.chats.dom;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.incode.module.note.dom.api.notable.Notable;
import org.incode.module.note.dom.impl.note.Note;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancyPathEvaluator;

//@DomainService(nature = NatureOfService.DOMAIN)
//NOT IN USE
public class ApplicationTenancyPathEvaluatorForChats implements ApplicationTenancyPathEvaluator {
	@Override
	public boolean handles(final Class<?> cls) {
		System.out.println(">>>handles");
		if(AbstractChatsDomainEntity.class.isAssignableFrom(cls))
			return true;
		if(Note.class.isAssignableFrom(cls))
			return true;
		return false;
	}

	@Override
	public String applicationTenancyPathFor(final Object domainObject) {
		// always safe to do, per the handles(...) method earlier
		if(domainObject instanceof AbstractChatsDomainEntity){
			System.out.print("applicationTenancyPathFor");
			return ((AbstractChatsDomainEntity)domainObject).getApplicationTenancy().getPath();
		}
		if(domainObject instanceof Note){
			System.out.print("applicationTenancyPathForNote");
			Notable noteable = ((Note)domainObject).getNotable();
			return ((AbstractChatsDomainEntity)noteable).getApplicationTenancy().getPath();
		}
		return null;
	}
}
