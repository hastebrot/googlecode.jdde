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
import com.google.code.jdde.ddeml.constants.DmlError;
import com.google.code.jdde.ddeml.constants.InitializeFlags;
import com.google.code.jdde.event.RequestEvent;
import com.google.code.jdde.misc.ClipboardFormat;
import com.google.code.jdde.misc.DdeException;
import com.google.code.jdde.server.DdeServer;
import com.google.code.jdde.server.event.TransactionAdapter;

/**
 * 
 * @author Vitor Costa
 */
public class RequestTests extends JavaDdeTests {

	@Test
	public void serverReceivesCorrectParameters() {
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public byte[] onRequest(RequestEvent e) {
				assertNotNull(e.getConversation());
				assertEquals(service, e.getConversation().getService());
				assertEquals(topic, e.getConversation().getTopic());
				assertEquals(item, e.getItem());
				assertEquals(ClipboardFormat.TEXT, e.getFormat());
				return data;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		conv.request(item);
	}
	
	@Test
	public void serverDoesNotListenWhenUsingFailRequests() {
		DdeServer server = newOpenServer(
				InitializeFlags.CBF_FAIL_REQUESTS, service);
		
		server.setTransactionListener(new TransactionAdapter() {
			public byte[] onRequest(RequestEvent e) {
				fail();
				return null;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		try {
			conv.request(item);
			fail();
		} catch (DdeException e) {
			assertEquals(DmlError.NOTPROCESSED, e.getError());
		}
	}
	
	@Test
	public void clientReceivesCorrectResult() {
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public byte[] onRequest(RequestEvent e) {
				return data;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		byte[] result = conv.request(item);
		
		assertEquals(data, result);
	}
	
	@Test
	public void clientFailsWhenServerReturnsNoData() {
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public byte[] onRequest(RequestEvent e) {
				return null;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		try {
			conv.request(item);
			fail();
		} catch (DdeException e) {
			assertEquals(DmlError.NOTPROCESSED, e.getError());
		}
	}

}
