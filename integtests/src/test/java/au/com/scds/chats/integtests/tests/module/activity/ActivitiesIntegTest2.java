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

//import au.com.scds.agric.fixture.dom.batches.RecreateBatches;
import au.com.scds.chats.dom.activity.Activities;
import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.fixture.activity.RecreateActivities;
import au.com.scds.chats.integtests.tests.DomainAppIntegTest;


import com.google.common.base.Throwables;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import static org.assertj.core.api.Assertions.assertThat;

public class ActivitiesIntegTest2 extends DomainAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;
    @Inject
    Activities activities;
    
	@Before
	public void setUp() throws Exception {
		// given
		RecreateActivities fs = new RecreateActivities();
		fixtureScripts.runFixtureScript(fs, null);
	}

    public static class Create extends ActivitiesIntegTest2 {

        @Test
        public void happyCase() throws Exception {

            List<ActivityEvent> list = activities.findActivityByName("Test Activity 1");
            assertThat(list.size()).isEqualTo(1);
            assertThat(list.get(0).getName()).isEqualTo("Test Activity 1");
            assertThat(list.get(0).getStartDateTime()).isEqualTo(new DateTime(2016,0,1,14,0,0));
        }
    }
}