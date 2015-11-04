/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package au.com.scds.chats.integtests.tests.module.activity;

import java.util.List;

import javax.inject.Inject;

import au.com.scds.chats.dom.module.activity.Activities;
import au.com.scds.chats.dom.module.activity.ActivityEvent;
import au.com.scds.chats.integtests.tests.DomainAppIntegTest;


import com.google.common.base.Throwables;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.joda.time.DateTime;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import static org.assertj.core.api.Assertions.assertThat;

public class ActivitiesIntegTest extends DomainAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;
    @Inject
    Activities activities;

    public static class Create extends ActivitiesIntegTest {

        @Test
        public void happyCase() throws Exception {

            // given
        	DateTime startDateTime = new DateTime();

            // when
            ActivityEvent obj = wrap(activities).createOneOffActivity("Foobar", startDateTime);

            // then
            assertThat(obj.getName()).isEqualTo("Foobar");
            assertThat(obj.getParentActivity()).isNull();
            assertThat(obj.getActivityType()).isNull();
            assertThat(obj.getActivityTypeName()).isNull();
            //TODO assertThat(obj.getRegion()).isNull();
            //assertThat(obj.getLocation()).isNull();
            //assertThat(obj.getLocationName()).isNull();
            assertThat(obj.getDescription()).isNull();
            assertThat(obj.getIsRestricted()).isNull();
            assertThat(obj.getCostForParticipant()).isNull();
            assertThat(obj.getScheduleId()).isNull();
            assertThat(obj.getApproximateEndDateTime()).isNull();
            assertThat(obj.getStartDateTime()).isEqualTo(startDateTime);
        }
        
        //Queries

        /*@Test
        public void whenAlreadyExists() throws Exception {

            // given
            FixtureScript fs = new SimpleObjectsTearDown();
            fixtureScripts.runFixtureScript(fs, null);
            nextTransaction();
            wrap(activities).create("Faz");
            nextTransaction();

            // then
            expectedExceptions.expectCause(causalChainContains(SQLIntegrityConstraintViolationException.class));

            // when
            wrap(activities).create("Faz");
            nextTransaction();
        }*/

        private static Matcher<? extends Throwable> causalChainContains(final Class<?> cls) {
            return new TypeSafeMatcher<Throwable>() {
                @Override
                protected boolean matchesSafely(Throwable item) {
                    final List<Throwable> causalChain = Throwables.getCausalChain(item);
                    for (Throwable throwable : causalChain) {
                        if(cls.isAssignableFrom(throwable.getClass())){
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public void describeTo(Description description) {
                    description.appendText("exception with causal chain containing " + cls.getSimpleName());
                }
            };
        }
    }
    
    public static class QueryTests extends ActivitiesIntegTest {

    	
    	@Test
        public void listAllFutureActivities() {
        	List<ActivityEvent> list = wrap(activities).listAllFutureActivities();
        	assertThat(list.size()).isEqualTo(0);
        }
    	
    	@Test
        public void listAllPastActivities() {
        	List<ActivityEvent> list = wrap(activities).listAllPastActivities();
        	assertThat(list.size()).isEqualTo(1);
        }
    }
    
    public static class findAllPastActivitiesQueryTest extends ActivitiesIntegTest {

    }
    
	//public static class Create extends ActivitiesIntegTest {

}