package au.com.scds.chats.dom.note;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Nature;

@DomainObject(objectType="chats.notesfinder", nature=Nature.VIEW_MODEL)
public class NotesFinder {
	
	public String title(){
		return "Notes Filter Interface, nothing here yet";
	}

}
