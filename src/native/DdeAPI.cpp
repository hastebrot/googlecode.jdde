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
#include <ddeml.h>

#include "DdeAPI.h"
#include "Util.h"

JNIEXPORT jboolean JNICALL Java_com_google_code_jdde_ddeml_DdeAPI_AbandonTransaction
  (JNIEnv *env, jclass cls, jint idInst, jint hConv, jint idTransaction)
{
	return DdeAbandonTransaction(idInst, (HCONV) hConv, idTransaction);
}

JNIEXPORT jbyteArray JNICALL Java_com_google_code_jdde_ddeml_DdeAPI_ClientTransaction
  (JNIEnv *env, jclass cls, jint idInst, jbyteArray jpData, jint hConv, jstring jhszItem,
		  jint wFmt, jint wType, jint dwTimeout, jobject $dwResult)
{
	HSZ hszItem = UtilCreateStringHandle(env, idInst, jhszItem);
	HDDEDATA pData = NULL;
	DWORD dwResult = 0;

	if (jpData != NULL) {
		jsize cb = env->GetArrayLength(jpData);
		jbyte *pSrc = env->GetByteArrayElements(jpData, 0);
		pData = DdeCreateDataHandle(idInst, (LPBYTE) pSrc, cb, 0, hszItem, wFmt, 0);
		env->ReleaseByteArrayElements(jpData, pSrc, 0);
	}

	HDDEDATA hddeData = DdeClientTransaction(
			(LPBYTE) pData,
			0xFFFFFFFF,
			(HCONV) hConv,
			hszItem,
			wFmt,
			wType,
			dwTimeout,
			&dwResult
	);

	if (pData != NULL) {
		DdeFreeDataHandle(pData);
	}

	UtilFreeStringHandle(idInst, hszItem);

	if ($dwResult != NULL) {
		SetObjectInPointer(env, $dwResult, NewInteger(env, dwResult));
	}

	if (hddeData == NULL) {
		return NULL;
	}
	else if (wType == XTYP_REQUEST) {
		jbyteArray result = ExtractData(env, hddeData);
		DdeFreeDataHandle(hddeData);
		return result;
	}

	return env->NewByteArray(0);
}

JNIEXPORT jint JNICALL Java_com_google_code_jdde_ddeml_DdeAPI_Connect
  (JNIEnv *env, jclass cls, jint idInst, jstring jhszService, jstring jhszTopic, jobject pCC)
{
	HSZ hszService = UtilCreateStringHandle(env, idInst, jhszService);
	HSZ hszTopic = UtilCreateStringHandle(env, idInst, jhszTopic);

	HCONV hConv = DdeConnect(
			idInst,					// instance identifier
			hszService,				// service name string handle
			hszTopic,				// topic string handle
			(PCONVCONTEXT) NULL);	// use default context

	UtilFreeStringHandle(idInst, hszService);
	UtilFreeStringHandle(idInst, hszTopic);

	#ifdef __i386__
		return (UINT) hConv;
	#endif
	#ifdef __x86_64__
		return (UINT64) hConv;
	#endif
}

JNIEXPORT jboolean JNICALL Java_com_google_code_jdde_ddeml_DdeAPI_Disconnect
  (JNIEnv *env, jclass cls, jint hConv)
{
	return DdeDisconnect((HCONV) hConv);
}

JNIEXPORT jint JNICALL Java_com_google_code_jdde_ddeml_DdeAPI_GetLastError
  (JNIEnv *env, jclass cls, jint idInst)
{
	return DdeGetLastError(idInst);
}

JNIEXPORT jint JNICALL Java_com_google_code_jdde_ddeml_DdeAPI_Initialize
  (JNIEnv *env, jclass cls, jobject $idInst, jint afCmd)
{
	DWORD idInst = 0;

	UINT initError = DdeInitialize(
			&idInst, // receives instance identifier
			(PFNCALLBACK) DdeCallback, // pointer to callback function
			afCmd, 0);

	SetObjectInPointer(env, $idInst, NewInteger(env, idInst));

	return initError;
}

JNIEXPORT jboolean JNICALL Java_com_google_code_jdde_ddeml_DdeAPI_NameService
  (JNIEnv *env, jclass cls, jint idInst, jstring jhsz1, jint afCmd)
{
	HSZ hsz1 = UtilCreateStringHandle(env, idInst, jhsz1);

	HDDEDATA data = DdeNameService(idInst, hsz1, 0L, afCmd);

	UtilFreeStringHandle(idInst, hsz1);

	return data != NULL;
}

JNIEXPORT jboolean JNICALL Java_com_google_code_jdde_ddeml_DdeAPI_PostAdvise
  (JNIEnv *env, jclass cls, jint idInst, jstring jhszTopic, jstring jhszItem)
{
	HSZ hszTopic = UtilCreateStringHandle(env, idInst, jhszTopic);
	HSZ hszItem = UtilCreateStringHandle(env, idInst, jhszItem);

	BOOL result = DdePostAdvise(idInst, hszTopic, hszItem);

	UtilFreeStringHandle(idInst, hszTopic);
	UtilFreeStringHandle(idInst, hszItem);

	return result;
}

JNIEXPORT jboolean JNICALL Java_com_google_code_jdde_ddeml_DdeAPI_Uninitialize
  (JNIEnv *env, jclass cls, jint idInst)
{
	return DdeUninitialize(idInst);
}

JavaVM *jvm;
jclass CallbackManager = NULL;
jmethodID mGetIdInst;
jmethodID mBooleanCallback;
jmethodID mDataCallback;
jmethodID mFlagCallback;
jmethodID mNotificationCallback;
jmethodID mWildConnectCallback;

jint JNI_OnLoad(JavaVM *vm, void *reserved)
{
	jvm = vm;
	return JNI_VERSION_1_4;
}

HDDEDATA CALLBACK DdeCallback(
    UINT uType,     // Transaction type.
    UINT uFmt,      // Clipboard data format.
    HCONV hconv,    // Handle to the conversation.
    HSZ hsz1,       // Handle to a string.
    HSZ hsz2,       // Handle to a string.
    HDDEDATA hdata, // Handle to a global memory object.
    DWORD dwData1,  // Transaction-specific data.
    DWORD dwData2)  // Transaction-specific data.

{
	JNIEnv *env;
	jvm->GetEnv((void **) &env, JNI_VERSION_1_4);

	env->PushLocalFrame(16);

	HDDEDATA result = NULL;

	UINT idThread = GetCurrentThreadId();

	if (CallbackManager == NULL) {
		jclass clazz = env->FindClass("com/google/code/jdde/ddeml/CallbackManager");
		CallbackManager = (jclass) env->NewGlobalRef(clazz);

		mGetIdInst = env->GetStaticMethodID(CallbackManager, "getIdInst", "(I)I");
		mBooleanCallback = env->GetStaticMethodID(clazz, "DdeBooleanCallback", "(Lcom/google/code/jdde/ddeml/CallbackParameters;)Z");
		mDataCallback = env->GetStaticMethodID(clazz, "DdeDataCallback", "(Lcom/google/code/jdde/ddeml/CallbackParameters;)[B");
		mFlagCallback = env->GetStaticMethodID(clazz, "DdeFlagCallback", "(Lcom/google/code/jdde/ddeml/CallbackParameters;)I");
		mNotificationCallback = env->GetStaticMethodID(clazz, "DdeNotificationCallback", "(Lcom/google/code/jdde/ddeml/CallbackParameters;)V");
		mWildConnectCallback = env->GetStaticMethodID(clazz, "DdeWildConnectCallback", "(Lcom/google/code/jdde/ddeml/CallbackParameters;)[Ljava/lang/String;");
	}

	UINT idInst = env->CallStaticIntMethod(CallbackManager, mGetIdInst, idThread);

	jobject jobj;
	jbyteArray bArray;
	jobjectArray sArray;
	jobject parameter = WrapCallbackParameters(env, idInst, idThread, uType,
			uFmt, hconv, hsz1, hsz2, hdata, dwData1, dwData2);

	switch (uType) {
	case XTYP_ADVSTART:
	case XTYP_CONNECT:
		result = (HDDEDATA) env->CallStaticBooleanMethod(CallbackManager, mBooleanCallback, parameter);
		break;
	case XTYP_ADVREQ:
	case XTYP_REQUEST:
		jobj = env->CallStaticObjectMethod(CallbackManager, mDataCallback, parameter);
		bArray = (jbyteArray) jobj;

		if (bArray != NULL) {
			jsize cb = env->GetArrayLength(bArray);
			jbyte *pSrc = env->GetByteArrayElements(bArray, 0);
			result = DdeCreateDataHandle(idInst, (LPBYTE) pSrc, cb, 0, hsz2, uFmt, 0);
			env->ReleaseByteArrayElements(bArray, pSrc, 0);
		}
		break;
	case XTYP_WILDCONNECT:
		jobj = env->CallStaticObjectMethod(CallbackManager, mWildConnectCallback, parameter);
		sArray = (jobjectArray) jobj;

		if (sArray != NULL) {
			jsize cb = env->GetArrayLength(sArray);
			jsize length = cb / 2;
			HSZPAIR* hszPair = (HSZPAIR*) malloc((length + 1) * sizeof(HSZPAIR));

			int j = 0;
			for (int i = 0; i < cb; ) {
				jstring str1 = (jstring) env->GetObjectArrayElement(sArray, i++);
				HSZ hsz1 = UtilCreateStringHandle(env, idInst, str1);

				jstring str2 = (jstring) env->GetObjectArrayElement(sArray, i++);
				HSZ hsz2 = UtilCreateStringHandle(env, idInst, str2);

				hszPair[j].hszSvc = hsz1;
				hszPair[j++].hszTopic = hsz2;
			}
			hszPair[j].hszSvc = NULL;
			hszPair[j++].hszTopic = NULL;

			result = DdeCreateDataHandle(idInst, (LPBYTE)&hszPair[0], sizeof(HSZPAIR) * j, 0L, 0, uFmt, 0);
		}
		break;
	case XTYP_ADVDATA:
	case XTYP_EXECUTE:
	case XTYP_POKE:
		result = (HDDEDATA) env->CallStaticIntMethod(CallbackManager, mFlagCallback, parameter);
		break;
	case XTYP_ADVSTOP:
	case XTYP_CONNECT_CONFIRM:
	case XTYP_DISCONNECT:
	case XTYP_ERROR:
	case XTYP_MONITOR:
	case XTYP_REGISTER:
	case XTYP_XACT_COMPLETE:
	case XTYP_UNREGISTER:
		env->CallStaticVoidMethod(CallbackManager, mNotificationCallback, parameter);
		break;
	}

	if (env->ExceptionOccurred()) {
		PostThreadMessage(GetCurrentThreadId(), 0x7F7F, 0, 0);
	}

	env->PopLocalFrame(NULL);
	return result;
}
