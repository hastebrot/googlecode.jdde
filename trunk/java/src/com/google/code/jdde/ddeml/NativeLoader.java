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
class NativeLoader {

	private static boolean loaded;
	
	public static synchronized void load() {
		if (!loaded) {
		    int bits = 32;
	        if (System.getProperty("os.arch").indexOf("64") != -1) {
	            bits = 64;
	        }
	        System.loadLibrary("jDDE_" + bits);
			loaded = true;
		}
	}
	
}
