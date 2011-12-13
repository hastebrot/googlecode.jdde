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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.code.jdde.client.event.AdviseDataListener;
import com.google.code.jdde.client.event.AsyncTransactionListener;
import com.google.code.jdde.client.event.ClientDisconnectListener;
import com.google.code.jdde.ddeml.CallbackParameters;
import com.google.code.jdde.ddeml.constants.TransactionFlags;
import com.google.code.jdde.event.DisconnectEvent.ClientDisconnectEvent;
import com.google.code.jdde.misc.ClientTransaction;
import com.google.code.jdde.misc.ClipboardFormat;
import com.google.code.jdde.misc.Conversation;
import com.google.code.jdde.misc.JavaDdeUtil;
import com.google.code.jdde.misc.PosTransactionTask;

/**
 * 
 * @author Vitor Costa
 */
public class ClientConversation extends Conversation {

	private static Logger logger = JavaDdeUtil.getLogger();
	
	private ClientDisconnectListener disconnectListener;
	
	private List<Advise> advises;
	private Map<Integer, AsyncTransaction> transactions;
	
	ClientConversation(DdeClient client, int hConv, String service, String topic) {
		super(client, hConv, service, topic);
		
		advises = new ArrayList<Advise>();
		transactions = new HashMap<Integer, AsyncTransaction>();
	}
	
	/* ************************************ *
	 ********* getters and setters ********** 
	 * ************************************ */
	
	public void setDisconnectListener(ClientDisconnectListener disconnectListener) {
		this.disconnectListener = disconnectListener;
	}
	
	public ClientDisconnectListener getDisconnectListener() {
		return disconnectListener;
	}
	
	public DdeClient getClient() {
		return (DdeClient) super.getApplication();
	}
	
	private final ClipboardFormat getDefaultFormat() {
		return getClient().getDefaultFormat();
	}
	
	private final int getDefaultTimeout() {
		return getClient().getDefaultTimeout();
	}
	
	/* ************************************ *
	 ************* xtyp_request ************* 
	 * ************************************ */
	
	public byte[] request(String item) {
		return request(item, getDefaultFormat(), getDefaultTimeout());
	}
	
	public byte[] request(String item, ClipboardFormat format, int timeout) {
		ClientTransaction tx = new ClientTransaction(this);
		tx.call(null, item, format.getValue(), TransactionFlags.XTYP_REQUEST, timeout);
		
		tx.throwExceptionOnError();
		return tx.getData();
	}
	
	public AsyncTransaction requestAsync(String item, AsyncTransactionListener listener) {
		return requestAsync(item, getDefaultFormat(), listener);
	}
	
	public AsyncTransaction requestAsync(String item, ClipboardFormat format,
			AsyncTransactionListener listener) {
		
		AsyncTransactionTask task = new AsyncTransactionTask(listener);
		
		ClientTransaction tx = new ClientTransaction(this);
		tx.call(null, item, format.getValue(),
				TransactionFlags.XTYP_REQUEST, TransactionFlags.TIMEOUT_ASYNC, task);
		
		tx.throwExceptionOnError();
		
		return task.asyncTx;
	}
	
	/* ************************************ *
	 ************* xtyp_execute ************* 
	 * ************************************ */
	
	public void execute(String command) {
		execute(command, getDefaultTimeout());
	}
	
	public void execute(String command, int timeout) {
		ClientTransaction tx = new ClientTransaction(this);
		tx.call(command.getBytes(), null, ClipboardFormat.TEXT.getValue(),
				TransactionFlags.XTYP_EXECUTE, timeout);

		tx.throwExceptionOnError();
	}
	
	public AsyncTransaction executeAsync(String command, AsyncTransactionListener listener) {
		AsyncTransactionTask task = new AsyncTransactionTask(listener);
		
		ClientTransaction tx = new ClientTransaction(this);
		tx.call(command.getBytes(), null, ClipboardFormat.TEXT.getValue(),
				TransactionFlags.XTYP_EXECUTE, TransactionFlags.TIMEOUT_ASYNC, task);
		
		tx.throwExceptionOnError();
		
		return task.asyncTx;
	}
	
	/* ************************************ *
	 ************** xtyp_poke *************** 
	 * ************************************ */
	
	public void poke(String item, byte[] data) {
		poke(item, data, getDefaultFormat(), getDefaultTimeout());
	}
	
	public void poke(String item, byte[] data, ClipboardFormat format, int timeout) {
		ClientTransaction tx = new ClientTransaction(this);
		tx.call(data, item, format.getValue(), TransactionFlags.XTYP_POKE, timeout);
		
		tx.throwExceptionOnError();
	}
	
	public AsyncTransaction pokeAsync(String item, byte[] data,
			AsyncTransactionListener listener) {
		return pokeAsync(item, data, getDefaultFormat(), listener);
	}

	public AsyncTransaction pokeAsync(String item, byte[] data,
			ClipboardFormat format, AsyncTransactionListener listener) {

		AsyncTransactionTask task = new AsyncTransactionTask(listener);
		
		ClientTransaction tx = new ClientTransaction(this);
		tx.call(data, item, format.getValue(), TransactionFlags.XTYP_POKE,
				TransactionFlags.TIMEOUT_ASYNC, task);

		tx.throwExceptionOnError();
		
		return task.asyncTx;
	}
	
	/* ************************************ *
	 ************ xtyp_advstart ************* 
	 * ************************************ */
	
	public Advise startAdvise(String item, AdviseDataListener listener) {
		return startAdvise(item, getDefaultFormat(), getDefaultTimeout(), listener);
	}
	
	public Advise startAdvise(String item, ClipboardFormat format, int timeout,
			AdviseDataListener listener) {
		
		StartAdviseTask task = new StartAdviseTask(item, format, listener);
		
		ClientTransaction tx = new ClientTransaction(this);
		tx.call(null, item, format.getValue(),
				TransactionFlags.XTYP_ADVSTART, timeout, task);
		
		tx.throwExceptionOnError();
		
		return task.advise;
	}
	
	/* ************************************ *
	 ********** dispatch callbacks ********** 
	 * ************************************ */
	
	void fireValueChanged(CallbackParameters parameters) {
		for (Advise advise : advises) {
			advise.fireValueChanged(parameters);
		}
	}
	
	void fireOnDisconnect(CallbackParameters parameters) {
		if (disconnectListener != null) {
			ClientDisconnectEvent event = new ClientDisconnectEvent(getClient(), this, parameters);
			disconnectListener.onDisconnect(event);
		}
	}
	
	void fireAsyncTransactionCompleted(CallbackParameters parameters) {
		Object txId = parameters.getDwData1();

		if (txId == null) {
			logger.severe("CallbackParameters.dwData1 is null");
		}
		else if (txId.getClass() != Integer.class) {
			logger.severe("CallbackParameters.dwData1 is not of type Integer");
		}
		else {
			AsyncTransaction asyncTx = transactions.get(txId);
			
			if (asyncTx != null) { 
				asyncTx.fireAsyncTransactionCompleted(parameters);
				transactions.remove(txId);
			}
			else {
				logger.severe("Could not find transaction with the given id: " + txId);
			}
		}
	}
	
	void adviseStoped(Advise advise) {
		advises.remove(advise);
	}
	
	void transactionCompleted(Integer txId) {
		transactions.remove(txId);
	}

	/* ************************************ *
	 ************ helper classes ************ 
	 * ************************************ */
	
	private class StartAdviseTask implements PosTransactionTask {
		
		private Advise advise;
		
		private String item;
		private ClipboardFormat format;
		private AdviseDataListener listener;
		
		public StartAdviseTask(String item, ClipboardFormat format,
				AdviseDataListener listener) {

			this.format = format;
			this.item = item;
			this.listener = listener;
		}

		@Override
		public void call(ClientTransaction clientTx) {
			advise = new Advise(getClient(), ClientConversation.this, item, format, listener);
			advises.add(advise);
		}
		
	}
	
	private class AsyncTransactionTask implements PosTransactionTask {
		
		private AsyncTransaction asyncTx;
		private AsyncTransactionListener listener;
		
		public AsyncTransactionTask(AsyncTransactionListener listener) {
			this.listener = listener;
		}
		
		@Override
		public void call(ClientTransaction clientTx) {
			Integer txId = clientTx.getResult();
			asyncTx = new AsyncTransaction(getClient(), ClientConversation.this, txId, listener);

			transactions.put(txId, asyncTx);
		}
		
	};
	
}
