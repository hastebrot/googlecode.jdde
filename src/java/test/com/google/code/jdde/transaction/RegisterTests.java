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

package com.google.code.jdde.transaction;

import org.junit.Ignore;
import org.junit.Test;

import com.google.code.jdde.JavaDdeTests;
import com.google.code.jdde.client.DdeClient;
import com.google.code.jdde.client.event.ClientRegistrationAdapter;
import com.google.code.jdde.event.ConnectEvent;
import com.google.code.jdde.event.RegisterEvent.ClientRegisterEvent;
import com.google.code.jdde.event.RegisterEvent.ServerRegisterEvent;
import com.google.code.jdde.event.UnregisterEvent.ClientUnregisterEvent;
import com.google.code.jdde.event.UnregisterEvent.ServerUnregisterEvent;
import com.google.code.jdde.server.DdeServer;
import com.google.code.jdde.server.event.ConnectionAdapter;
import com.google.code.jdde.server.event.ServerRegistrationAdapter;

/**
 * 
 * @author Vitor Costa
 */
public class RegisterTests extends JavaDdeTests {

	@Test
	public void serverReceivesRegisterEvent() throws Exception {
		startTest(1);
		
		DdeServer server = newOpenServer();
		server.setRegistrationListener(new ServerRegistrationAdapter() {
			public void onRegister(ServerRegisterEvent e) {
				assertEquals(service, e.getService());
				countDown();
			}
		});
		
		server.registerService(service);
	}
	
	@Test
	public void serverReceivesUnregisterEvent() throws Exception {
		startTest(1);
		
		DdeServer server = newOpenServer();
		server.setRegistrationListener(new ServerRegistrationAdapter() {
			public void onUnregister(ServerUnregisterEvent e) {
				assertEquals(service, e.getService());
				countDown();
			}
		});
		
		server.registerService(service);
		server.unregisterService(service);
	}
	
	@Test
	public void clientReceivesRegisterEvent() throws Exception {
		startTest(1);
		
		DdeClient client = newClient();
		client.setRegistrationListener(new ClientRegistrationAdapter() {
			public void onRegister(ClientRegisterEvent e) {
				assertEquals(service, e.getService());
				countDown();
			}
		});
		
		@SuppressWarnings("unused")
		DdeServer server = newOpenServer(service);
	}
	
	@Test
	public void clientReceivesUnregisterEvent() throws Exception {
		startTest(1);
		
		DdeClient client = newClient();
		client.setRegistrationListener(new ClientRegistrationAdapter() {
			public void onUnregister(ClientUnregisterEvent e) {
				assertEquals(service, e.getService());
				countDown();
			}
		});
		
		DdeServer server = newOpenServer(service);
		server.unregisterService(service);
	}
	
	@Test
	@Ignore
	public void clientCanConnectUsingSpecificService() throws Exception {
		startTest(2);
		
		DdeClient client = newClient();
		client.setRegistrationListener(new ClientRegistrationAdapter() {
			public void onRegister(ClientRegisterEvent e) {
				countDown();

				DdeClient client = e.getApplication();
				client.connect(e.getSpecificService(), topic);
			}
		});
		
		DdeServer server = newServer();
		server.setConnectionListener(new ConnectionAdapter() {
			public boolean onConnect(ConnectEvent e) {
				assertEquals(topic, e.getTopic());
				countDown();
				return true;
			}
		});
		
		server.registerService(service);
	}
	
}
