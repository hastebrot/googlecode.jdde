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
import com.google.code.jdde.client.AsyncTransaction;
import com.google.code.jdde.client.ClientConversation;
import com.google.code.jdde.client.DdeClient;
import com.google.code.jdde.client.event.AsyncTransactionListener;
import com.google.code.jdde.ddeml.constants.DmlError;
import com.google.code.jdde.ddeml.constants.FlagCallbackResult;
import com.google.code.jdde.event.AsyncTransactionEvent;
import com.google.code.jdde.event.ExecuteEvent;
import com.google.code.jdde.event.PokeEvent;
import com.google.code.jdde.event.RequestEvent;
import com.google.code.jdde.misc.ClipboardFormat;
import com.google.code.jdde.misc.DdeException;
import com.google.code.jdde.server.DdeServer;
import com.google.code.jdde.server.event.TransactionAdapter;

/**
 * 
 * @author Vitor Costa
 */
public class AsynchronousTests extends JavaDdeTests {

	@Test
	public void asyncRequestSucceedsWhenServerReturnsData() throws Exception {
		startTest(2);
		
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public byte[] onRequest(RequestEvent e) {
				assertNotNull(e.getConversation());
				assertEquals(service, e.getConversation().getService());
				assertEquals(topic, e.getConversation().getTopic());
				assertEquals(item, e.getItem());
				assertEquals(ClipboardFormat.TEXT, e.getFormat());
				countDown();
				return data;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		
		conv.requestAsync(item, new AsyncTransactionListener() {
			@Override
			public void onError(AsyncTransactionEvent e) {
				fail();
			}
			@Override
			public void onSuccess(AsyncTransactionEvent e) {
				assertEquals(data, e.getData());
				countDown();
			}
		});
	}
	
	@Test
	public void asyncRequestFailsWhenServerReturnsNoData() throws Exception {
		startTest(2);
		
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public byte[] onRequest(RequestEvent e) {
				countDown();
				return null;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		
		conv.requestAsync(item, new AsyncTransactionListener() {
			@Override
			public void onError(AsyncTransactionEvent e) {
				countDown();
			}
			@Override
			public void onSuccess(AsyncTransactionEvent e) {
				fail();
			}
		});
	}
	
	@Test
	public void asyncExecuteSucceedsWhenServerAcknowledgesTheTransaction() throws Exception {
		startTest(2);
		
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public FlagCallbackResult onExecute(ExecuteEvent e) {
				assertNotNull(e.getConversation());
				assertEquals(service, e.getConversation().getService());
				assertEquals(topic, e.getConversation().getTopic());
				assertEquals(command, e.getCommand());
				countDown();
				return FlagCallbackResult.DDE_FACK;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		
		conv.executeAsync(command, new AsyncTransactionListener() {
			@Override
			public void onError(AsyncTransactionEvent e) {
				fail();
			}
			@Override
			public void onSuccess(AsyncTransactionEvent e) {
				countDown();
			}
		});
	}
	
	@Test
	public void asyncExecuteFailsWhenServerIsBusy() throws Exception {
		startTest(2);
		
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public FlagCallbackResult onExecute(ExecuteEvent e) {
				countDown();
				return FlagCallbackResult.DDE_FBUSY;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		
		conv.executeAsync(command, new AsyncTransactionListener() {
			@Override
			public void onError(AsyncTransactionEvent e) {
				countDown();
			}
			@Override
			public void onSuccess(AsyncTransactionEvent e) {
				fail();
			}
		});
	}

	@Test
	public void asyncExecuteFailsWhenServerDoesNotProcessTheTransaction() throws Exception {
		startTest(2);
		
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public FlagCallbackResult onExecute(ExecuteEvent e) {
				countDown();
				return FlagCallbackResult.DDE_FNOTPROCESSED;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		
		conv.executeAsync(command, new AsyncTransactionListener() {
			@Override
			public void onError(AsyncTransactionEvent e) {
				countDown();
			}
			@Override
			public void onSuccess(AsyncTransactionEvent e) {
				fail();
			}
		});
	}
	
	@Test
	public void asyncPokeSucceedsWhenServerAcknowledgesTheTransaction() {
		startTest(2);
		
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public FlagCallbackResult onPoke(PokeEvent e) {
				assertNotNull(e.getConversation());
				assertEquals(service, e.getConversation().getService());
				assertEquals(topic, e.getConversation().getTopic());
				assertEquals(item, e.getItem());
				assertEquals(ClipboardFormat.TEXT, e.getFormat());
				
				assertEquals(data, e.getData());
				
				countDown();
				return FlagCallbackResult.DDE_FACK;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		
		conv.pokeAsync(item, data, new AsyncTransactionListener() {
			@Override
			public void onError(AsyncTransactionEvent e) {
				fail();
			}
			@Override
			public void onSuccess(AsyncTransactionEvent e) {
				countDown();
			}
		});
	}
	
	@Test
	public void asyncPokeFailsWhenServerIsBusy() {
		startTest(2);
		
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public FlagCallbackResult onPoke(PokeEvent e) {
				countDown();
				return FlagCallbackResult.DDE_FBUSY;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);

		conv.pokeAsync(item, data, new AsyncTransactionListener() {
			@Override
			public void onError(AsyncTransactionEvent e) {
				countDown();
			}
			@Override
			public void onSuccess(AsyncTransactionEvent e) {
				fail();
			}
		});
	}
	
	@Test
	public void asyncPokeFailsWhenServerDoesNotProcessTheTransaction() {
		startTest(2);
		
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public FlagCallbackResult onPoke(PokeEvent e) {
				countDown();
				return FlagCallbackResult.DDE_FNOTPROCESSED;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);

		conv.pokeAsync(item, data, new AsyncTransactionListener() {
			@Override
			public void onError(AsyncTransactionEvent e) {
				countDown();
			}
			@Override
			public void onSuccess(AsyncTransactionEvent e) {
				fail();
			}
		});
	}
	
	@Test
	public void clientCanAbandonTransaction() throws Exception {
		startTest(1);
		
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public byte[] onRequest(RequestEvent e) {
				sleep(100);	// sleep for a while
				countDown();
				return data;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		
		AsyncTransaction asyncTx = conv.requestAsync(item, new AsyncTransactionListener() {
			@Override
			public void onError(AsyncTransactionEvent e) {
				fail();
			}
			@Override
			public void onSuccess(AsyncTransactionEvent e) {
				fail();
			}
		});
		asyncTx.abandon();
	}
	
	@Test
	public void clientCannotAbandonTransactionAfterItHasFinished() throws Exception {
		startTest(2);
		
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public byte[] onRequest(RequestEvent e) {
				countDown();
				return data;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		
		AsyncTransaction asyncTx = conv.requestAsync(item, new AsyncTransactionListener() {
			@Override
			public void onError(AsyncTransactionEvent e) {
				fail();
			}
			@Override
			public void onSuccess(AsyncTransactionEvent e) {
				countDown();
			}
		});
		
		// wait until the latch has counted down to zero
		super.await();
		
		try {
			asyncTx.abandon();
			fail();
		} catch (DdeException e) {
			// thought it would be DmlError.UNFOUND_QUEUE_ID 
			assertEquals(DmlError.INVALIDPARAMETER, e.getError());
		}
	}
	
}
