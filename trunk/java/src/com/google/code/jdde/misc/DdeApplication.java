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

package com.google.code.jdde.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.code.jdde.ddeml.DdeAPI;
import com.google.code.jdde.ddeml.DdeCallback;
import com.google.code.jdde.ddeml.Pointer;
import com.google.code.jdde.ddeml.constants.DmlError;

/**
 * 
 * @author Vitor Costa
 */
public abstract class DdeApplication {

	private static Logger logger = JavaDdeUtil.getLogger();
	
	protected int idInst;
	protected final MessageLoop loop;
	
	protected List<Conversation> conversations;
	
	public DdeApplication() {
		loop = new MessageLoop();
		conversations = new ArrayList<Conversation>();
	}
	
	public int getIdInst() {
		return idInst;
	}
	
	public MessageLoop getLoop() {
		return loop;
	}
	
	protected void initialize(final DdeCallback callback, final int initializeFlags) {
		final Pointer<Integer> error = new Pointer<Integer>();
		
		loop.invokeAndWait(new Runnable() {
			public void run() {
				Pointer<Integer> $idInst = new Pointer<Integer>();
				int initError = DdeAPI.Initialize($idInst, callback, initializeFlags);
				
				if (initError != 0) {
					error.value = initError;
					return;
				}
				idInst = $idInst.value;
			}
		});

		DmlError.throwExceptionIfValidError(error.value);
	}
	
	public boolean uninitialize() {
		final Pointer<Boolean> result = new Pointer<Boolean>();
		
		loop.invokeAndWait(new Runnable() {
			public void run() {
				result.value = DdeAPI.Uninitialize_(idInst);
				
				if (result.value) {
					loop.terminate();
				}
			}
		});
		
		return result.value;
	}
	
	protected Conversation findConversation(int hConv) {
		for (int i = 0; i < conversations.size(); i++) {
			Conversation conversation = conversations.get(i);
			if (hConv == conversation.getHConv()) {
				return conversation;
			}
		}
		logger.warning("Conversation " + hConv + " hasn't been found!");
		return null;
	}
	
	protected boolean removeConversation(Conversation conversation) {
		boolean result = conversations.remove(conversation);
		if (!result) {
			logger.warning("Conversation " + conversation.getHConv() + " hasn't been found!");
		}
		return result;
	}
	
	/**
	 * Used for test purposes only.
	 */
	public void rethrowMessageLoopException() {
		Throwable t = loop.getLastThrowableCaught();
		if (t != null) {
			if (t instanceof Error) {
				throw (Error) t;
			}
			if (t instanceof RuntimeException) {
				throw (RuntimeException) t;
			}
			if (t instanceof Exception) {
				throw new RuntimeException(t);
			}
		}
	}
	
}
