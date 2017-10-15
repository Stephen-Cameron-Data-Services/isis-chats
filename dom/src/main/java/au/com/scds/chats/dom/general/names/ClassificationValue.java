/*
 *
 *  Copyright 2015 Stephen Cameron Data Services
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
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
package au.com.scds.chats.dom.general.names;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import org.apache.isis.applib.util.ObjectContracts;

import au.com.scds.chats.dom.activity.Activity;

//import au.com.scds.chats.dom.AbstractNamedChatsDomainEntity;

/**
 * Base class for the 'name' types.
 * 
 * We want to have referential integrity in the database but use strings as
 * primary and foreign keys for these simple code types, so have to use
 * Application as the Identity Type and identify name as the primary key.
 * 
 * This means we cannot extend the AbstractNamedChatsDomain entity as DN seems
 * to want the Identity Type of child to be the same as parent class.
 * 
 * @author stevec
 * 
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class ClassificationValue {
	
	private String name;

	public ClassificationValue() {
	}

	public ClassificationValue(String name) {
		this.name = name;
	}
	
	public String title(){
		return getName();
	}

	@PrimaryKey
	@Column(allowsNull = "false", length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!this.getClass().isInstance(obj)) {
			return false;
		}
		return ((ClassificationValue) obj).getName().equals(this.getName());
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":" + this.getName();
	}
}
