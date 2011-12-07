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

import com.google.code.jdde.client.ClientConversation;
import com.google.code.jdde.client.DdeClient;
import com.google.code.jdde.ddeml.CallbackParameters;
import com.google.code.jdde.ddeml.constants.DmlError;
import com.google.code.jdde.misc.Conversation;
import com.google.code.jdde.misc.DdeApplication;
import com.google.code.jdde.server.DdeServer;
import com.google.code.jdde.server.ServerConversation;

/**
 * 
 * @author Vitor Costa
 *
 * @param <A>
 * @param <C>
 */
public abstract class ErrorEvent<A extends DdeApplication, C extends Conversation>
		extends ConversationEvent<A, C> {

	private final DmlError error;

	public ErrorEvent(A application, C conversation, CallbackParameters parameters) {
		super(application, conversation);

		Integer errorCode = (Integer) parameters.getDwData1();
		error = DmlError.findErrorByNativeValue(errorCode);
	}

	public DmlError getError() {
		return error;
	}

	public static class ClientErrorEvent extends ErrorEvent<DdeClient, ClientConversation> {

		public ClientErrorEvent(DdeClient client,
				ClientConversation conversation, CallbackParameters parameters) {
			super(client, conversation, parameters);
		}

	}

	public static class ServerErrorEvent extends ErrorEvent<DdeServer, ServerConversation> {

		public ServerErrorEvent(DdeServer server,
				ServerConversation conversation, CallbackParameters parameters) {
			super(server, conversation, parameters);
		}

	}

}