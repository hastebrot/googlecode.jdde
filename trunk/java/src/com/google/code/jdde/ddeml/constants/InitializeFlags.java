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
 * Specifies a set of {@code APPCMD_}, {@code CBF_}, and {@code MF_} flags. The
 * {@code APPCMD_} flags provide special instructions to <b>DdeInitialize</b>.
 * The {@code CBF_} flags specify filters that prevent specific types of
 * transactions from reaching the callback function. The {@code MF_} flags
 * specify the types of DDE activity that a DDE monitoring application monitors.
 * Using these flags enhances the performance of a DDE application by
 * eliminating unnecessary calls to the callback function.
 * 
 * @author Vitor Costa
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648757(VS.85).aspx">DdeInitialize Function</a>
 */
public interface InitializeFlags {

	/**
	 * Makes it possible for the application to monitor DDE activity in the
	 * system. This flag is for use by DDE monitoring applications. The
	 * application specifies the types of DDE activity to monitor by combining
	 * one or more monitor flags with the {@code APPCLASS_MONITOR} flag.
	 * <p>
	 * A DDE monitoring application should not attempt to perform DDE operations
	 * (establish conversations, issue transactions, and so on) within the
	 * context of the same application instance.
	 */
	int APPCLASS_MONITOR	= 0x01;

	/**
	 * Registers the application as a standard (nonmonitoring) DDEML
	 * application.
	 */
	int APPCLASS_STANDARD	= 0x00;

	/**
	 * Prevents the application from becoming a server in a DDE conversation.
	 * The application can only be a client. This flag reduces consumption of
	 * resources by the DDEML. It includes the functionality of the {@code
	 * CBF_FAIL_ALLSVRXACTIONS} flag.
	 * <p>
	 * The {@code APPCMD_CLIENTONLY} flag prevents the DDEML from creating key
	 * resources for the server and cannot be changed by a subsequent call to
	 * <b>DdeInitialize</b>.
	 */
	int APPCMD_CLIENTONLY	= 0x10;

	/**
	 * Prevents the DDEML from sending {@code XTYP_CONNECT} and {@code
	 * XTYP_WILDCONNECT} transactions to the application until the application
	 * has created its string handles and registered its service names or has
	 * turned off filtering by a subsequent call to the <b>DdeNameService</b> or
	 * <b>DdeInitialize</b> function. This flag is always in effect when an
	 * application calls <b>DdeInitialize</b> for the first time, regardless of
	 * whether the application specifies the flag. On subsequent calls to
	 * <b>DdeInitialize</b>, not specifying this flag turns off the
	 * application's service-name filters, but specifying it turns on the
	 * application's service name filters.
	 */
	int APPCMD_FILTERINITS	= 0x20;

	/**
	 * Prevents the callback function from receiving server transactions. The
	 * system returns {@code DDE_FNOTPROCESSED} to each client that sends a
	 * transaction to this application. This flag is equivalent to combining all
	 * {@code CBF_FAIL_} flags.
	 * <p>
	 * The {@code CBF_FAIL_ALLSVRXACTIONS} flag causes the DDEML to filter all
	 * server transactions and can be changed by a subsequent call to
	 * <b>DdeInitialize</b>.
	 */
	int CBF_FAIL_ALLSVRXACTIONS		= 0x3f000;

	/**
	 * Prevents the callback function from receiving {@code XTYP_ADVSTART} and
	 * {@code XTYP_ADVSTOP} transactions. The system returns {@code
	 * DDE_FNOTPROCESSED} to each client that sends an {@code XTYP_ADVSTART} or
	 * {@code XTYP_ADVSTOP} transaction to the server.
	 */
	int CBF_FAIL_ADVISES			= 0x4000;

	/**
	 * Prevents the callback function from receiving {@code XTYP_CONNECT} and
	 * {@code XTYP_WILDCONNECT} transactions.
	 */
	int CBF_FAIL_CONNECTIONS		= 0x2000;

	/**
	 * Prevents the callback function from receiving {@code XTYP_EXECUTE}
	 * transactions. The system returns {@code DDE_FNOTPROCESSED} to a client
	 * that sends an {@code XTYP_EXECUTE} transaction to the server.
	 */
	int CBF_FAIL_EXECUTES			= 0x8000;

	/**
	 * Prevents the callback function from receiving {@code XTYP_POKE}
	 * transactions. The system returns {@code DDE_FNOTPROCESSED} to a client
	 * that sends an {@code XTYP_POKE} transaction to the server.
	 */
	int CBF_FAIL_POKES				= 0x10000;

	/**
	 * Prevents the callback function from receiving {@code XTYP_REQUEST}
	 * transactions. The system returns {@code DDE_FNOTPROCESSED} to a client
	 * that sends an {@code XTYP_REQUEST} transaction to the server.
	 */
	int CBF_FAIL_REQUESTS			= 0x20000;

	/**
	 * Prevents the callback function from receiving {@code XTYP_CONNECT}
	 * transactions from the application's own instance. This flag prevents an
	 * application from establishing a DDE conversation with its own instance.
	 * An application should use this flag if it needs to communicate with other
	 * instances of itself but not with itself.
	 */
	int CBF_FAIL_SELFCONNECTIONS	= 0x1000;

	/**
	 * Prevents the callback function from receiving any notifications. This
	 * flag is equivalent to combining all {@code CBF_SKIP_} flags.
	 */
	int CBF_SKIP_ALLNOTIFICATIONS	= 0x3c0000;

	/**
	 * Prevents the callback function from receiving {@code
	 * XTYP_CONNECT_CONFIRM} notifications.
	 */
	int CBF_SKIP_CONNECT_CONFIRMS	= 0x40000;

	/**
	 * Prevents the callback function from receiving
	 * {@code XTYP_DISCONNECT} notifications.
	 */
	int CBF_SKIP_DISCONNECTS		= 0x200000;

	/**
	 * Prevents the callback function from receiving {@code XTYP_REGISTER}
	 * notifications.
	 */
	int CBF_SKIP_REGISTRATIONS		= 0x80000;

	/**
	 * Prevents the callback function from receiving
	 * {@code XTYP_UNREGISTER} notifications.
	 */
	int CBF_SKIP_UNREGISTRATIONS	= 0x100000;

	/**
	 * Notifies the callback function whenever a transaction is sent to any DDE
	 * callback function in the system.
	 */
	int MF_CALLBACKS	= 0x08000000;

	/**
	 * Notifies the callback function whenever a conversation is established or
	 * terminated.
	 */
	int MF_CONV			= 0x40000000;

	/**
	 * Notifies the callback function whenever a DDE error occurs.
	 */
	int MF_ERRORS		= 0x10000000;

	/**
	 * Notifies the callback function whenever a DDE application creates, frees,
	 * or increments the usage count of a string handle or whenever a string
	 * handle is freed as a result of a call to the <b>DdeUninitialize</b>
	 * function.
	 */
	int MF_HSZ_INFO		= 0x01000000;

	/**
	 * Notifies the callback function whenever an advise loop is started or
	 * ended.
	 */
	int MF_LINKS		= 0x20000000;

	/**
	 * Notifies the callback function whenever the system or an application
	 * posts a DDE message.
	 */
	int MF_POSTMSGS		= 0x04000000;

	/**
	 * Notifies the callback function whenever the system or an application
	 * sends a DDE message.
	 */
	int MF_SENDMSGS		= 0x02000000;
	
}
