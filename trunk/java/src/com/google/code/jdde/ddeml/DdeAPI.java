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

/**
 * 
 * @author Vitor Costa
 */
public class DdeAPI {

	/* First thing---register the natives */
	static {
		NativeLoader.load();
	}
	
	public static native boolean AbandonTransaction(int idInst, int hConv,
			int idTransaction);

	public static native byte[] ClientTransaction(int idInst, byte[] pData, int hConv,
			String hszItem, int wFmt, int wType, int dwTimeout, Pointer<Integer> $dwResult);

	//TODO pCC
	public static native int Connect(int idInst, String hszService,
			String hszTopic, ConvContext pCC);

	public static native boolean Disconnect(int hConv);
	
	public static native int GetLastError(int idInst);
	
	public static int Initialize(Pointer<Integer> $idInst, DdeCallback pfnCallback, int afCmd) {
		int initError = Initialize($idInst, afCmd);
		if (initError == 0) {
			CallbackManager.register($idInst.value, pfnCallback);
		}
		return initError;
	}
	
	private static native int Initialize(Pointer<Integer> $idInst, int afCmd);
	
	public static native boolean NameService(int idInst, String hsz1, int afCmd);
	
	public static native boolean PostAdvise(int idInst, String hszTopic, String hszItem);
	
	private static native boolean Uninitialize(int idInst);

	public static boolean Uninitialize_(int idInst) {
		boolean result = Uninitialize(idInst);
		if (result) {
			CallbackManager.unregister();
		}
		return result;
	}
	
}
