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
 * Specifies the service name options.
 * 
 * @author Vitor Costa
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648772(VS.85).aspx">Name Service</a>
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648759(VS.85).aspx">DdeNameService Function</a>
 */
public interface NameServiceFlags {

	/**
	 * Registers the error code service name.
	 */
	int DNS_REGISTER = 1;

	/**
	 * Unregisters the error code service name. If the <i>hsz1</i> parameter
	 * from a call to the <b>DdeNameService</b> function is 0L, all service
	 * names registered by the server will be unregistered.
	 */
	int DNS_UNREGISTER = 2;

	/**
	 * Turns on service name initiation filtering. The filter prevents a server
	 * from receiving {@code XTYP_CONNECT} transactions for service names it has
	 * not registered. This is the default setting for this filter.
	 * <p>
	 * If a server application does not register any service names, the
	 * application cannot receive {@code XTYP_WILDCONNECT} transactions.
	 */
	int DNS_FILTERON = 4;

	/**
	 * Turns off service name initiation filtering. If this flag is specified,
	 * the server receives an {@code XTYP_CONNECT} transaction whenever another
	 * DDE application calls the <b>DdeConnect</b> function, regardless of the
	 * service name.
	 */
	int DNS_FILTEROFF = 8;
	
}
