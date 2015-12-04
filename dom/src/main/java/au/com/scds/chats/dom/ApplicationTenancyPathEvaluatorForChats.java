package au.com.scds.chats.dom;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.incode.module.note.dom.api.notable.Notable;
import org.incode.module.note.dom.impl.note.Note;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancyPathEvaluator;

//@DomainService(nature = NatureOfService.DOMAIN)
public class ApplicationTenancyPathEvaluatorForChats implements ApplicationTenancyPathEvaluator {
	@Override
	public boolean handles(final Class<?> cls) {
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
