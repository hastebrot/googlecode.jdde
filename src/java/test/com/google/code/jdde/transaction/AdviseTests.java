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
import com.google.code.jdde.client.Advise;
import com.google.code.jdde.client.ClientConversation;
import com.google.code.jdde.client.DdeClient;
import com.google.code.jdde.client.event.AdviseDataListener;
import com.google.code.jdde.ddeml.constants.DmlError;
import com.google.code.jdde.ddeml.constants.InitializeFlags;
import com.google.code.jdde.event.AdviseDataEvent;
import com.google.code.jdde.event.AdviseRequestEvent;
import com.google.code.jdde.event.AdviseStartEvent;
import com.google.code.jdde.event.AdviseStopEvent;
import com.google.code.jdde.misc.ClipboardFormat;
import com.google.code.jdde.misc.DdeException;
import com.google.code.jdde.server.DdeServer;
import com.google.code.jdde.server.event.TransactionAdapter;

/**
 * 
 * @author Vitor Costa
 */
public class AdviseTests extends JavaDdeTests {

	@Test
	public void serverReceivesCorrectParameters() {
		startTest(4);
		
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public boolean onAdviseStart(AdviseStartEvent e) {
				assertNotNull(e.getConversation());
				assertEquals(service, e.getConversation().getService());
				assertEquals(topic, e.getConversation().getTopic());
				assertEquals(item, e.getItem());
				assertEquals(ClipboardFormat.TEXT, e.getFormat());
				countDown();
				return true;
			}
			public byte[] onAdviseRequest(AdviseRequestEvent e) {
				assertNotNull(e.getConversation());
				assertEquals(service, e.getConversation().getService());
				assertEquals(topic, e.getConversation().getTopic());
				assertEquals(item, e.getItem());
				assertEquals(ClipboardFormat.TEXT, e.getFormat());
				countDown();
				return data;
			}
			@Override
			public void onAdviseStop(AdviseStopEvent e) {
				assertNotNull(e.getConversation());
				assertEquals(service, e.getConversation().getService());
				assertEquals(topic, e.getConversation().getTopic());
				assertEquals(item, e.getItem());
				assertEquals(ClipboardFormat.TEXT, e.getFormat());
				countDown();
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		Advise advise = conv.startAdvise(item, new AdviseDataListener() {
			public void valueChanged(AdviseDataEvent event) {
				countDown();
			}
		});
		
		server.postAdvise(topic, item);
		advise.stop();
	}
	
	@Test
	public void serverDoesNotListenWhenUsingFailAdvises() {
		DdeServer server = newOpenServer(
				InitializeFlags.CBF_FAIL_ADVISES, service);
		
		server.setTransactionListener(new TransactionAdapter() {
			public boolean onAdviseStart(AdviseStartEvent e) {
				fail();
				return false;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);

		try {
			conv.startAdvise(item, new AdviseDataListener() {
				public void valueChanged(AdviseDataEvent event) {}
			});
			fail();
		} catch (DdeException e) {
			assertEquals(DmlError.NOTPROCESSED, e.getError());
		}
	}
	
	@Test
	public void clientReceivesCorrectResult() {
		startTest(3);
		
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public boolean onAdviseStart(AdviseStartEvent e) {
				countDown();
				return true;
			}
			public byte[] onAdviseRequest(AdviseRequestEvent e) {
				countDown();
				return data;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		conv.startAdvise(item, new AdviseDataListener() {
			public void valueChanged(AdviseDataEvent event) {
				assertEquals(data, event.getData());
				countDown();
			}
		});

		server.postAdvise(topic, item);
	}
	
	@Test
	public void clientFailsWhenServerDoesNotAcceptAdvise() throws Exception {
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public boolean onAdviseStart(AdviseStartEvent e) {
				return false;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		
		try {
			conv.startAdvise(item, new AdviseDataListener() {
				public void valueChanged(AdviseDataEvent event) {}
			});
			fail();
		} catch (DdeException e) {
			assertEquals(DmlError.NOTPROCESSED, e.getError());
		}
	}
	
}
