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

import com.google.code.jdde.client.Advise;
import com.google.code.jdde.client.ClientConversation;
import com.google.code.jdde.client.DdeClient;

/**
 * 
 * @author Vitor Costa
 */
public class AdviseDataEvent extends ConversationEvent<DdeClient, ClientConversation> {

	private final Advise advise;
	private final byte[] data;

	public AdviseDataEvent(DdeClient client, ClientConversation conversation,
			Advise advise, byte[] data) {

		super(client, conversation);
		
		this.advise = advise;
		this.data = data;
	}

	public Advise getAdvise() {
		return advise;
	}

	public byte[] getData() {
		return data;
	}

}
