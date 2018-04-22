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


import org.junit.BeforeClass;

import au.com.scds.chats.dom.ChatsDomModule;

import org.apache.isis.core.integtestsupport.IntegrationTestAbstract3;

public abstract class IntegTestAbstract extends IntegrationTestAbstract3 {

	public IntegTestAbstract() {
		super(new ChatsDomModule()
				.withConfigurationProperty("isis.objects.editing", "true")
				.withConfigurationProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionDriverName",
						"org.hsqldb.jdbcDriver")
				.withConfigurationProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionURL",
						"jdbc:hsqldb:mem:test")
				.withConfigurationProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionUserName", "sa")
				.withConfigurationProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionPassword", ""));
	}
}
