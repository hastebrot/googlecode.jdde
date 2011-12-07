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
public class WinAPI {

	/* First thing---register the natives */
	static {
		NativeLoader.load();
	}

	/**
	 * Retrieves the thread identifier of the calling thread.
	 * <p>
	 * <b>Remarks</b>
	 * <p>
	 * Until the thread terminates, the thread identifier uniquely identifies
	 * the thread throughout the system.
	 * 
	 * @return The return value is the thread identifier of the calling thread.
	 */
	public static native int GetCurrentThreadId();

	/**
	 * Retrieves the calling thread's last-error code value. The last-error code
	 * is maintained on a per-thread basis. Multiple threads do not overwrite
	 * each other's last-error code.
	 * <p>
	 * The Return Value section of the documentation for each function that sets
	 * the last-error code notes the conditions under which the function sets
	 * the last-error code. Most functions that set the thread's last-error code
	 * set it when they fail. However, some functions also set the last-error
	 * code when they succeed. If the function is not documented to set the
	 * last-error code, the value returned by this function is simply the most
	 * recent last-error code to have been set; some functions set the
	 * last-error code to 0 on success and others do not.
	 * 
	 * @return The return value is the calling thread's last-error code.
	 */
	public static native int GetLastError();

	/**
	 * This function destroys the specified timer. The system searches the
	 * message queue for any pending {@code WM_TIMER} messages associated with
	 * the timer and removes them.
	 * <p>
	 * <b>Remarks</b>
	 * <p>
	 * If the <i>uElapse</i> parameter of the <b>SetTimer</b> function is set to
	 * {@code INFINITE}, <b>KillTimer</b> fails to destroy the timer.
	 *
	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms644903(VS.85).aspx">KillTimer Function</a>
	 * 
	 * @param uIDEvent
	 *            [in] Specifies the identifier of the timer to be destroyed.
	 *            this parameter must be the timer identifier returned by
	 *            <b>SetTimer</b>.
	 * @return Nonzero indicates success. Zero indicates failure. To get
	 *         extended error information, call <b>GetLastError</b>.
	 *         
	 */
	public static native boolean KillTimer(int uIDEvent);

	/**
	 * The {@code PostThreadMessage} function posts a message to the message
	 * queue of the specified thread. It returns without waiting for the thread
	 * to process the message.
	 * <p>
	 * The function fails if the specified thread does not have a message queue.
	 * The system creates a thread's message queue when the thread makes its
	 * first call to one of the User or GDI functions.
	 * 
	 * @param idThread
	 *            [in] Identifier of the thread to which the message is to be
	 *            posted.
	 * @param Msg
	 *            [in] Specifies the type of message to be posted.
	 * @param wParam
	 *            [in] Specifies additional message-specific information.
	 * @param lParam
	 *            [in] Specifies additional message-specific information.
	 * @return If the function succeeds, the return value is nonzero. If the
	 *         function fails, the return value is zero. To get extended error
	 *         information, call {@code GetLastError}. {@code GetLastError}
	 *         returns {@code ERROR_INVALID_THREAD_ID} if <i>idThread</i> is not
	 *         a valid thread identifier, or if the thread specified by
	 *         <i>idThread</i> does not have a message queue.
	 */
	public static native boolean PostThreadMessage(int idThread, int Msg,
			int wParam, int lParam);

	/**
	 * The {@code RegisterClipboardFormat} function registers a new clipboard
	 * format. This format can then be used as a valid clipboard format.
	 * <p>
	 * <b>Remarks</b>
	 * <p>
	 * If a registered format with the specified name already exists, a new
	 * format is not registered and the return value identifies the existing
	 * format. This enables more than one application to copy and paste data
	 * using the same registered clipboard format. Note that the format name
	 * comparison is case-insensitive.
	 * <p>
	 * Registered clipboard formats are identified by values in the range
	 * {@code 0xC000} through {@code 0xFFFF}.
	 * <p>
	 * When registered clipboard formats are placed on or retrieved from the
	 * clipboard, they must be in the form of an <b>HGLOBAL</b> value.
	 * 
	 * @param lpszFormat
	 *            [in] Pointer to a null-terminated string that names the new
	 *            format.
	 * @return If the function succeeds, the return value identifies the
	 *         registered clipboard format. If the function fails, the return
	 *         value is zero. To get extended error information, call {@code
	 *         GetLastError}.
	 */
	public static native int RegisterClipboardFormat(String lpszFormat);

	/**
	 * The {@code RegisterWindowMessage} function defines a new window message
	 * that is guaranteed to be unique throughout the system. The message value
	 * can be used when sending or posting messages.
	 * <p>
	 * <b>Remarks</b>
	 * <p>
	 * The {@code RegisterWindowMessage} function is typically used to register
	 * messages for communicating between two cooperating applications.
	 * <p>
	 * If two different applications register the same message string, the
	 * applications return the same message value. The message remains
	 * registered until the session ends.
	 * <p>
	 * Only use {@code RegisterWindowMessage} when more than one application
	 * must process the same message. For sending private messages within a
	 * window class, an application can use any integer in the range {@code
	 * WM_USER} through {@code 0x7FFF}. (Messages in this range are private to a
	 * window class, not to an application. For example, predefined control
	 * classes such as {@code BUTTON}, {@code EDIT}, {@code LISTBOX}, and
	 * {@code COMBOBOX} may use values in this range.)
	 * 
	 * @param lpString
	 *            [in] Pointer to a null-terminated string that specifies the
	 *            message to be registered.
	 * @return If the message is successfully registered, the return value is a
	 *         message identifier in the range {@code 0xC000} through {@code
	 *         0xFFFF}. If the function fails, the return value is zero. To get
	 *         extended error information, call {@code GetLastError}.
	 */
	public static native int RegisterWindowMessage(String lpString);

	/**
	 * The <b>SetTimer</b> function creates a timer with the specified time-out
	 * value.
	 * 
 	 * @see <a href="http://msdn.microsoft.com/en-us/library/ms644906(VS.85).aspx">SetTimer Function</a>
	 * 
	 * @param uElapse
	 *            [in] Specifies the time-out value, in milliseconds.
	 * @return If the function succeeds, the return value is an integer
	 *         identifying the new timer. An application can pass this value to
	 *         the <b>KillTimer</b> function to destroy the timer. If the
	 *         function fails to create a timer, the return value is zero. To
	 *         get extended error information, call <b>GetLastError</b>.
	 */
	public static native int SetTimer(int uElapse);

	/**
	 * TODO Escrever alguma coisa aqui.
	 * 
	 * <pre>
	 * 	MSG msg;
	 * 
	 * 	while (GetMessage(&amp;msg, NULL, 0, 0)) {
	 * 		TranslateMessage(&amp;msg);
	 * 		DispatchMessage(&amp;msg);
	 * 
	 * 		if (msg.message == Msg) {
	 * 			return;
	 * 		}
	 * 	}
	 * </pre>
	 * 
	 * @param Msg
	 *            [in] Specifies the type of message to wait for.
	 */
	public static native void WaitOnMessageLoop(int Msg);

}
