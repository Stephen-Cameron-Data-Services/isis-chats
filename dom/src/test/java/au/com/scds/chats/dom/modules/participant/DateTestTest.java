package au.com.scds.chats.dom.modules.participant;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.apache.isis.applib.value.Date;
import org.junit.Before;
import org.junit.Test;

public class DateTestTest {
	
    DateTest date;

    @Before
    public void setUp() throws Exception {
        date = new DateTest();
    }

    public static class DateTester extends DateTestTest {

        @Test
        public void happyCase() throws Exception {
            // given
            assertThat(date.getTest(), is(nullValue()));

            // when
            Date now = new Date();
            //Date later = now.add(1, 1, 1);
            date.setTest(now);

            // then
            assertThat(date.getTest(), is(now));
        }
    }
}
