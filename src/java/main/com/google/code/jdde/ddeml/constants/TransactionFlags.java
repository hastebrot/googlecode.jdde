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
 * Specifies the available transaction types.
 * 
 * @author Vitor Costa
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms674614(VS.85).aspx">Transactions</a>
 */
public interface TransactionFlags {

	/**
	 * Used internally.
	 */
	int XCLASS_BOOL			= 0x1000;
	
	/**
	 * Used internally.
	 */
	int XCLASS_DATA			= 0x2000;
	
	/**
	 * Used internally.
	 */
	int XCLASS_FLAGS		= 0x4000;
	
	/**
	 * Used internally.
	 */
	int XCLASS_NOTIFICATION	= 0x8000;

	/**
	 * Used internally.
	 */
	int XTYPF_NOBLOCK	= 2;

	/**
	 * Instructs the server to notify the client of any data changes without
	 * actually sending the data. This flag gives the client the option of
	 * ignoring the notification or requesting the changed data from the server.
	 */
	int XTYPF_NODATA	= 4;
	
	/**
	 * Instructs the server to wait until the client acknowledges that it
	 * received the previous data item before sending the next data item. This
	 * flag prevents a fast server from sending data faster than the client can
	 * process it.
	 */
	int XTYPF_ACKREQ	= 8;

	/**
	 * The {@code XTYP_ADVDATA} transaction informs the client that the value of
	 * the data item has changed. The Dynamic Data Exchange (DDE) client
	 * callback function, <b>DdeCallback</b>, receives this transaction after
	 * establishing an advise loop with a server.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648714(VS.85).aspx">XTYP_ADVDATA Transaction</a>
	 */
	int XTYP_ADVDATA			= 0x0010 | XCLASS_FLAGS;

	/**
	 * The {@code XTYP_ADVREQ} transaction informs the server that an advise
	 * transaction is outstanding on the specified topic name and item name pair
	 * and that data corresponding to the topic name and item name pair has
	 * changed. The system sends this transaction to the Dynamic Data Exchange
	 * (DDE) callback function, <b>DdeCallback</b>, after the server calls the
	 * <b>DdePostAdvise</b> function.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648715(VS.85).aspx">XTYP_ADVREQ Transaction</a>
	 */
	int XTYP_ADVREQ				= 0x0020 | XCLASS_DATA | XTYPF_NOBLOCK;
	
	/**
	 * A client uses the {@code XTYP_ADVSTART} transaction to establish an
	 * advise loop with a server. A Dynamic Data Exchange (DDE) server callback
	 * function, <b>DdeCallback</b>, receives this transaction when a client
	 * specifies {@code XTYP_ADVSTART} as the {@code wType} parameter of the
	 * <b>DdeClientTransaction</b> function.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648716(VS.85).aspx">XTYP_ADVSTART Transaction</a>
	 */
	int XTYP_ADVSTART			= 0x0030 | XCLASS_BOOL;

	/**
	 * A client uses the {@code XTYP_ADVSTOP} transaction to end an advise loop
	 * with a server. A Dynamic Data Exchange (DDE) server callback function,
	 * <b>DdeCallback</b>, receives this transaction when a client specifies
	 * {@code XTYP_ADVSTOP} in the <b>DdeClientTransaction</b> function.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648717(VS.85).aspx">XTYP_ADVSTOP Transaction</a>
	 */
	int XTYP_ADVSTOP			= 0x0040 | XCLASS_NOTIFICATION;
	
	/**
	 * A client uses the {@code XTYP_CONNECT} transaction to establish a
	 * conversation. A Dynamic Data Exchange (DDE) server callback function,
	 * <b>DdeCallback</b>, receives this transaction when a client specifies a
	 * service name that the server supports (and a topic name that is not
	 * {@code NULL}) in a call to the <b>DdeConnect</b> function.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648718(VS.85).aspx">XTYP_CONNECT Transaction</a>
	 */
	int XTYP_CONNECT			= 0x0060 | XCLASS_BOOL | XTYPF_NOBLOCK;

	/**
	 * A Dynamic Data Exchange (DDE) server callback function,
	 * <b>DdeCallback</b>, receives the {@code XTYP_CONNECT_CONFIRM} transaction
	 * to confirm that a conversation has been established with a client and to
	 * provide the server with the conversation handle. The system sends this
	 * transaction as a result of a previous {@code XTYP_CONNECT} or {@code
	 * XTYP_WILDCONNECT} transaction.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648719(VS.85).aspx">XTYP_CONNECT_CONFIRM Transaction</a>
	 */
	int XTYP_CONNECT_CONFIRM	= 0x0070 | XCLASS_NOTIFICATION | XTYPF_NOBLOCK;

	/**
	 * An application's Dynamic Data Exchange (DDE) callback function,
	 * <b>DdeCallback</b>, receives the {@code XTYP_DISCONNECT} transaction when
	 * the application's partner in a conversation uses the <b>DdeDisconnect</b>
	 * function to terminate the conversation.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648720(VS.85).aspx">XTYP_DISCONNECT Transaction</a>
	 */
	int XTYP_DISCONNECT			= 0x00C0 | XCLASS_NOTIFICATION | XTYPF_NOBLOCK;
	
	/**
	 * A Dynamic Data Exchange (DDE) callback function, <b>DdeCallback</b>, receives
	 * the {@code XTYP_ERROR} transaction when a critical error occurs.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648721(VS.85).aspx">XTYP_ERROR Transaction</a>
	 */
	int XTYP_ERROR				= 0x0000 | XCLASS_NOTIFICATION | XTYPF_NOBLOCK;

	/**
	 * A client uses the {@code XTYP_EXECUTE} transaction to send a command
	 * string to the server. A Dynamic Data Exchange (DDE) server callback
	 * function, <b>DdeCallback</b>, receives this transaction when a client
	 * specifies {@code XTYP_EXECUTE} in the <b>DdeClientTransaction</b>
	 * function.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648722(VS.85).aspx">XTYP_EXECUTE Transaction</a>
	 */
	int XTYP_EXECUTE			= 0x0050 | XCLASS_FLAGS;

	/**
	 * A Dynamic Data Exchange (DDE) debugger's DDE callback function,
	 * <b>DdeCallback</b>, receives the {@code XTYP_MONITOR} transaction
	 * whenever a DDE event occurs in the system. To receive this transaction,
	 * an application must specify the {@code APPCLASS_MONITOR} value when it
	 * calls the <b>DdeInitialize</b> function.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648723(VS.85).aspx">XTYP_MONITOR Transaction</a>
	 */
	int XTYP_MONITOR			= 0x00F0 | XCLASS_NOTIFICATION | XTYPF_NOBLOCK;

	/**
	 * A client uses the {@code XTYP_POKE} transaction to send unsolicited data
	 * to the server. A Dynamic Data Exchange (DDE) server callback function,
	 * <b>DdeCallback</b>, receives this transaction when a client specifies
	 * {@code XTYP_POKE} in the <b>DdeClientTransaction</b> function.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648724(VS.85).aspx">XTYP_POKE Transaction</a>
	 */
	int XTYP_POKE				= 0x0090 | XCLASS_FLAGS;

	/**
	 * A Dynamic Data Exchange (DDE) callback function, <b>DdeCallback</b>, receives
	 * the {@code XTYP_REGISTER} transaction type whenever a Dynamic Data Exchange
	 * Management Library (DDEML) server application uses the <b>DdeNameService</b>
	 * function to register a service name, or whenever a non-DDEML application
	 * that supports the System topic is started.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648725(VS.85).aspx">XTYP_REGISTER Transaction</a>
	 */
	int XTYP_REGISTER			= 0x00A0 | XCLASS_NOTIFICATION | XTYPF_NOBLOCK;

	/**
	 * A client uses the {@code XTYP_REQUEST} transaction to request data from a
	 * server. A Dynamic Data Exchange (DDE) server callback function,
	 * <b>DdeCallback</b>, receives this transaction when a client specifies
	 * {@code XTYP_REQUEST} in the <b>DdeClientTransaction</b> function.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648726(VS.85).aspx">XTYP_REQUEST Transaction</a>
	 */
	int XTYP_REQUEST			= 0x00B0 | XCLASS_DATA;

	/**
	 * A Dynamic Data Exchange (DDE) callback function, <b>DdeCallback</b>,
	 * receives the {@code XTYP_UNREGISTER} transaction whenever a Dynamic Data
	 * Exchange Management Library (DDEML) server application uses the
	 * <b>DdeNameService</b> function to unregister a service name, or whenever
	 * a non-DDEML application that supports the System topic is terminated.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648727(VS.85).aspx">XTYP_UNREGISTER Transaction</a>
	 */
	int XTYP_UNREGISTER			= 0x00D0 | XCLASS_NOTIFICATION | XTYPF_NOBLOCK;

	/**
	 * The {@code XTYP_WILDCONNECT} transaction allows a client to establish a
	 * conversation on each of the server's service name and topic name pairs
	 * that match the specified service name and topic name. A Dynamic Data
	 * Exchange (DDE) server callback function, <b>DdeCallback</b>, receives
	 * this transaction when a client specifies a {@code NULL} service name, a
	 * {@code NULL} topic name, or both in a call to the <b>DdeConnect</b> or
	 * <b>DdeConnectList</b> function.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648728(VS.85).aspx">XTYP_WILDCONNECT Transaction</a>
	 */
	int XTYP_WILDCONNECT		= 0x00E0 | XCLASS_DATA | XTYPF_NOBLOCK;

	/**
	 * A Dynamic Data Exchange (DDE) client callback function,
	 * <b>DdeCallback</b>, receives the {@code XTYP_XACT_COMPLETE} transaction
	 * when an asynchronous transaction, initiated by a call to the
	 * <b>DdeClientTransaction</b> function, has completed.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648729(VS.85).aspx">XTYP_XACT_COMPLETE Transaction</a>
	 */
	int XTYP_XACT_COMPLETE		= 0x0080 | XCLASS_NOTIFICATION;
	
	/**
	 * Specifies that a transaction should be handled asynchronously.
	 * 
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648773(VS.85).aspx#_win32_Synchronous_and_Asynchronous_Transactions">Synchronous and Asynchronous Transactions</a>
	 */
	int TIMEOUT_ASYNC = 0xFFFFFFFF;
	
}
