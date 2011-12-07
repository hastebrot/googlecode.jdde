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

import com.google.code.jdde.ddeml.CallbackParameters;
import com.google.code.jdde.misc.ClipboardFormat;
import com.google.code.jdde.server.DdeServer;
import com.google.code.jdde.server.ServerConversation;

/**
 * 
 * @author Vitor Costa
 */
public class AdviseStartEvent extends ConversationEvent<DdeServer, ServerConversation> {

	private String topic;
	private String item;
	
	private ClipboardFormat format;
	
	public AdviseStartEvent(DdeServer server, ServerConversation conversation,
			CallbackParameters parameters) {
		super(server, conversation);

		format = new ClipboardFormat(parameters.getUFmt());
		
		topic = parameters.getHsz1();
		item = parameters.getHsz2();
	}
	
	public String getTopic() {
		return topic;
	}

	public String getItem() {
		return item;
	}

	public ClipboardFormat getFormat() {
		return format;
	}
	
}
