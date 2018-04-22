package au.com.scds.chats.integtest;
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


import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import au.com.scds.chats.dom.activity.ChatsRecurringActivity;
import au.com.scds.chats.fixture.scenarios.CreateChatsRecurringActivities;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.xactn.TransactionService;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public class RecurringActivities_IntegTest extends IntegTestAbstract {

	   @Inject
	    FixtureScripts fixtureScripts;
	    @Inject
	    TransactionService transactionService;
	    
	    List<ChatsRecurringActivity> activities  =  null;

	    @Before
	    public void setUp() throws Exception {
	        // given
	        CreateChatsRecurringActivities fs = new CreateChatsRecurringActivities();
	        fixtureScripts.runFixtureScript(fs, null);
	        transactionService.nextTransaction();
	        activities = fs.getActivities();
	        assertThat(activities).isNotNull();
	    }

	    public static class RecurringActivities extends RecurringActivities_IntegTest {

	        @Test
	        public void accessible() throws Exception {
	            // then
	        	assertThat(activities.get(0)).isNotNull();
	        	assertThat(activities.get(0).getParticipations()).isNotNull();
	        	assertThat(activities.get(0).getParticipations().size()).isEqualTo(1);
	        	assertThat(activities.get(0).getChildEvents()).isNotNull();
	        	assertThat(activities.get(0).getChildEvents().size()).isEqualTo(1);
	        	//one participation from parent and one from child, so two
	        	assertThat(activities.get(0).getChildEvents().first().getParticipations().size()).isEqualTo(2);
	        }
	    }
	}