package au.com.scds.chats.dom.general;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;
import org.isisaddons.wicket.gmap3.cpt.service.LocationLookupService;
import org.jmock.auto.Mock;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import au.com.scds.chats.dom.general.Person;

public class PersonAgeTest {

	@Rule
	public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

	@Mock
	DomainObjectContainer mockContainer;
	Person person;

	public static class PersonTest_Tests extends PersonAgeTest {

		@Test
		public void birthdayAtTodayTest(){
			LocalDate when = new LocalDate(2015,1,1);
			LocalDate bdate = new LocalDate(1950,1,1);
			Person p = new Person("","",bdate);
			assertThat(p.getAge(when)).isEqualTo(65);
			assertThat(p.getDaysUntilBirthday(when)).isEqualTo(0);
		}

		@Test
		public void birthdayAtYesterdayTest(){
			LocalDate when = new LocalDate(2014,12,31);
			LocalDate bdate = new LocalDate(1950,1,1);
			Person p = new Person("","",bdate);
			assertThat(p.getAge(when)).isEqualTo(64);
			assertThat(p.getDaysUntilBirthday(when)).isEqualTo(1);
		}
		
		@Test
		public void birthdayAtTomorrowTest(){
			LocalDate when = new LocalDate(2015,1,2);
			LocalDate bdate = new LocalDate(1950,1,1);
			Person p = new Person("","",bdate);
			assertThat(p.getAge(when)).isEqualTo(65);
			assertThat(p.getDaysUntilBirthday(when)).isEqualTo(364);
		}
		
		@Test
		public void birthdayWithinOneWeek(){
			LocalDate when = new LocalDate(2015,1,1);
			LocalDate bdate = new LocalDate(1950,1,8);
			Person p = new Person("","",bdate);
			assertThat(p.getAge(when)).isEqualTo(64);
			assertThat(p.getDaysUntilBirthday(when)).isEqualTo(7);
		}

		@Test
		public void birthdayAtOneYearFromTodayTest(){
			LocalDate when = new LocalDate(2016,1,1);
			LocalDate bdate = new LocalDate(1950,1,1);
			Person p = new Person("","",bdate);
			assertThat(p.getAge(when)).isEqualTo(66);
			assertThat(p.getDaysUntilBirthday(when)).isEqualTo(0);
		}
		
		@Test
		public void birthdayAtOneYearFromYesterdayTest(){
			LocalDate when = new LocalDate(2015,12,31);
			LocalDate bdate = new LocalDate(1950,1,1);
			Person p = new Person("","",bdate);
			assertThat(p.getAge(when)).isEqualTo(65);
			assertThat(p.getDaysUntilBirthday(when)).isEqualTo(1);
		}
		
		@Test
		public void birthdayAtOneYearFromTomorrowTest(){
			LocalDate when = new LocalDate(2016,1,2);
			LocalDate bdate = new LocalDate(1950,1,1);
			Person p = new Person("","",bdate);
			assertThat(p.getAge(when)).isEqualTo(66);
			assertThat(p.getDaysUntilBirthday(when)).isEqualTo(364);
		}
		
		@Test
		public void ageSillyBirthdate(){
			LocalDate when = new LocalDate(2016,5,5);
			LocalDate bdate = new LocalDate(2016,1,22);
			Person p = new Person("","",bdate);
			assertThat(p.getAge(when)).isEqualTo(0);
			assertThat(p.getDaysUntilBirthday(when)).isEqualTo(261);
		}
	}
}
