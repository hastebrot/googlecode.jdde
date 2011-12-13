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

/**
 * The return type from a callback of a transaction that belongs to the {@code
 * XCLASS_FLAGS} class.
 * <p>
 * The {@code XCLASS_FLAGS} transaction class consists of the following types:
 * 
 * <ul>
 * <li>{@code XTYP_ADVDATA}</li>
 * <li>{@code XTYP_EXECUTE}</li>
 * <li>{@code XTYP_POKE}</li>
 * </ul>
 * 
 * @author Vitor Costa
 */
public enum FlagCallbackResult {

	/**
	 * The server/client processes the transaction.
	 */
	DDE_FACK(0x8000),
	
	/**
	 * The server/client is too busy to process the transaction.
	 */
	DDE_FBUSY(0x4000),
	
	/**
	 * The server/client rejects the transaction.
	 */
	DDE_FNOTPROCESSED(0);
	
	/**
	 * The native value of this flag.
	 */
	private int value;
	
	/**
	 * Private constructor if this enum. 
	 */
	private FlagCallbackResult(int value) {
		this.value = value;
	}
	
	/**
	 * Returns the native value of this flag.
	 * 
	 * @return the native value of this flag.
	 */
	public int getValue() {
		return value;
	}
	
}
