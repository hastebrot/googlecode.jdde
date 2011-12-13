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

#include <windows.h>
#include <winuser.h>

#include "WinAPI.h"


JNIEXPORT jint JNICALL Java_com_google_code_jdde_ddeml_WinAPI_GetCurrentThreadId
  (JNIEnv *env, jclass cls)
{
	return GetCurrentThreadId();
}

JNIEXPORT jint JNICALL Java_com_google_code_jdde_ddeml_WinAPI_GetLastError
  (JNIEnv *env, jclass cls)
{
	return GetLastError();
}

JNIEXPORT jboolean JNICALL Java_com_google_code_jdde_ddeml_WinAPI_KillTimer
  (JNIEnv *env, jclass cls, jint uIDEvent)
{
	return KillTimer(0, uIDEvent);
}

JNIEXPORT jboolean JNICALL Java_com_google_code_jdde_ddeml_WinAPI_PostThreadMessage
  (JNIEnv *env, jclass cls, jint idThread, jint Msg, jint wParam, jint lParam)
{
	return PostThreadMessage(idThread, Msg, wParam, lParam);
}

JNIEXPORT jint JNICALL Java_com_google_code_jdde_ddeml_WinAPI_RegisterClipboardFormat
  (JNIEnv *env, jclass cls, jstring jlpszFormat)
{
	LPCTSTR lpszFormat = env->GetStringUTFChars(jlpszFormat, 0);
	UINT result = RegisterClipboardFormat(lpszFormat);
	env->ReleaseStringUTFChars(jlpszFormat, lpszFormat);
	return result;
}

JNIEXPORT jint JNICALL Java_com_google_code_jdde_ddeml_WinAPI_RegisterWindowMessage
  (JNIEnv *env, jclass cls, jstring jlpString)
{
	LPCTSTR lpString = env->GetStringUTFChars(jlpString, 0);
	UINT result = RegisterWindowMessage(lpString);
	env->ReleaseStringUTFChars(jlpString, lpString);
	return result;
}

JNIEXPORT jint JNICALL Java_com_google_code_jdde_ddeml_WinAPI_SetTimer
  (JNIEnv *env, jclass cls, jint uElapse)
{
	return SetTimer(0, 0, uElapse, 0);
}

JNIEXPORT void JNICALL Java_com_google_code_jdde_ddeml_WinAPI_WaitOnMessageLoop
  (JNIEnv *env, jclass cls, const jint Msg)
{
	MSG msg;	// Message loop:
	while (GetMessage(&msg, NULL, 0, 0) > 0) {
		TranslateMessage(&msg);
		DispatchMessage(&msg);

		if (msg.message == (UINT) Msg || msg.message == WM_TIMER) {
			return;
		}

		if (msg.message == 0x7F7F) {
			return;
		}
	}
}
