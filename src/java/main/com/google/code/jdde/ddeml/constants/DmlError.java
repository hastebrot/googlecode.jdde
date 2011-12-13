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

package com.google.code.jdde.ddeml.constants;

import com.google.code.jdde.misc.DdeException;

/**
 * 
 * @author Vitor Costa
 */
public enum DmlError {

//	NO_ERROR(0),
//	FIRST(0x4000),
	ADVACKTIMEOUT(0x4000),
	BUSY(0x4001),
	DATAACKTIMEOUT(0x4002),
	DLL_NOT_INITIALIZED(0x4003),
	DLL_USAGE(0x4004),
	EXECACKTIMEOUT(0x4005),
	INVALIDPARAMETER(0x4006),
	LOW_MEMORY(0x4007),
	MEMORY_ERROR(0x4008),
	NOTPROCESSED(0x4009),
	NO_CONV_ESTABLISHED(0x400a),
	POKEACKTIMEOUT(0x400b),
	POSTMSG_FAILED(0x400c),
	REENTRANCY(0x400d),
	SERVER_DIED(0x400e),
	SYS_ERROR(0x400f),
	UNADVACKTIMEOUT(0x4010),
	UNFOUND_QUEUE_ID(0x4011);
//	LAST(0x4011);
	
	private int nativeValue;
	
	private DmlError(int nativeValue) {
		this.nativeValue = nativeValue;
	}
	
	public static DmlError findErrorByNativeValue(Integer value) {
		if (value == null) {
			return null;
		}
		for (DmlError error : DmlError.values()) {
			if (error.nativeValue == value) {
				return error;
			}
		}
		return null;
	}
	
	public static void throwExceptionIfValidError(Integer value) {
		DmlError error = findErrorByNativeValue(value);
		if (error != null) {
			throw new DdeException(error);
		}
	}
	
}
