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


import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.inject.Inject;

import au.com.scds.chats.dom.modules.participant.Participant;
import au.com.scds.chats.dom.modules.participant.Participants;
import au.com.scds.chats.fixture.modules.participant.ParticipantsTearDown;
import au.com.scds.chats.fixture.scenarios.RecreateParticipants;
import au.com.scds.isis.integtests.tests.SimpleAppIntegTest;

import com.google.common.base.Throwables;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScripts;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ClientsIntegTest extends SimpleAppIntegTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Inject
    FixtureScripts fixtureScripts;
    @Inject
    Participants clients;

    public static class ListAll extends ClientsIntegTest {

        @Test
        public void happyCase() throws Exception {

            // given
            RecreateParticipants fs = new RecreateParticipants();
            fixtureScripts.runFixtureScript(fs, null);
            nextTransaction();

            // when
            final List<Participant> all = wrap(clients).listAll();

            // then
            assertThat(all.size(), is(fs.getParticipants().size()));

            Participant participant = wrap(all.get(0));
            assertThat(participant.getFullname(), is(fs.getParticipants().get(0).getFullname()));
        }

        @Test
        public void whenNone() throws Exception {

            // given
            FixtureScript fs = new ParticipantsTearDown();
            fixtureScripts.runFixtureScript(fs, null);
            nextTransaction();

            // when
            final List<Participant> all = wrap(clients).listAll();

            // then
            assertThat(all.size(), is(0));
        }
    }

    public static class Create extends ClientsIntegTest {

        @Test
        public void happyCase() throws Exception {

            // given
            FixtureScript fs = new ParticipantsTearDown();
            fixtureScripts.runFixtureScript(fs, null);
            nextTransaction();

            // when
            wrap(clients).create("Faz");

            // then
            final List<Participant> all = wrap(clients).listAll();
            assertThat(all.size(), is(1));
        }

        @Test
        public void whenAlreadyExists() throws Exception {

            // given
            FixtureScript fs = new ParticipantsTearDown();
            fixtureScripts.runFixtureScript(fs, null);
            nextTransaction();
            wrap(clients).create("Faz");
            nextTransaction();

            // then
            expectedException.expectCause(causalChainContains(SQLIntegrityConstraintViolationException.class));

            // when
            wrap(clients).create("Faz");
            nextTransaction();
        }

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

}