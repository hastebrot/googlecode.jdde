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

package com.google.code.jdde.client;

import com.google.code.jdde.client.event.AsyncTransactionListener;
import com.google.code.jdde.ddeml.CallbackParameters;
import com.google.code.jdde.ddeml.DdeAPI;
import com.google.code.jdde.ddeml.Pointer;
import com.google.code.jdde.ddeml.constants.DmlError;
import com.google.code.jdde.event.AsyncTransactionEvent;

/**
 * 
 * @author Vitor Costa
 */
public class AsyncTransaction {

	private DdeClient client;
	private ClientConversation conversation;
	
	private int transactionId;
	private boolean completed;
	
	private AsyncTransactionListener listener;
	
	AsyncTransaction(DdeClient client, ClientConversation conversation, int transactionId,
			AsyncTransactionListener listener) {
		this.client = client;
		this.conversation = conversation;
		
		this.transactionId = transactionId;
		
		this.listener = listener;
	}
	
	/* ************************************ *
	 ********* getters and setters ********** 
	 * ************************************ */
	
	public DdeClient getClient() {
		return client;
	}
	
	public ClientConversation getConversation() {
		return conversation;
	}
	
	public int getTransactionId() {
		return transactionId;
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	/* ************************************ *
	 *********** ddeml api calls ************ 
	 * ************************************ */
	
	public void abandon() {
		final Pointer<Integer> error = new Pointer<Integer>();
		
		client.getLoop().invokeAndWait(new Runnable() {
			public void run() {
				int idInst = client.getIdInst();
				int hConv = conversation.getHConv();
				
				boolean succeded = DdeAPI.AbandonTransaction(idInst, hConv, transactionId);

				if (!succeded) {
					error.value = DdeAPI.GetLastError(client.getIdInst());
				}				
			}
		});
		
		DmlError.throwExceptionIfValidError(error.value);
		
		completed = true;
		conversation.transactionCompleted(transactionId);
	}
	
	/* ************************************ *
	 ********** dispatch callbacks ********** 
	 * ************************************ */
	
	void fireAsyncTransactionCompleted(CallbackParameters parameters) {
		if (listener != null) {
			AsyncTransactionEvent event = new AsyncTransactionEvent(
					client, conversation, this, parameters);
			
			if (parameters.getHdata() == null) {
				listener.onError(event);
			}
			else {
				listener.onSuccess(event);
			}
		}
		
		completed = true;
		conversation.transactionCompleted(transactionId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + transactionId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AsyncTransaction other = (AsyncTransaction) obj;
		if (transactionId != other.transactionId)
			return false;
		return true;
	}

}
