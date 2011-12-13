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

package com.google.code.jdde.ddeml;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.code.jdde.ddeml.constants.FlagCallbackResult;
import com.google.code.jdde.misc.JavaDdeUtil;
import com.google.code.jdde.misc.SupportedServiceTopic;

/**
 * 
 * @author Vitor Costa
 */
class CallbackManager {

	private static Logger logger = JavaDdeUtil.getLogger();
	
	private static Map<Integer, Integer> instances;
	private static Map<Integer, DdeCallback> callbacks;

	static {
		instances = new HashMap<Integer, Integer>();
		callbacks = new HashMap<Integer, DdeCallback>();
	}
	
	/**
	 * This method can only be invoked by the same thread that initialized the DDEML.
	 * 
	 * @param idInst
	 * @param callback
	 */
	static void register(int idInst, DdeCallback callback) {
		int idThread = WinAPI.GetCurrentThreadId();
		
		instances.put(idThread, idInst);
		callbacks.put(idThread, callback);
	}
	
	/**
	 * This method can only be invoked by the same thread that initialized the DDEML.
	 */
	static void unregister() {
		int idThread = WinAPI.GetCurrentThreadId();
		
		instances.remove(idThread);
		callbacks.remove(idThread);
	}
	
	public static int getIdInst(int idThread) {
		Integer idInst = instances.get(idThread);
		return (idInst != null) ? idInst.intValue() : 0;
	}
	
	public static boolean DdeBooleanCallback(CallbackParameters parameters) {
		logger.finer(parameters.toString());
		DdeCallback callback = callbacks.get(parameters.getIdThread());
		if (callback != null) {
			return callback.DdeBooleanCallback(parameters);
		}
		else {	// Should never happen..
			logger.warning("A callback was not found with the given thread id: " + parameters.getIdThread());
			return false;
		}
	}
	
	public static byte[] DdeDataCallback(CallbackParameters parameters) {
		logger.finer(parameters.toString());
		DdeCallback callback = callbacks.get(parameters.getIdThread());
		if (callback != null) {
			return callback.DdeDataCallback(parameters);
		}
		else {	// Should never happen..
			logger.warning("A callback was not found with the given thread id: " + parameters.getIdThread());
			return null;
		}
	}
	
	public static int DdeFlagCallback(CallbackParameters parameters) {
		logger.finer(parameters.toString());
		DdeCallback callback = callbacks.get(parameters.getIdThread());
		if (callback != null) {
			FlagCallbackResult result = callback.DdeFlagCallback(parameters);
			if (result == null) {
				result = FlagCallbackResult.DDE_FNOTPROCESSED;
			}
			return result.getValue();
		}
		else {	// Should never happen..
			logger.warning("A callback was not found with the given thread id: " + parameters.getIdThread());
			return FlagCallbackResult.DDE_FNOTPROCESSED.getValue();
		}
	}
	
	public static void DdeNotificationCallback(CallbackParameters parameters) {
		logger.finer(parameters.toString());
		DdeCallback callback = callbacks.get(parameters.getIdThread());
		if (callback != null) {
			callback.DdeNotificationCallback(parameters);
		}
		else {	// Should never happen..
			logger.warning("A callback was not found with the given thread id: " + parameters.getIdThread());
		}
	}
	
	public static String[] DdeWildConnectCallback(CallbackParameters parameters) {
		logger.finer(parameters.toString());
		DdeCallback callback = callbacks.get(parameters.getIdThread());
		if (callback != null) {
			SupportedServiceTopic[] result = callback.DdeWildConnectCallback(parameters);
			
			if (result != null) {
				String[] strings = new String[result.length * 2];
				
				for (int i = 0, j = 0; i < result.length; i++) {
					strings[j++] = result[i].getService();
					strings[j++] = result[i].getTopic();
				}
				return strings;
			}
		}
		else {	// Should never happen..
			logger.warning("A callback was not found with the given thread id: " + parameters.getIdThread());
		}
		
		return null;
	}
	
}
