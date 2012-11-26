/*
 * Copyright 2012 Alin Vasile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jsla.web.authority;

import org.jsla.core.Authority;

public class DefaultAuthorityUser implements Authority {

	private String username;
	
	private String group;
	
	private boolean anonymous;
	
	public DefaultAuthorityUser() {
		super();
	}

	public DefaultAuthorityUser(String username, String group) {
		super();
		this.username = username;
		this.group = group;
		this.anonymous = false;
	}

	public DefaultAuthorityUser(String username, String group, boolean anonymous) {
		super();
		this.username = username;
		this.group = group;
		this.anonymous = anonymous;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String name) {
		this.username = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}
	
	public static DefaultAuthorityUser createAnonymousUser(){
		return  new DefaultAuthorityUser(null,null,true);
	}
	
	public static DefaultAuthorityUser createUsernameOnlyUser(String user){
		return  new DefaultAuthorityUser(user,null,false);
	}

	public String[] getRoles() {
		return null;
	}
	
}
