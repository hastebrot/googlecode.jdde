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

#ifndef HELLOWORLD_H_
#define HELLOWORLD_H_

#include <jni.h>
#include <windows.h>
#include <winuser.h>
#include <ddeml.h>

HSZ UtilCreateStringHandle(JNIEnv *env, DWORD idInst, jstring jstr);

void UtilFreeStringHandle(DWORD idInst, HSZ hsz);

jobject NewObject(JNIEnv *env, const char *className, const char *signature, ...);

jobject NewInteger(JNIEnv *env, int value);

jobject NewBoolean(JNIEnv *env, int value);

void SetObjectInPointer(JNIEnv *, jobject, jobject);

jbyteArray ExtractData(JNIEnv *, HDDEDATA);

jstring ExtractString(JNIEnv *env, UINT idInst, HSZ hsz);

jobject WrapCallbackParameters(
		JNIEnv *env,
		UINT idInst,
		UINT idThread,
		UINT uType,
		UINT uFmt,
		HCONV hconv,
		HSZ hsz1,
		HSZ hsz2,
		HDDEDATA hdata,
		DWORD dwData1,
		DWORD dwData2);

HDDEDATA CALLBACK DdeCallback(
    UINT uType,     // Transaction type.
    UINT uFmt,      // Clipboard data format.
    HCONV hconv,    // Handle to the conversation.
    HSZ hsz1,       // Handle to a string.
    HSZ hsz2,       // Handle to a string.
    HDDEDATA hdata, // Handle to a global memory object.
    DWORD dwData1,  // Transaction-specific data.
    DWORD dwData2); // Transaction-specific data.

#endif /* HELLOWORLD_H_ */
