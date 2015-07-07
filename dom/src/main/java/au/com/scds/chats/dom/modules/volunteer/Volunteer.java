package au.com.scds.chats.dom.modules.volunteer;

import javax.jdo.annotations.Column;

import org.apache.isis.applib.annotation.MemberOrder;


public class Volunteer {

	// {{ Status (property)
	private Status status = Status.ACTIVE;

	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public Status getStatus() {
		return status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}
	// }}
}
