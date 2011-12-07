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

package com.google.code.jdde.server;

import com.google.code.jdde.ddeml.DdeAPI;
import com.google.code.jdde.ddeml.Pointer;
import com.google.code.jdde.ddeml.constants.DmlError;
import com.google.code.jdde.ddeml.constants.InitializeFlags;
import com.google.code.jdde.ddeml.constants.NameServiceFlags;
import com.google.code.jdde.misc.DdeApplication;
import com.google.code.jdde.server.event.ConnectionListener;
import com.google.code.jdde.server.event.ServerErrorListener;
import com.google.code.jdde.server.event.ServerRegistrationListener;
import com.google.code.jdde.server.event.TransactionListener;

/**
 * 
 * @author Vitor Costa
 */
public class DdeServer extends DdeApplication {

	private ConnectionListener connectionListener;
	private TransactionListener transactionListener;
	private ServerErrorListener errorListener;
	private ServerRegistrationListener registrationListener;
	
	public DdeServer() {
		this(0);
	}
	
	public DdeServer(int initializeFlags) {
		initialize(new ServerCallbackImpl(this), 
				InitializeFlags.APPCLASS_STANDARD |
				InitializeFlags.APPCMD_FILTERINITS |
				initializeFlags);
	}
	
	/* ************************************ *
	 ********* getters and setters ********** 
	 * ************************************ */
	
	public void setConnectionListener(ConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
	}
	
	public ConnectionListener getConnectionListener() {
		return connectionListener;
	}
	
	public void setTransactionListener(TransactionListener transactionListener) {
		this.transactionListener = transactionListener;
	}
	
	public TransactionListener getTransactionListener() {
		return transactionListener;
	}
	
	public void setErrorListener(ServerErrorListener errorListener) {
		this.errorListener = errorListener;
	}
	
	public ServerErrorListener getErrorListener() {
		return errorListener;
	}
	
	public void setRegistrationListener(ServerRegistrationListener registrationListener) {
		this.registrationListener = registrationListener;
	}
	
	public ServerRegistrationListener getRegistrationListener() {
		return registrationListener;
	}
	
	/* ************************************ *
	 *********** ddeml api calls ************ 
	 * ************************************ */
	
	public void registerService(String service) {
		invokeNameService(service, NameServiceFlags.DNS_REGISTER);
	}
	
	public void unregisterService(String service) {
		invokeNameService(service, NameServiceFlags.DNS_UNREGISTER);
	}
	
	public void unregisterAllServices() {
		invokeNameService(null, NameServiceFlags.DNS_UNREGISTER);
	}
	
	public void turnServiceNameFilteringOn() {
		invokeNameService(null, NameServiceFlags.DNS_FILTERON);
	}
	
	public void turnServiceNameFilteringOff() {
		invokeNameService(null, NameServiceFlags.DNS_FILTEROFF);
	}
	
	private void invokeNameService(final String service, final int commands) {
		final Pointer<Integer> error = new Pointer<Integer>();
		
		loop.invokeAndWait(new Runnable() {
			public void run() {
				boolean succeded = DdeAPI.NameService(idInst, service, commands);

				if (!succeded) {
					error.value = DdeAPI.GetLastError(idInst);
				}				
			}
		});
		
		DmlError.throwExceptionIfValidError(error.value);
	}
	
	public void postAdvise(final String topic, final String item) {
		final Pointer<Integer> error = new Pointer<Integer>();
		
		loop.invokeAndWait(new Runnable() {
			public void run() {
				boolean succeded = DdeAPI.PostAdvise(idInst, topic, item);

				if (!succeded) {
					error.value = DdeAPI.GetLastError(idInst);
				}				
			}
		});
		
		DmlError.throwExceptionIfValidError(error.value);
	}
	
	/* ************************************ *
	 ************ helper methods ************ 
	 * ************************************ */
	
	@Override
	protected ServerConversation findConversation(int conv) {
		return (ServerConversation) super.findConversation(conv);
	}
	
	void addConversation(ServerConversation conversation) {
		conversations.add(conversation);
	}
	
}
