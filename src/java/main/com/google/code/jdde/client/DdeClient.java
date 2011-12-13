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

import com.google.code.jdde.client.event.ClientErrorListener;
import com.google.code.jdde.client.event.ClientRegistrationListener;
import com.google.code.jdde.ddeml.DdeAPI;
import com.google.code.jdde.ddeml.Pointer;
import com.google.code.jdde.ddeml.constants.DmlError;
import com.google.code.jdde.ddeml.constants.InitializeFlags;
import com.google.code.jdde.misc.ClipboardFormat;
import com.google.code.jdde.misc.DdeApplication;

/**
 * 
 * @author Vitor Costa
 */
public class DdeClient extends DdeApplication {

	private int defaultTimeout;
	private ClipboardFormat defaultFormat;
	
	private ClientErrorListener errorListener;
	private ClientRegistrationListener registrationListener;
	
	public DdeClient() {
		defaultTimeout = 9999;
		defaultFormat = ClipboardFormat.TEXT;
		
		initialize(new ClientCallbackImpl(this), 
				InitializeFlags.APPCLASS_STANDARD |
				InitializeFlags.APPCMD_CLIENTONLY);
	}
	
	/* ************************************ *
	 ********* getters and setters ********** 
	 * ************************************ */
	
	public int getDefaultTimeout() {
		return defaultTimeout;
	}

	public void setDefaultTimeout(int defaultTimeout) {
		this.defaultTimeout = defaultTimeout;
	}

	public ClipboardFormat getDefaultFormat() {
		return defaultFormat;
	}

	public void setDefaultFormat(ClipboardFormat defaultFormat) {
		this.defaultFormat = defaultFormat;
	}
	
	public ClientErrorListener getErrorListener() {
		return errorListener;
	}

	public void setErrorListener(ClientErrorListener errorListener) {
		this.errorListener = errorListener;
	}
	
	public ClientRegistrationListener getRegistrationListener() {
		return registrationListener;
	}

	public void setRegistrationListener(ClientRegistrationListener registrationListener) {
		this.registrationListener = registrationListener;
	}

	/* ************************************ *
	 *********** ddeml api calls ************ 
	 * ************************************ */
	
	public ClientConversation connect(final String service, final String topic) {
		final Pointer<Integer> error = new Pointer<Integer>();
		final Pointer<Integer> hConv = new Pointer<Integer>();
		
		loop.invokeAndWait(new Runnable() {
			public void run() {
				hConv.value = DdeAPI.Connect(idInst, service, topic, null);
				
				if (hConv.value == 0) {
					error.value = DdeAPI.GetLastError(idInst);
				}
			}
		});

		DmlError.throwExceptionIfValidError(error.value);
		
		ClientConversation conversation = new ClientConversation(this, hConv.value, service, topic);
		conversations.add(conversation);
		
		return conversation;
	}

	@Override
	protected ClientConversation findConversation(int hConv) {
		return (ClientConversation) super.findConversation(hConv);
	}

}
