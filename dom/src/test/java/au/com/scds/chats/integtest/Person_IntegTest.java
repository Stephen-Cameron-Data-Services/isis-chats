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

import au.com.scds.chats.dom.activity.ChatsActivity;
import au.com.scds.chats.dom.general.ChatsPerson;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.fixture.scenarios.CreateChatsActivities;
import au.com.scds.chats.fixture.scenarios.CreateChatsPerson;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.xactn.TransactionService;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public class Person_IntegTest extends IntegTestAbstract {

	    @Inject
	    FixtureScripts fixtureScripts;
	    @Inject
	    TransactionService transactionService;
	    @Inject
	    Persons personMenu;
	    
	    ChatsPerson person  =  null;

	    @Before
	    public void setUp() throws Exception {
	        // given
	        CreateChatsPerson fs = new CreateChatsPerson();
	        fixtureScripts.runFixtureScript(fs, null);
	        transactionService.nextTransaction();
	        person = fs.getPerson();
	        assertThat(person).isNotNull();
	    }

	    public static class Persons extends Person_IntegTest {

	        @Test
	        public void accessible() throws Exception {
	            // then
	        	assertThat(person.getFirstname()).isEqualTo("Bob");
	        	assertThat(person.getSurname()).isEqualTo("Brown");
	        	assertThat(person.getSex()).isEqualTo(Sex.MALE);
	        }
	    }
	    


	}