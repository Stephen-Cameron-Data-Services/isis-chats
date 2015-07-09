/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package au.com.scds.chats.dom.modules.participant;

import java.util.List;

import au.com.scds.chats.dom.modules.participant.Participant;
import au.com.scds.chats.dom.modules.participant.Participants;

import com.google.common.collect.Lists;


import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ParticipantsTest {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    DomainObjectContainer mockContainer;
    
    Participants participants;

    @Before
    public void setUp() throws Exception {
        participants = new Participants();
        participants.container = mockContainer;
    }

    public static class Create extends ParticipantsTest {

        @Test
        public void happyCase() throws Exception {

            // given
            final Participant client = new Participant();

            final Sequence seq = context.sequence("create");
            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).newTransientInstance(Participant.class);
                    inSequence(seq);
                    will(returnValue(client));

                    oneOf(mockContainer).persistIfNotAlready(client);
                    inSequence(seq);
                }
            });

            // when
            final Participant obj = participants.create("Foobar");

            // then
            assertThat(obj, is(client));
            assertThat(obj.getFullname(), is("Foobar"));
        }

    }

    public static class ListAll extends ParticipantsTest {

        @Test
        public void happyCase() throws Exception {

            // given
            final List<Participant> all = Lists.newArrayList();

            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).allInstances(Participant.class);
                    will(returnValue(all));
                }
            });

            // when
            final List<Participant> list = participants.listAll();

            // then
            assertThat(list, is(all));
        }
    }
}