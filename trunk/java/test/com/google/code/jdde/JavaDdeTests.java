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

package com.google.code.jdde;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;

import com.google.code.jdde.client.DdeClient;
import com.google.code.jdde.event.ConnectEvent;
import com.google.code.jdde.server.DdeServer;
import com.google.code.jdde.server.event.ConnectionAdapter;

/**
 * 
 * @author Vitor Costa
 */
public class JavaDdeTests extends Assert {

	protected String service	= "TestService";
	protected String topic		= "TestTopic";
	protected String item		= "TestItem";
	protected String command	= "[SOME.COMMAND()]";
	protected byte[] data		= new byte[] {2, 4, 6, 8};
	
	private CountDownLatch latch;
	
	private List<DdeClient> clients = new ArrayList<DdeClient>();
	private List<DdeServer> servers = new ArrayList<DdeServer>();
	
	@Before
	public void clear() {
		latch = null;
		clients.clear();
		servers.clear();
	}
	
	protected void startTest(int count) {
		latch = new CountDownLatch(count);
	}

	protected void countDown() {
		latch.countDown();
	}
	
	protected void await() {
		try {
			latch.await();
		} catch (InterruptedException e) { }
	}
	
	@After
	public void finishTest() {
		for (DdeClient client : clients) {
			client.rethrowMessageLoopException();
		}
		for (DdeServer server : servers) {
			server.rethrowMessageLoopException();
		}

		if (latch != null) {
			try {
				latch.await(1000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {}
			
			if (latch.getCount() > 0) {
				throw new AssertionFailedError("countdown latch is not empty");
			}
		}
	}
	
	@After
	public void releaseResources() {
		for (DdeClient client : clients) {
			client.uninitialize();
		}
		for (DdeServer server : servers) {
			server.uninitialize();
		}
	}
	
	public DdeClient newClient() {
		DdeClient client = new DdeClient();
		clients.add(client);
		return client;
	}
	
	public DdeServer newServer(String ... services) {
		return newServer(0, services);
	}
	
	public DdeServer newServer(int initializeFlags, String ... services) {
		DdeServer server = new DdeServer(initializeFlags);
		for (String service : services) {
			server.registerService(service);
		}
		servers.add(server);
		return server;
	}
	
	public DdeServer newOpenServer(String ... services) {
		return newOpenServer(0, services);
	}
	
	public DdeServer newOpenServer(int initializeFlags, String ... services) {
		DdeServer server = newServer(initializeFlags, services);
		server.setConnectionListener(new ConnectionAdapter() {
			public boolean onConnect(ConnectEvent event) {
				return true;
			}
		});
		return server;
	}
	
	protected void assertEquals(byte[] expected, byte[] actual) {
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual[i]);
		}
	}
	
	protected void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) { }
	}
	
}
