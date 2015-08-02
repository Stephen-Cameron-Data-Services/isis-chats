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
package au.com.scds.chats.fixture;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.RestrictTo;
import org.apache.isis.applib.fixturescripts.FixtureResult;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScripts;

import au.com.scds.chats.fixture.modules.participant.ParticipantCreate;
//import au.com.scds.chats.fixture.modules.participant.ParticipantCreate2;
//import au.com.scds.chats.fixture.scenarios.RecreateParticipants2;
import au.com.scds.chats.fixture.scenarios.RecreateParticipants3;

/**
 * Enables fixtures to be installed from the application.
 */
@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY
)
@DomainServiceLayout(
        named = "Prototyping",
        menuBar = DomainServiceLayout.MenuBar.SECONDARY,
        menuOrder = "500.10")
public class IsisChatsFixturesService extends FixtureScripts {

    public IsisChatsFixturesService() {
        super(
                IsisChatsFixturesService.class.getPackage().getName(),
                MultipleExecutionStrategy.EXECUTE);
    }

    @Action(
            restrictTo = RestrictTo.PROTOTYPING
    )
    @ActionLayout(
            cssClassFa="fa fa-bolt"
    )
    @MemberOrder(sequence = "500.10.1")
    @Override
    public List<FixtureResult> runFixtureScript(
            final FixtureScript fixtureScript,
            @ParameterLayout(
                    named="Parameters",
                    describedAs = "Script-specific parameters (key=value) ",
                    multiLine = 10)
            @Parameter(optionality = Optionality.OPTIONAL)
            final String parameters) {
        return super.runFixtureScript(fixtureScript, parameters);
    }

    @Override
    public FixtureScript default0RunFixtureScript() {
        //return findFixtureScriptFor(ParticipantCreate2.class);
    	return findFixtureScriptFor(ParticipantCreate.class);
    	
    }

    /**
     * Raising visibility to <tt>public</tt> so that choices are available for first param
     * of {@link #runFixtureScript(FixtureScript, String)}.
     */
    @Override
    public List<FixtureScript> choices0RunFixtureScript() {
        return super.choices0RunFixtureScript();
    }

    // //////////////////////////////////////
 
	@Action(restrictTo = RestrictTo.PROTOTYPING)
	@ActionLayout(cssClassFa = "fa fa-refresh")
	@MemberOrder(sequence = "20")
	public Object recreateObjectsAndReturnFirst() throws Exception {
		final FixtureScript script = findFixtureScriptFor(RecreateParticipants3.class);
		if (script != null) {
			final List<FixtureResult> results = script.run(null);
			return results.get(0).getObject();
		} else {
			throw new Exception("Fixture Script not found");
		}
	}
}
