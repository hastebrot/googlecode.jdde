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
import com.google.code.jdde.ddeml.constants.FlagCallbackResult;
import com.google.code.jdde.ddeml.constants.InitializeFlags;
import com.google.code.jdde.event.ExecuteEvent;
import com.google.code.jdde.misc.DdeException;
import com.google.code.jdde.server.DdeServer;
import com.google.code.jdde.server.event.TransactionAdapter;

/**
 * 
 * @author Vitor Costa
 */
public class ExecuteTests extends JavaDdeTests {

	@Test
	public void serverReceivesCorrectParameters() {
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public FlagCallbackResult onExecute(ExecuteEvent e) {
				assertNotNull(e.getConversation());
				assertEquals(service, e.getConversation().getService());
				assertEquals(topic, e.getConversation().getTopic());
				assertEquals(command, e.getCommand());
				return FlagCallbackResult.DDE_FACK;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		conv.execute(command);
	}
	
	@Test
	public void serverDoesNotListenWhenUsingFailExecutes() {
		DdeServer server = newOpenServer(
				InitializeFlags.CBF_FAIL_EXECUTES, service);
		
		server.setTransactionListener(new TransactionAdapter() {
			public FlagCallbackResult onExecute(ExecuteEvent e) {
				fail();
				return FlagCallbackResult.DDE_FACK;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		try {
			conv.execute(command);
			fail();
		} catch (DdeException e) {
			assertEquals(DmlError.NOTPROCESSED, e.getError());
		}
	}
	
	@Test
	public void clientSucceedsWhenServerAcknowledgesTheTransaction() {
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public FlagCallbackResult onExecute(ExecuteEvent e) {
				return FlagCallbackResult.DDE_FACK;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		conv.execute(command);
	}
	
	@Test
	public void clientFailsWhenServerIsBusy() {
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public FlagCallbackResult onExecute(ExecuteEvent e) {
				return FlagCallbackResult.DDE_FBUSY;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		try {
			conv.execute(command);
			fail();
		} catch (DdeException e) {
			assertEquals(DmlError.BUSY, e.getError());
		}
	}
	
	@Test
	public void clientFailsWhenServerDoesNotProcessTheTransaction() {
		DdeServer server = newOpenServer(service);
		server.setTransactionListener(new TransactionAdapter() {
			public FlagCallbackResult onExecute(ExecuteEvent e) {
				return FlagCallbackResult.DDE_FNOTPROCESSED;
			}
		});
		
		DdeClient client = newClient();
		ClientConversation conv = client.connect(service, topic);
		try {
			conv.execute(command);
			fail();
		} catch (DdeException e) {
			assertEquals(DmlError.NOTPROCESSED, e.getError());
		}
	}
	
}
