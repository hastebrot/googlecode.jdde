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

import java.util.ResourceBundle;

import com.google.code.jdde.ddeml.constants.DmlError;

/**
 * 
 * @author Vitor Costa
 */
public class DdeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private DmlError error;
	
	public DdeException(DmlError error) {
		this.error = error;
	}
	
	public DmlError getError() {
		return error;
	}
	
	@Override
	public String getMessage() {
		String errorName = "DMLERR_" + error.name();
		
		ResourceBundle rb = ResourceBundle.getBundle("com.google.code.jdde.ddeml.constants.DmlError");
		return "[" + errorName + "] " + rb.getString(errorName);
	}
	
}
