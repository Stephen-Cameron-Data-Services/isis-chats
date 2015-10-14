package au.com.scds.chats.dom.module.note;

import java.util.List;

import javax.jdo.annotations.NotPersistent;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Nature;

import org.joda.time.LocalDate;

@DomainObject(nature = Nature.VIEW_MODEL)
public class NotesFilter {
	
	// {{ StartDate (property)
	private LocalDate startDate;

	@MemberOrder(sequence = "1")
	@NotPersistent
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(final LocalDate startDate) {
		this.startDate = startDate;
	}
	// }}

    @MemberOrder(sequence = "1")
    public List<Note> notes() {
        return container.allInstances(Note.class);
    }
    
    @javax.inject.Inject
    DomainObjectContainer container;
}
