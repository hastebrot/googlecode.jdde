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
import com.google.code.jdde.client.DdeClient;
import com.google.code.jdde.ddeml.constants.DmlError;
import com.google.code.jdde.ddeml.constants.InitializeFlags;
import com.google.code.jdde.event.ConnectConfirmEvent;
import com.google.code.jdde.event.ConnectEvent;
import com.google.code.jdde.misc.Conversation;
import com.google.code.jdde.misc.DdeException;
import com.google.code.jdde.server.DdeServer;
import com.google.code.jdde.server.event.ConnectionAdapter;

/**
 * 
 * @author Vitor Costa
 */
public class ConnectTests extends JavaDdeTests {

	@Test
	public void serverReceivesCorrectParameters() {
		startTest(1);
		
		DdeServer server = newServer(service);
		server.setConnectionListener(new ConnectionAdapter() {
			public boolean onConnect(ConnectEvent e) {
				assertEquals(service, e.getService());
				assertEquals(topic, e.getTopic());
				assertFalse(e.isSameInstance());
				return true;
			}
			
			public void onConnectConfirm(ConnectConfirmEvent e) {
				assertNotNull(e.getConversation());
				assertEquals(service, e.getConversation().getService());
				assertEquals(topic, e.getConversation().getTopic());
				countDown();
			}
		});
		
		DdeClient client = newClient();
		client.connect(service, topic);
	}
	
	@Test
	public void serverDoesNotListenOnUnregisteredService() {
		DdeServer server = newServer(service);
		server.setConnectionListener(new ConnectionAdapter() {
			public boolean onConnect(ConnectEvent e) {
				fail();
				return true;
			}
		});
		
		DdeClient client = newClient();
		try {
			client.connect("UnknownService", "UnknowTopic");
			fail();
		} catch (DdeException e) {
			assertEquals(DmlError.NO_CONV_ESTABLISHED, e.getError());
		}
	}
	
	@Test
	public void serverDoesNotListenWhenUsingFailConnections() {
		DdeServer server = newServer(
				InitializeFlags.CBF_FAIL_CONNECTIONS, service);
		server.setConnectionListener(new ConnectionAdapter() {
			public boolean onConnect(ConnectEvent e) {
				fail();
				return true;
			}
		});
		
		DdeClient client = newClient();
		try {
			client.connect(service, topic);
			fail();
		} catch (DdeException e) {
			assertEquals(DmlError.NO_CONV_ESTABLISHED, e.getError());
		}
	}
	
	@Test
	public void serverCanRegisterService() {
		DdeServer server = newServer();
		server.setConnectionListener(new ConnectionAdapter() {
			public boolean onConnect(ConnectEvent e) {
				fail();
				return true;
			}
		});

		DdeClient client = newClient();
		try {
			client.connect(service, topic);
			fail();
		} catch (DdeException e) {
			assertEquals(DmlError.NO_CONV_ESTABLISHED, e.getError());
		}
		
		server.registerService(service);
		server.setConnectionListener(new ConnectionAdapter() {
			public boolean onConnect(ConnectEvent e) {
				return topic.equals(e.getTopic());
			}
		});
		
		client.connect(service, topic);
	}
	
	@Test
	public void serverCanUnregisterService() {
		DdeServer server = newServer(service);
		server.setConnectionListener(new ConnectionAdapter() {
			public boolean onConnect(ConnectEvent e) {
				return topic.equals(e.getTopic());
			}
		});
		
		DdeClient client = newClient();
		Conversation conv = client.connect(service, topic);
		boolean disconnected = conv.disconnect();
		
		assertTrue(disconnected);
		
		server.unregisterService(service);
		server.setConnectionListener(new ConnectionAdapter() {
			public boolean onConnect(ConnectEvent e) {
				fail();
				return true;
			}
		});
		
		try {
			client.connect(service, topic);
			fail();
		} catch (DdeException e) {
			assertEquals(DmlError.NO_CONV_ESTABLISHED, e.getError());
		}
	}
	
	@Test
	public void serverCanUnregisterOnlyOneService() throws Exception {
		DdeServer server = newServer("TestService1", "TestService2");
		server.setConnectionListener(new ConnectionAdapter() {
			public boolean onConnect(ConnectEvent e) {
				return topic.equals(e.getTopic());
			}
		});
		
		DdeClient client = newClient();
		client.connect("TestService1", topic).disconnect();
		client.connect("TestService2", topic).disconnect();
		
		server.unregisterService("TestService2");
		server.setConnectionListener(new ConnectionAdapter() {
			public boolean onConnect(ConnectEvent e) {
				assertEquals("TestService1", e.getService());
				return true;
			}
		});
		
		client.connect("TestService1", topic);
		// succeeds
		
		try {
			client.connect("TestService2", topic);
			fail();
		} catch (DdeException e) {
			assertEquals(DmlError.NO_CONV_ESTABLISHED, e.getError());
		}
	}
	
	@Test
	public void serverCanUnregisterAllServices() {
		DdeServer server = newServer("TestService1", "TestService2");
		server.setConnectionListener(new ConnectionAdapter() {
			public boolean onConnect(ConnectEvent e) {
				return topic.equals(e.getTopic());
			}
		});
		
		DdeClient client = newClient();
		client.connect("TestService1", topic).disconnect();
		client.connect("TestService2", topic).disconnect();
		
		server.unregisterAllServices();
		server.setConnectionListener(new ConnectionAdapter() {
			public boolean onConnect(ConnectEvent e) {
				fail();
				return true;
			}
		});
		
		try {
			client.connect("TestService1", topic);
			fail();
		} catch (DdeException e) {
			assertEquals(DmlError.NO_CONV_ESTABLISHED, e.getError());
		}
		
		try {
			client.connect("TestService2", topic);
			fail();
		} catch (DdeException e) {
			assertEquals(DmlError.NO_CONV_ESTABLISHED, e.getError());
		}
	}
	
}
