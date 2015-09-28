package au.com.scds.chats.dom.user;

import org.joda.time.DateTime;

public interface CreateTrackedEntity {
    void setCreatedBy(String createdBy);
    void setCreatedOn(DateTime createdOn);
}
