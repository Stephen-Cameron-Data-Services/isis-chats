package au.com.scds.chats.dom.call;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;


public class RegularScheduledCallAllocationTest {
	RegularScheduledCallAllocation allocation;

    @Before
    public void setUp() throws Exception {
    	allocation = new RegularScheduledCallAllocation();
    }

    public static class RegularScheduledCallAllocationTest_Test extends RegularScheduledCallAllocationTest {

        @Test
        public void ApproximateCallDateTime_Test1() throws Exception {
            // given
        	LocalDate date = new LocalDate(2016,1,1);
        	DateTime dateTime = new DateTime(2016,1,1,12,30);
            
            // when
        	allocation.setApproximateCallTime("12:30 PM");

            // then
            assertThat(allocation.approximateCallDateTime(date)).isEqualTo(dateTime);
        }
        
        @Test
        public void ApproximateCallDateTime_Test2() throws Exception {
            // given
        	LocalDate date = new LocalDate(2016,1,1);
        	DateTime dateTime = new DateTime(2016,1,1,11,30);
            
            // when
        	allocation.setApproximateCallTime("11:30 AM");

            // then
            assertThat(allocation.approximateCallDateTime(date)).isEqualTo(dateTime);
        }
    }
}
