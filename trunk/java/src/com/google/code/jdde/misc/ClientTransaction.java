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

import com.google.code.jdde.client.ClientConversation;
import com.google.code.jdde.ddeml.DdeAPI;
import com.google.code.jdde.ddeml.Pointer;
import com.google.code.jdde.ddeml.constants.DmlError;

/**
 * 
 * @author Vitor Costa
 */
public class ClientTransaction {

	private final int idInst;
	private final int hConv;
	private final MessageLoop loop;
	
	private int error;
	private int result;
	private byte[] data;
	
	public ClientTransaction(ClientConversation conversation) {
		this.idInst = conversation.getApplication().getIdInst();
		this.hConv = conversation.getHConv();
		this.loop = conversation.getApplication().getLoop();
	}
	
	public void call(byte[] pData, String hszItem, int wFmt, int wType,
			int dwTimeout) {
		
		call(pData, hszItem, wFmt, wType, dwTimeout, null);
	}
	
	public void call(final byte[] pData, final String hszItem, final int wFmt,
			final int wType, final int dwTimeout, final PosTransactionTask task) {
		
		loop.invokeAndWait(new Runnable() {
			public void run() {
				Pointer<Integer> $dwResult = new Pointer<Integer>();
				data = DdeAPI.ClientTransaction(idInst, pData, hConv, hszItem,
						wFmt, wType, dwTimeout, $dwResult);

				if (data == null) {
					error = DdeAPI.GetLastError(idInst);
				}
				else {
					result = $dwResult.value;
					if (task != null) {
						task.call(ClientTransaction.this);
					}
				}
			}
		});
	}
	
	public void throwExceptionOnError() {
		DmlError.throwExceptionIfValidError(error);
	}

	public int getError() {
		return error;
	}

	public int getResult() {
		return result;
	}

	public byte[] getData() {
		return data;
	}
	
}
