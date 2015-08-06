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
package au.com.scds.isis.integtests.tests.modules.simple;


import javax.inject.Inject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.i18n.TranslationService;
import org.apache.isis.applib.services.wrapper.DisabledException;
import org.apache.isis.applib.services.wrapper.InvalidException;

import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participants;
import au.com.scds.chats.fixture.scenarios.RecreateParticipants;
import au.com.scds.isis.integtests.tests.SimpleAppIntegTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ClientIntegTest extends SimpleAppIntegTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Inject
    FixtureScripts fixtureScripts;
    @Inject
    Participants clients;
    @Inject
    TranslationService translationService;

    RecreateParticipants fs;
    Participant clientPojo;
    Participant clientWrapped;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new RecreateParticipants().setNumber(1);
        fixtureScripts.runFixtureScript(fs, null);

        clientPojo = fs.getParticipants().get(0);

        assertThat(clientPojo, is(not(nullValue())));
        clientWrapped = wrap(clientPojo);
    }

    public static class Name extends ClientIntegTest {

        @Test
        public void accessible() throws Exception {

            // when
            final String name = clientWrapped.getFullname();
            //
            // then
            assertThat(name, is(fs.NAMES.get(0)));
        }

        @Test
        public void cannotBeUpdatedDirectly() throws Exception {

            // expect
            expectedExceptions.expect(DisabledException.class);

            // when
            clientWrapped.setFullname("new name");
        }
    }

    public static class UpdateName extends ClientIntegTest {

        @Test
        public void happyCase() throws Exception {

            // when
            clientWrapped.setFullname("new name");

            // then
            assertThat(clientWrapped.getFullname(), is("new name"));
        }

        @Test
        public void failsValidation() throws Exception {

            // expect
            expectedExceptions.expect(InvalidException.class);
            expectedExceptions.expectMessage("Exclamation mark is not allowed");

            // when
            clientWrapped.setFullname("new name!");
        }
    }


    public static class Title extends ClientIntegTest {

        @Test
        public void interpolatesName() throws Exception {

            // given
            final String name = clientWrapped.getFullname();

            // when
            final String title = container().titleOf(clientWrapped);

            // then
            assertThat(title, is("Client: " + name));
        }
    }
}