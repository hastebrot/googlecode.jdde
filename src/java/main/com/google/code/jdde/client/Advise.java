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

import com.google.code.jdde.client.event.AdviseDataListener;
import com.google.code.jdde.ddeml.CallbackParameters;
import com.google.code.jdde.ddeml.constants.TransactionFlags;
import com.google.code.jdde.event.AdviseDataEvent;
import com.google.code.jdde.misc.ClientTransaction;
import com.google.code.jdde.misc.ClipboardFormat;

/**
 * 
 * @author Vitor Costa
 */
public class Advise {

	private DdeClient client;
	private ClientConversation conversation;
	
	private String item;
	private ClipboardFormat format;
	
	private byte[] lastValue;
	private AdviseDataListener listener;
	
	Advise(DdeClient client, ClientConversation conversation, String item,
			ClipboardFormat format, AdviseDataListener listener) {
		this.client = client;
		this.conversation = conversation;
		
		this.item = item;
		this.format = format;
		
		this.listener = listener;
	}
	
	public String getItem() {
		return item;
	}
	
	public ClipboardFormat getFormat() {
		return format;
	}
	
	public byte[] getLastValue() {
		return lastValue;
	}
	
	public void stop() {
		ClientTransaction tx = new ClientTransaction(conversation);
		tx.call(null, item, format.getValue(), TransactionFlags.XTYP_ADVSTOP,
				client.getDefaultTimeout());

		tx.throwExceptionOnError();
		conversation.adviseStoped(this);
	}
	
	void fireValueChanged(CallbackParameters parameters) {
		if (item.equals(parameters.getHsz2()) && format.getValue() == parameters.getUFmt()) {
		
			lastValue = parameters.getHdata();
			AdviseDataEvent event = new AdviseDataEvent(
					client, conversation, this, parameters.getHdata());
			
			if (listener != null) {
				listener.valueChanged(event);
			}
		}
	}
	
}
