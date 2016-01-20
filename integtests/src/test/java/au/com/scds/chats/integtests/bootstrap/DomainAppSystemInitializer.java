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
package au.com.scds.chats.integtests.bootstrap;

import org.apache.isis.core.integtestsupport.IsisSystemForTest;
import org.apache.isis.objectstore.jdo.datanucleus.IsisConfigurationForJdoIntegTests;

import domainapp.app.DomainAppAppManifest;

public class DomainAppSystemInitializer {

	public static void initIsft() {
		IsisSystemForTest isft = IsisSystemForTest.getElseNull();

		IsisConfigurationForJdoIntegTests config = new IsisConfigurationForJdoIntegTests();

		config.put("isis.persistor.datanucleus.impl.datanucleus.identifier.case","LowerCase");
		config.put("isis.persistor.datanucleus.impl.datanucleus.identifierFactory","jpa");
		config.put("isis.persistor.datanucleus.impl.datanucleus.schema.autoCreateAll","false");
		config.put("isis.persistor.datanucleus.impl.datanucleus.schema.validateTables","false");
		config.put("isis.persistor.datanucleus.impl.datanucleus.schema.validateConstraints","false");
		config.put("isis.persistor.datanucleus.install-fixtures","false");
		config.put("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionDriverName", "com.mysql.jdbc.Driver");
		config.put("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionURL",
				"jdbc:mysql://localhost:3306/chats?zeroDateTimeBehavior=convertToNull");
		config.put("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionUserName", "chats");
		config.put("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionPassword", "password");

		if (isft == null) {
			try{
			isft = new IsisSystemForTest.Builder().withLoggingAt(org.apache.log4j.Level.DEBUG)
					.with(new DomainAppAppManifest()).with(config).build()
					.setUpSystem();
			IsisSystemForTest.set(isft);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}

}
