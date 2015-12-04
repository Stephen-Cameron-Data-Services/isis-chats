package au.com.scds.chats.dom.module.note;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.*;
import org.incode.module.note.dom.impl.note.Note;
import org.incode.module.note.dom.impl.note.NoteRepository;


@DomainService()
@DomainServiceLayout(named = "Notes", menuOrder = "80")
public class Notes {

	public List<Note> allNotes(){
		return notesRepo.allNotes();
	}
	
	@Inject
	NoteRepository notesRepo;

}
