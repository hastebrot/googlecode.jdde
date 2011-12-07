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
import com.google.code.jdde.client.event.ClientDisconnectListener;
import com.google.code.jdde.ddeml.Pointer;
import com.google.code.jdde.event.ConnectConfirmEvent;
import com.google.code.jdde.event.ConnectEvent;
import com.google.code.jdde.event.DisconnectEvent.ClientDisconnectEvent;
import com.google.code.jdde.event.DisconnectEvent.ServerDisconnectEvent;
import com.google.code.jdde.server.DdeServer;
import com.google.code.jdde.server.ServerConversation;
import com.google.code.jdde.server.event.ConnectionAdapter;
import com.google.code.jdde.server.event.ServerDisconnectListener;

/**
 * 
 * @author Vitor Costa
 */
public class DisconnectTests extends JavaDdeTests {
	
	@Test
	public void serverReceivesDiconnectEvent() throws Exception {
		startTest(1);
		
		DdeServer server = newServer(service);
		server.setConnectionListener(new ConnectionAdapter() {
			public boolean onConnect(ConnectEvent e) {
				return true;
			}
			public void onConnectConfirm(ConnectConfirmEvent e) {
				ServerConversation sConv = e.getConversation();
				sConv.setDisconnectListener(new ServerDisconnectListener() {
					public void onDisconnect(ServerDisconnectEvent e) {
						countDown();
					}
				});
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		
		// wait to server receive connect confirm
		Thread.sleep(10);
		
		boolean disconnected = conv.disconnect();
		assertTrue(disconnected);
	}

	@Test
	public void clientReceivesDisconnectEvent() throws Exception {
		startTest(1);
		
		final Pointer<ServerConversation> serverConv = new Pointer<ServerConversation>();
		
		DdeServer server = newServer(service);
		server.setConnectionListener(new ConnectionAdapter() {
			public boolean onConnect(ConnectEvent e) {
				return true;
			}
			public void onConnectConfirm(ConnectConfirmEvent e) {
				serverConv.value = e.getConversation();
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		conv.setDisconnectListener(new ClientDisconnectListener() {
			public void onDisconnect(ClientDisconnectEvent e) {
				countDown();
			}
		});
		
		// wait to server receive connect confirm
		Thread.sleep(10);
		
		boolean disconnected = serverConv.value.disconnect();
		assertTrue(disconnected);
	}
	
}
