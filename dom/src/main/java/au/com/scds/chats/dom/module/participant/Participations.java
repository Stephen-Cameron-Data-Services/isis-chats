package au.com.scds.chats.dom.module.participant;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;

import au.com.scds.chats.dom.module.activity.Activity;

/**
 * Created by jvanderwal on 02/09/2015.
 */
@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Participation.class)
public class Participations {

    public Participation newParticipation(Activity activity, Participant participant){
        Participation  participation = container.newTransientInstance(Participation.class);
        participation.setActivity(activity);
        participation.setParticipant(participant);
        container.persistIfNotAlready(participation);
        return participation;
    }

    @Inject
    DomainObjectContainer container;
}
