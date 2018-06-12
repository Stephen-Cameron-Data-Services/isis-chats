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
package domainapp.application.services.homepage;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.ViewModel;

@ViewModel
@DomainObject(objectType="HomePage")
public class HomePageViewModel {

	// region > title
	public String title() {
		return "Chats Application";
		//noteRepository.allNotes().size() + " notes";
	}

	// endregion

	// region > object (collection)
	//@HomePage
	//@CollectionLayout(render = RenderType.EAGERLY, paged = 10)
	//public List<Note> getNotes() {
	//	return noteRepository.allNotes();
	//}

	// endregion

	// region > injected services

	//@javax.inject.Inject
	//NoteRepository noteRepository;

	// endregion
}