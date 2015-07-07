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
package au.com.scds.isis.integtests.specglue.modules.simple;

import au.com.scds.chats.dom.modules.participant.Participant;
import au.com.scds.chats.dom.modules.participant.Participants;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

import java.util.List;
import java.util.UUID;
import org.apache.isis.core.specsupport.specs.CukeGlueAbstract;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ClientGlue extends CukeGlueAbstract {

    @Given("^there are.* (\\d+) client objects$")
    public void there_are_N_client_objects(int n) throws Throwable {
        try {
            final List<Participant> findAll = service(Participants.class).listAll();
            assertThat(findAll.size(), is(n));
            putVar("list", "all", findAll);
            
        } finally {
            assertMocksSatisfied();
        }
    }
    
    @When("^I create a new client object$")
    public void I_create_a_new_client_object() throws Throwable {
        service(Participants.class).create(UUID.randomUUID().toString());
    }
    
}
