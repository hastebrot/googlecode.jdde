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

import java.text.MessageFormat;

import com.google.code.jdde.misc.JavaDdeUtil;

/**
 * 
 * @author Vitor Costa
 */
public class CallbackParameters {

	private int idThread;

	private int uType;
	private int uFmt;
	private int hconv;

	private String hsz1;
	private String hsz2;

	private byte[] hdata;

	private Object dwData1;
	private Object dwData2;
	
	public CallbackParameters(int idThread,
			int uType, int uFmt, int hconv,
			String hsz1, String hsz2,
			byte[] hdata,
			Object dwData1, Object dwData2) {

		this.idThread = idThread;
		
		this.uType = uType;
		this.uFmt = uFmt;
		this.hconv = hconv;
		
		this.hsz1 = hsz1;
		this.hsz2 = hsz2;
		
		this.hdata = hdata;
		
		this.dwData1 = dwData1;
		this.dwData2 = dwData2;
	}

	public int getIdThread() {
		return idThread;
	}

	public int getUType() {
		return uType;
	}

	public int getUFmt() {
		return uFmt;
	}

	public int getHconv() {
		return hconv;
	}

	public String getHsz1() {
		return hsz1;
	}

	public String getHsz2() {
		return hsz2;
	}

	public byte[] getHdata() {
		return hdata;
	}

	public Object getDwData1() {
		return dwData1;
	}

	public Object getDwData2() {
		return dwData2;
	}

	@Override
	public String toString() {
		String pattern = "[idThread: {0}; uType: {1}; uFmt: {2}; hconv: {3}; hsz1: {4}; hsz2: {5};]";
		return MessageFormat.format(pattern, Integer.toString(idThread),
				JavaDdeUtil.translateTransaction(uType), Integer.toString(uFmt),
				Integer.toString(hconv), hsz1, hsz2);
	}
	
}
