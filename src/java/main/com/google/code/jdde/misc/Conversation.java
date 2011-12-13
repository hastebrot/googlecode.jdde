/*
 * Copyright 2008 Vitor Costa
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.code.jdde.misc;

import com.google.code.jdde.ddeml.DdeAPI;
import com.google.code.jdde.ddeml.Pointer;

/**
 * 
 * @author Vitor Costa
 *
 * @param <A>
 */
public abstract class Conversation {

	private final DdeApplication application; 
	
	private final int hConv;
	
	private final String service;
	private final String topic;

	public Conversation(DdeApplication application, int hConv, String service, String topic) {
		this.application = application;
		
		this.hConv = hConv;
		
		this.service = service;
		this.topic = topic;
	}

	protected DdeApplication getApplication() {
		return application;
	}
	
	public int getHConv() {
		return hConv;
	}

	public String getService() {
		return service;
	}

	public String getTopic() {
		return topic;
	}
	
	public boolean disconnect() {
		final Pointer<Boolean> result = new Pointer<Boolean>();
		
		application.getLoop().invokeAndWait(new Runnable() {
			public void run() {
				result.value = DdeAPI.Disconnect(hConv);
			}
		});
		
		return result.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result
//				+ ((application == null) ? 0 : application.hashCode());
		result = prime * result + hConv;
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		result = prime * result + ((topic == null) ? 0 : topic.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Conversation other = (Conversation) obj;
		if (hConv != other.hConv)
			return false;
		return true;
	}
	
}
