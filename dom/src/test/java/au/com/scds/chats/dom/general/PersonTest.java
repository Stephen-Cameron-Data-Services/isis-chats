package au.com.scds.chats.dom.general;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;

import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participation;

public class PersonTest {

	@Rule
	public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

	@Mock
	DomainObjectContainer mockContainer;

	Persons persons;
	Person person;

	public static class PersonTest_Tests extends PersonTest {

		@Test
		public void createPersonHappyCaseTest() {
			
			persons = new Persons(mockContainer);
			person = new Person();
			
			context.checking(new Expectations() {
				{
					oneOf(mockContainer).newTransientInstance(Person.class);
					will(returnValue(person));
					oneOf(mockContainer).persistIfNotAlready(person);
					oneOf(mockContainer).flush();
				}
			});
			
			try {
				person = persons.createPerson("Test", "Person", new LocalDate(1900, 01, 01), Sex.MALE);
			} catch (Exception e) {
				e.printStackTrace();
				fail( "Create Person threw Exception: " + e.getMessage());
			}
			assertThat(person.getFirstname()).isEqualTo("Test");
			assertThat(person.getSurname()).isEqualTo("Person");
			assertThat(person.getBirthdate()).isEqualTo(new LocalDate(1900, 01, 10));
			assertThat(person.getSex()).isEqualTo(Sex.MALE);
			assertThat(person.getSlk()).isEqualTo("EROES010119001");
		}
	}
}