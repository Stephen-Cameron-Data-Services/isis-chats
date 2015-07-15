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
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.RestrictTo;
import org.apache.isis.applib.fixturescripts.FixtureResult;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.fixturescripts.SimpleFixtureScript;

import au.com.scds.chats.fixture.scenarios.RecreateParticipants;

/**
 * Enables fixtures to be installed from the application.
 */
@DomainService
@DomainServiceLayout(named = "Prototyping", menuBar = DomainServiceLayout.MenuBar.SECONDARY, menuOrder = "20")
public class DomainAppFixturesService extends FixtureScripts {

	public DomainAppFixturesService() {
		super("au.com.scds.chats.fixture", MultipleExecutionStrategy.EXECUTE);
	}

	@Override
	public FixtureScript default0RunFixtureScript() {
		return findFixtureScriptFor(SimpleFixtureScript.class);
	}

	@Override
	public List<FixtureScript> choices0RunFixtureScript() {
		return super.choices0RunFixtureScript();
	}

	// //////////////////////////////////////

	@Action(restrictTo = RestrictTo.PROTOTYPING)
	@ActionLayout(cssClassFa = "fa fa-refresh")
	@MemberOrder(sequence = "20")
	public Object recreateObjectsAndReturnFirst() throws Exception {
		final FixtureScript script = findFixtureScriptFor(RecreateParticipants.class);
		if (script != null) {
			final List<FixtureResult> results = script.run(null);
			return results.get(0).getObject();
		} else {
			throw new Exception("Fixture Script not found");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DomainAppFixturesService service = new DomainAppFixturesService();
		service.init();
		try {
			service.recreateObjectsAndReturnFirst();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
