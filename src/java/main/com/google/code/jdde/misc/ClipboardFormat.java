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

import com.google.code.jdde.ddeml.WinAPI;

/**
 * 
 * @author Vitor Costa
 */
public class ClipboardFormat {

	private int value;
	
	public ClipboardFormat(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		ClipboardFormat other = (ClipboardFormat) obj;
		return value == other.value;
	}

	public static final ClipboardFormat TEXT = new ClipboardFormat(1);
	public static final ClipboardFormat BITMAP = new ClipboardFormat(2);
	
	public static ClipboardFormat register(String format) {
		int fValue = WinAPI.RegisterClipboardFormat(format);
		return new ClipboardFormat(fValue);
	}
	
}