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

package com.google.code.jdde.event;

import com.google.code.jdde.client.AsyncTransaction;
import com.google.code.jdde.client.ClientConversation;
import com.google.code.jdde.client.DdeClient;
import com.google.code.jdde.ddeml.CallbackParameters;
import com.google.code.jdde.misc.ClipboardFormat;

/**
 * 
 * @author Vitor Costa
 */
public class AsyncTransactionEvent extends ConversationEvent<DdeClient, ClientConversation> {

	private final AsyncTransaction transaction;
	private final ClipboardFormat format;
	
	private final byte[] data;
	private final int statusFlags;
	
	public AsyncTransactionEvent(DdeClient client, ClientConversation conversation, 
			AsyncTransaction transaction, CallbackParameters parameters) {
		
		super(client, conversation);
		
		this.transaction = transaction;
		this.format = new ClipboardFormat(parameters.getUFmt());
		
		this.data = parameters.getHdata();
		this.statusFlags = ((Integer) parameters.getDwData2()).intValue();
	}

	public AsyncTransaction getTransaction() {
		return transaction;
	}

	public ClipboardFormat getFormat() {
		return format;
	}

	public byte[] getData() {
		return data;
	}

	public int getStatusFlags() {
		return statusFlags;
	}
	
}
