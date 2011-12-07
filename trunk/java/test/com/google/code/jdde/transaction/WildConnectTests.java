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

import org.junit.Test;

import com.google.code.jdde.JavaDdeTests;
import com.google.code.jdde.client.ClientConversation;
import com.google.code.jdde.client.DdeClient;
import com.google.code.jdde.event.ConnectConfirmEvent;
import com.google.code.jdde.event.ConnectEvent;
import com.google.code.jdde.misc.SupportedServiceTopic;
import com.google.code.jdde.server.DdeServer;
import com.google.code.jdde.server.event.ConnectionAdapter;

/**
 * 
 * @author Vitor Costa
 */
public class WildConnectTests extends JavaDdeTests {

	SupportedServiceTopic[] sst = new SupportedServiceTopic[] {
			new SupportedServiceTopic("Service1", "Topic1"),
			new SupportedServiceTopic("Service2", "Topic2")
	};
	
	@Test
	public void serverReceivesWildConnectWhenClientUsesNullToConnect() throws Exception {
		startTest(2);
		
		DdeServer server = newServer();
		server.turnServiceNameFilteringOff();
		
		server.setConnectionListener(new ConnectionAdapter() {
			public SupportedServiceTopic[] onWildConnect(ConnectEvent e) {
				assertNull(e.getService());
				assertNull(e.getTopic());
				
				countDown();
				return sst;
			}
			public void onConnectConfirm(ConnectConfirmEvent e) {
				countDown();
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(null, null);

		boolean disconnected = conv.disconnect();
		assertTrue(disconnected);
	}
	
	@Test
	public void serverReceivesWildConnectWhenClientUsesNullAsService() throws Exception {
		startTest(2);
		
		DdeServer server = newServer();
		server.turnServiceNameFilteringOff();
		
		server.setConnectionListener(new ConnectionAdapter() {
			public SupportedServiceTopic[] onWildConnect(ConnectEvent e) {
				assertEquals(service, e.getService());
				assertNull(e.getTopic());
				
				countDown();
				return sst;
			}
			public void onConnectConfirm(ConnectConfirmEvent e) {
				countDown();
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, null);

		boolean disconnected = conv.disconnect();
		assertTrue(disconnected);
	}
	
	@Test
	public void serverReceivesWildConnectWhenClientUsesNullAsTopic() throws Exception {
		startTest(2);
		
		DdeServer server = newServer();
		server.turnServiceNameFilteringOff();
		
		server.setConnectionListener(new ConnectionAdapter() {
			public SupportedServiceTopic[] onWildConnect(ConnectEvent e) {
				assertNull(e.getService());
				assertEquals(topic, e.getTopic());
				
				countDown();
				return sst;
			}
			public void onConnectConfirm(ConnectConfirmEvent e) {
				countDown();
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(null, topic);

		boolean disconnected = conv.disconnect();
		assertTrue(disconnected);
	}
	
}
