package au.com.scds.chats;

import java.util.Set;

import org.apache.isis.applib.Module;
import org.apache.isis.applib.ModuleAbstract;
import org.isisaddons.module.security.SecurityModule;

import com.google.common.collect.Sets;

import au.com.scds.eventschedule.base.EventScheduleBaseModule;

public class ChatsModule extends ModuleAbstract {
	
	@Override
    public Set<Module> getDependencies() {
        return Sets.newHashSet(new EventScheduleBaseModule());
    }
	
	@Override
    public Set<Class<?>> getAdditionalModules() {
        return Sets.newHashSet(SecurityModule.class);
    }

}
