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
package au.com.scds.chats.fixture.scenarios;

import java.util.ArrayList;

import javax.inject.Inject;

import au.com.scds.chats.fixture.modules.participant.ParticipantCreate3;
import au.com.scds.chats.fixture.modules.participant.ParticipantData;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

public class RecreateParticipants3 extends FixtureScript {

	ArrayList<ParticipantData> canned;

	public RecreateParticipants3() {
		withDiscoverability(Discoverability.DISCOVERABLE);
		getCanned();
	}

	public int numberCanned() {
		return canned.size(); // keep in step with canned() !!
	}

	@Override
	protected void execute(final ExecutionContext executionContext) {

		for(ParticipantData data : canned){
			executionContext.executeChild(this, new ParticipantCreate3(data));			
		}
	}

	private void getCanned() {

		canned = new ArrayList<ParticipantData>();
		ParticipantData d = new ParticipantData();
		d.fullName = "Joseph Andrew Bloggs";
		d.preferredName = "Joe";
		d.mobilePhoneNumber = "0000 000 000";
		d.homePhoneNumber = "02 00 000 000";
		d.email = "joebloggs@unknown.com";
		d.sStreet1 = "Unit 10";
		d.sStreet2 = "The Aged Home";
		d.sSuburb = "Underwood";
		d.sPostcode = "1234";
		d.sStreetIsMail = "true";
		d.mStreet1 = "";
		d.mStreet2 = "";
		d.mSuburb = "";
		d.mPostcode = "";
		d.dob = "1935-10-28";
		canned.add(d);

		d = new ParticipantData();
		d.fullName = "Frederick Arthur Dagg";
		d.preferredName = "Fred";
		d.mobilePhoneNumber = "0000 000 000";
		d.homePhoneNumber = "02 00 000 000";
		d.email = "fred.dagg@unknown.com";
		d.sStreet1 = "The Mansion";
		d.sStreet2 = "123 Hilltop Road";
		d.sSuburb = "Nobbs Hill";
		d.sPostcode = "1235";
		d.sStreetIsMail = "false";
		d.mStreet1 = "PO Box 1111";
		d.mStreet2 = "Post Office";
		d.mSuburb = "Sunnyville";
		d.mPostcode = "1236";
		d.dob = "1935-10-01";
		canned.add(d);
	}

	@Inject
	private IsisJdoSupport isisJdoSupport;
}
