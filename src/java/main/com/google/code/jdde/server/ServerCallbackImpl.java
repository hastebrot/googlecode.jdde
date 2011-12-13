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

import java.util.logging.Logger;

import com.google.code.jdde.ddeml.CallbackParameters;
import com.google.code.jdde.ddeml.DdeCallback;
import com.google.code.jdde.ddeml.constants.FlagCallbackResult;
import com.google.code.jdde.ddeml.constants.TransactionFlags;
import com.google.code.jdde.event.ConnectConfirmEvent;
import com.google.code.jdde.event.ConnectEvent;
import com.google.code.jdde.event.ErrorEvent.ServerErrorEvent;
import com.google.code.jdde.event.RegisterEvent.ServerRegisterEvent;
import com.google.code.jdde.event.UnregisterEvent.ServerUnregisterEvent;
import com.google.code.jdde.misc.JavaDdeUtil;
import com.google.code.jdde.misc.SupportedServiceTopic;
import com.google.code.jdde.server.event.ConnectionListener;
import com.google.code.jdde.server.event.ServerErrorListener;
import com.google.code.jdde.server.event.ServerRegistrationListener;
import com.google.code.jdde.server.event.TransactionListener;

/**
 * 
 * @author Vitor Costa
 */
class ServerCallbackImpl implements DdeCallback {
	
	private static Logger logger = JavaDdeUtil.getLogger();
	
	private DdeServer server;
	
	public ServerCallbackImpl(DdeServer server) {
		this.server = server;
	}
	
	private ServerConversation findConversation(CallbackParameters parameters) {
		return server.findConversation(parameters.getHconv());
	}

	@Override
	public boolean DdeBooleanCallback(CallbackParameters parameters) {
		boolean result = false;
		
		switch (parameters.getUType()) {
		case TransactionFlags.XTYP_ADVSTART:
			ServerConversation conversation = findConversation(parameters);
			if (conversation != null) {
				return conversation.fireOnAdviseStart(parameters);
			}
			break;
		case TransactionFlags.XTYP_CONNECT:
			ConnectEvent event = new ConnectEvent(server, parameters);

			ConnectionListener connectionListener = server.getConnectionListener();
			if (connectionListener != null) {
				result = connectionListener.onConnect(event);
			}
			break;
		default:
			String tx = JavaDdeUtil.translateTransaction(parameters.getUType());
			logger.warning("DdeServer should never receive a boolean callback of type " + tx);
			break;
		}
		
		return result;
	}

	@Override
	public byte[] DdeDataCallback(CallbackParameters parameters) {
		ServerConversation conversation = null;
		
		switch (parameters.getUType()) {
		case TransactionFlags.XTYP_ADVREQ:
			conversation = findConversation(parameters);
			if (conversation != null) {
				return conversation.fireOnAdviseRequest(parameters);
			}
			break;
		case TransactionFlags.XTYP_REQUEST:
			conversation = findConversation(parameters);
			if (conversation != null) {
				return conversation.fireOnRequest(parameters);
			}
			break;
		default:
			String tx = JavaDdeUtil.translateTransaction(parameters.getUType());
			logger.warning("DdeServer should never receive a data callback of type " + tx);
			break;
		}
		return null;
	}

	@Override
	public FlagCallbackResult DdeFlagCallback(CallbackParameters parameters) {
		ServerConversation conversation = findConversation(parameters);
		if (conversation == null) {
			return FlagCallbackResult.DDE_FNOTPROCESSED;
		}
		
		switch (parameters.getUType()) {
		case TransactionFlags.XTYP_EXECUTE:
			return conversation.fireOnExecute(parameters);
		case TransactionFlags.XTYP_POKE:
			return conversation.fireOnPoke(parameters);
		default:
			String tx = JavaDdeUtil.translateTransaction(parameters.getUType());
			logger.warning("DdeServer should never receive a flag callback of type " + tx);
			return FlagCallbackResult.DDE_FNOTPROCESSED;
		}
	}

	@Override
	public void DdeNotificationCallback(CallbackParameters parameters) {
		ServerConversation conversation = null;
		
		ConnectionListener connectionListener = server.getConnectionListener();
		TransactionListener transactionListener = server.getTransactionListener();
		ServerErrorListener errorListener = server.getErrorListener();
		ServerRegistrationListener registrationListener = server.getRegistrationListener();
		
		switch (parameters.getUType()) {
		case TransactionFlags.XTYP_ADVSTOP:
			conversation = findConversation(parameters);
			if (conversation != null) {
				conversation.fireOnAdviseStop(parameters);
			}
			break;
		case TransactionFlags.XTYP_CONNECT_CONFIRM:
			conversation = new ServerConversation(
					server,
					parameters.getHconv(),
					parameters.getHsz2(),
					parameters.getHsz1()
			);
			conversation.setTransactionListener(transactionListener);
			server.addConversation(conversation);

			
			if (connectionListener != null) {
				ConnectConfirmEvent event = new ConnectConfirmEvent(
						server, conversation, parameters);
				connectionListener.onConnectConfirm(event);
			}
			break;
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
				ServerErrorEvent event = new ServerErrorEvent(server, conversation, parameters);
				errorListener.onError(event);
			}
			break;
		case TransactionFlags.XTYP_REGISTER:
			if (registrationListener != null) {
				ServerRegisterEvent event = new ServerRegisterEvent(server, parameters);
				registrationListener.onRegister(event);
			}
			break;
		case TransactionFlags.XTYP_UNREGISTER:
			if (registrationListener != null) {
				ServerUnregisterEvent event = new ServerUnregisterEvent(server, parameters);
				registrationListener.onUnregister(event);
			}
			break;
		default:
			String tx = JavaDdeUtil.translateTransaction(parameters.getUType());
			logger.warning("DdeServer should never receive a notification callback of type " + tx);
			break;
		}
	}
	
	@Override
	public SupportedServiceTopic[] DdeWildConnectCallback(CallbackParameters parameters) {
		switch (parameters.getUType()) {
		case TransactionFlags.XTYP_WILDCONNECT:
			ConnectEvent event = new ConnectEvent(server, parameters);
			
			ConnectionListener connectionListener = server.getConnectionListener();
			if (connectionListener != null) {
				return connectionListener.onWildConnect(event);
			}
			break;
		default:
			String tx = JavaDdeUtil.translateTransaction(parameters.getUType());
			logger.warning("DdeServer should never receive a wild connect callback of type " + tx);
			break;	
		}
		
		return null;
	}

}
