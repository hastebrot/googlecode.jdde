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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.code.jdde.ddeml.constants.TransactionFlags;

/**
 * 
 * @author Vitor Costa
 */
public class JavaDdeUtil {

	private static Map<Integer, String> transactionDictionary;
	
	public static Logger getLogger() {
		return Logger.getLogger("com.google.code.jdde");
	}

	public static String translateTransaction(int uType) {
		if (transactionDictionary == null) {
			transactionDictionary = new HashMap<Integer, String>();
			
			transactionDictionary.put(TransactionFlags.XTYP_ADVDATA, "XTYP_ADVDATA");
			transactionDictionary.put(TransactionFlags.XTYP_ADVREQ, "XTYP_ADVREQ");
			transactionDictionary.put(TransactionFlags.XTYP_ADVSTART, "XTYP_ADVSTART");
			transactionDictionary.put(TransactionFlags.XTYP_ADVSTOP, "XTYP_ADVSTOP");
			transactionDictionary.put(TransactionFlags.XTYP_CONNECT, "XTYP_CONNECT");
			transactionDictionary.put(TransactionFlags.XTYP_CONNECT_CONFIRM, "XTYP_CONNECT_CONFIRM");
			transactionDictionary.put(TransactionFlags.XTYP_DISCONNECT, "XTYP_DISCONNECT");
			transactionDictionary.put(TransactionFlags.XTYP_ERROR, "XTYP_ERROR");
			transactionDictionary.put(TransactionFlags.XTYP_EXECUTE, "XTYP_EXECUTE");
			transactionDictionary.put(TransactionFlags.XTYP_MONITOR, "XTYP_MONITOR");
			transactionDictionary.put(TransactionFlags.XTYP_POKE, "XTYP_POKE");
			transactionDictionary.put(TransactionFlags.XTYP_REGISTER, "XTYP_REGISTER");
			transactionDictionary.put(TransactionFlags.XTYP_REQUEST, "XTYP_REQUEST");
			transactionDictionary.put(TransactionFlags.XTYP_UNREGISTER, "XTYP_UNREGISTER");
			transactionDictionary.put(TransactionFlags.XTYP_WILDCONNECT, "XTYP_WILDCONNECT");
			transactionDictionary.put(TransactionFlags.XTYP_XACT_COMPLETE, "XTYP_XACT_COMPLETE");
		}
		
		String translation = transactionDictionary.get(uType);
		return translation != null ? translation : "Unknown";
	}

}
