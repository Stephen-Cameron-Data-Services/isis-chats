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
package domainapp.application.manifest;

import org.apache.isis.applib.AppManifestAbstract2;

import au.com.scds.chats.fixture.ChatsFixtureModule;
import domainapp.application.DomainAppApplicationModule;

/**
 * Bootstrap the application.
 */
public class DomainAppAppManifest extends AppManifestAbstract2 {

	public static final Builder BUILDER = Builder.forModule(new DomainAppApplicationModule())
			.withConfigurationPropertiesFile(DomainAppAppManifest.class, 
					"isis-non-changing.properties",
					"authentication_shiro.properties",
					"persistor_datanucleus.properties",
					"viewer_restfulobjects.properties",
					"viewer_wicket.properties")
			.withAuthMechanism("shiro")
			.withAdditionalModules(org.isisaddons.module.security.SecurityModule.class)
			.withAdditionalServices(
					org.isisaddons.module.security.dom.password.PasswordEncryptionServiceUsingJBcrypt.class,
					org.isisaddons.module.security.dom.permission.PermissionsEvaluationServiceAllowBeatsVeto.class);

	public DomainAppAppManifest() {
		super(BUILDER);
	}

}
