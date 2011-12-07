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

import java.util.logging.Logger;

import com.google.code.jdde.client.event.ClientErrorListener;
import com.google.code.jdde.client.event.ClientRegistrationListener;
import com.google.code.jdde.ddeml.CallbackParameters;
import com.google.code.jdde.ddeml.DdeCallback;
import com.google.code.jdde.ddeml.constants.FlagCallbackResult;
import com.google.code.jdde.ddeml.constants.TransactionFlags;
import com.google.code.jdde.event.ErrorEvent.ClientErrorEvent;
import com.google.code.jdde.event.RegisterEvent.ClientRegisterEvent;
import com.google.code.jdde.event.UnregisterEvent.ClientUnregisterEvent;
import com.google.code.jdde.misc.JavaDdeUtil;
import com.google.code.jdde.misc.SupportedServiceTopic;

/**
 * 
 * @author Vitor Costa
 */
class ClientCallbackImpl implements DdeCallback {

	private static Logger logger = JavaDdeUtil.getLogger();
	
	private DdeClient client;
	
	public ClientCallbackImpl(DdeClient client) {
		this.client = client;
	}
	
	private ClientConversation findConversation(CallbackParameters parameters) {
		return client.findConversation(parameters.getHconv());
	}
	
	@Override
	public boolean DdeBooleanCallback(CallbackParameters parameters) {
		logger.warning("DdeClient should never receive a boolean callback");
		return false;
	}

	@Override
	public byte[] DdeDataCallback(CallbackParameters parameters) {
		logger.warning("DdeClient should never receive a data callback");
		return null;
	}

	@Override
	public FlagCallbackResult DdeFlagCallback(CallbackParameters parameters) {
		switch (parameters.getUType()) {
		case TransactionFlags.XTYP_ADVDATA:
			ClientConversation conversation = findConversation(parameters);
			if (conversation != null) {
				conversation.fireValueChanged(parameters);
			}
			break;
		default:
			String tx = JavaDdeUtil.translateTransaction(parameters.getUType());
			logger.warning("DdeClient should never receive a flag callback of type " + tx);
			break;
		}

		return FlagCallbackResult.DDE_FACK;
	}

	@Override
	public void DdeNotificationCallback(CallbackParameters parameters) {
		ClientConversation conversation = null;
		
		ClientErrorListener errorListener = client.getErrorListener();
		ClientRegistrationListener registrationListener = client.getRegistrationListener();
		
		switch (parameters.getUType()) {
		case TransactionFlags.XTYP_DISCONNECT:
			conversation = findConversation(parameters);
			if (conversation != null) {
				conversation.fireOnDisconnect(parameters);
			}
			break;
		case TransactionFlags.XTYP_ERROR:
			if (parameters.getHconv() != 0) {
				conversation = findConversation(parameters);
			}
			if (errorListener != null) {
				ClientErrorEvent event = new ClientErrorEvent(client, conversation, parameters);
				errorListener.onError(event);
			}
			break;
		case TransactionFlags.XTYP_REGISTER:
			if (registrationListener != null) {
				ClientRegisterEvent event = new ClientRegisterEvent(client, parameters);
				registrationListener.onRegister(event);
			}
			break;
		case TransactionFlags.XTYP_UNREGISTER:
			if (registrationListener != null) {
				ClientUnregisterEvent event = new ClientUnregisterEvent(client, parameters);
				registrationListener.onUnregister(event);
			}
			break;
		case TransactionFlags.XTYP_XACT_COMPLETE:
			conversation = findConversation(parameters);
			if (conversation != null) {
				conversation.fireAsyncTransactionCompleted(parameters);
			}
			break;
		default:
			String tx = JavaDdeUtil.translateTransaction(parameters.getUType());
			logger.warning("DdeClient should never receive a notification callback of type " + tx);
			break;
		}
	}
	
	@Override
	public SupportedServiceTopic[] DdeWildConnectCallback(CallbackParameters parameters) {
		logger.warning("DdeClient should never receive a wild connect callback");
		return null;
	}
	
}
