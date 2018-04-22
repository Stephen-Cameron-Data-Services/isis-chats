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

import au.com.scds.chats.dom.activity.ChatsParticipant;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.fixture.scenarios.CreateVolunteer;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.xactn.TransactionService;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

public class Volunteers_IntegTest extends IntegTestAbstract {

	   @Inject
	    FixtureScripts fixtureScripts;
	    @Inject
	    TransactionService transactionService;
	    
	    List<Volunteer> volunteers  =  null;

	    @Before
	    public void setUp() throws Exception {
	        // given
	        CreateVolunteer fs = new CreateVolunteer();
	        fixtureScripts.runFixtureScript(fs, null);
	        transactionService.nextTransaction();
	        volunteers = fs.getVolunteers();
	        assertThat(volunteers).isNotNull();
	    }

	    public static class Volunteers extends Volunteers_IntegTest {

	        @Test
	        public void accessible() throws Exception {
	            // then
	        	assertThat(volunteers.get(0)).isNotNull();
	        	Volunteer v = volunteers.get(0);
	        	assertThat(v.getCallAllocations()).isNotNull();
	        	assertThat(v.getCallAllocations().size()).isEqualTo(1);
	        	assertThat(v.getCallAllocations().get(0).getParticipant()).isNotNull();
	        	assertThat(v.getCallAllocations().get(0).getVolunteer()).isEqualTo(v);
	        	assertThat(v.getCallAllocations().get(0).getApproximateCallTime()).isEqualTo("12:00:00");
	        	
	        	ChatsParticipant p = v.getCallAllocations().get(0).getParticipant();
	        	assertThat(p.getCallAllocations()).isNotNull();
	        	assertThat(p.getCallAllocations().size()).isEqualTo(1);
	        }
	    }
	}