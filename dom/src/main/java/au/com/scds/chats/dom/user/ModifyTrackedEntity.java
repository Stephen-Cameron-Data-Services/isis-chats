package au.com.scds.chats.dom.user;

import org.joda.time.DateTime;

public interface ModifyTrackedEntity {
    void setLastModifiedBy(String username);
    void setLastModifiedOn(DateTime modifiedOn);
}
