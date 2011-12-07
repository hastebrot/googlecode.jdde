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

package com.google.code.jdde.event;

import com.google.code.jdde.client.DdeClient;
import com.google.code.jdde.ddeml.CallbackParameters;
import com.google.code.jdde.misc.DdeApplication;
import com.google.code.jdde.server.DdeServer;

/**
 * 
 * @author Vitor Costa
 *
 * @param <A>
 */
public abstract class UnregisterEvent<A extends DdeApplication> extends DdeEvent<A> {

	private String service;
	private String specificService;
	
	public UnregisterEvent(A application, CallbackParameters parameters) {
		super(application);
		
		service = parameters.getHsz1();
		specificService = parameters.getHsz2();
	}
	
	public String getService() {
		return service;
	}
	
	public String getSpecificService() {
		return specificService;
	}
	
	public static class ClientUnregisterEvent extends UnregisterEvent<DdeClient> {
		
		public ClientUnregisterEvent(DdeClient client, CallbackParameters parameters) {
			super(client, parameters);
		}
		
	}
	
	public static class ServerUnregisterEvent extends UnregisterEvent<DdeServer> {
		
		public ServerUnregisterEvent(DdeServer server, CallbackParameters parameters) {
			super(server, parameters);
		}
		
	}
	
}