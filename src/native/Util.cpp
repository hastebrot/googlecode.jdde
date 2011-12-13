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

#include "Util.h"

#define iCodePage CP_WINANSI

HSZ UtilCreateStringHandle(JNIEnv *env, DWORD idInst, jstring jstr)
{
	HSZ hsz = NULL;
	if (jstr != NULL) {
		const char *str = env->GetStringUTFChars(jstr, 0);
		hsz = DdeCreateStringHandle(idInst, str, iCodePage);
		env->ReleaseStringUTFChars(jstr, str);
	}
	return hsz;
}

void UtilFreeStringHandle(DWORD idInst, HSZ hsz)
{
	if (hsz != NULL) {
		DdeFreeStringHandle(idInst, hsz);
	}
}

jobject NewObject(JNIEnv *env, const char *className, const char *signature, ...)
{
	va_list args;

	jclass clazz = env->FindClass(className);
	jmethodID constructor = env->GetMethodID(clazz, "<init>", signature);

	jobject result;
	va_start(args, signature);
	result = env->NewObjectV(clazz, constructor, args);
	va_end(args);

	env->DeleteLocalRef(clazz);
	return result;
}

jobject NewInteger(JNIEnv *env, int value)
{
	return NewObject(env, "Ljava/lang/Integer;", "(I)V", value);
}

jobject NewBoolean(JNIEnv *env, int value)
{
	return NewObject(env, "Ljava/lang/Boolean;", "(Z)V", value);
}

void SetObjectInPointer(JNIEnv *env, jobject pointer, jobject value)
{
	jclass clazz = env->GetObjectClass(pointer);
	jfieldID fieldID = env->GetFieldID(clazz, "value", "Ljava/lang/Object;");
	env->SetObjectField(pointer, fieldID, value);

	env->DeleteLocalRef(clazz);
}

jbyteArray ExtractData(JNIEnv *env, HDDEDATA data)
{
	DWORD cb = DdeGetData(data, (LPBYTE) NULL, 0, 0);
	LPBYTE pDst = (LPBYTE) malloc(cb);

	DdeGetData(data, pDst, cb, 0);

	jbyteArray jb = env->NewByteArray(cb);
	env->SetByteArrayRegion(jb, 0, cb, (jbyte *) pDst);

	free(pDst);

	return jb;
}

jstring ExtractString(JNIEnv *env, UINT idInst, HSZ hsz)
{
	// DdeQueryString does not include the terminating null character
	DWORD cb = DdeQueryString(idInst, hsz, NULL, 0, CP_WINANSI) + 1;
	LPTSTR psz = (LPTSTR) malloc(cb);

	DdeQueryString(idInst, hsz, psz, cb, CP_WINANSI);

	jstring js = env->NewStringUTF(psz);

	free(psz);

	return js;
}

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
		DWORD dwData2)
{

	jstring jhsz1 = NULL;
	jstring jhsz2 = NULL;
	jbyteArray jhdata = NULL;
	jobject jdwData1 = NULL;
	jobject jdwData2 = NULL;

	if (hsz1 != NULL) {
		jhsz1 = ExtractString(env, idInst, hsz1);
	}

	if (hsz2 != NULL) {
		jhsz2 = ExtractString(env, idInst, hsz2);
	}

	if (hdata != NULL) {
		jhdata = ExtractData(env, hdata);
	}

	switch (uType) {
	case XTYP_ADVREQ:
	case XTYP_ERROR:
		jdwData1 = NewInteger(env, LOWORD(dwData1));
		// dwData2 is not used
		break;
	case XTYP_CONNECT:
	case XTYP_WILDCONNECT:
		//jdwData1 = CONVCONTEXT
		jdwData2 = NewBoolean(env, dwData2);
		break;
	case XTYP_CONNECT_CONFIRM:
	case XTYP_DISCONNECT:
		// dwData1 is not used
		jdwData2 = NewBoolean(env, dwData2);
		break;
	case XTYP_MONITOR:
		// dwData1 is not used
		jdwData2 = NewInteger(env, dwData2);
		break;
	case XTYP_XACT_COMPLETE:
		jdwData1 = NewInteger(env, dwData1);
		jdwData2 = NewInteger(env, LOWORD(dwData2));
		break;
	}

	// the following transactions don't use dwData1 nor dwData2:
	// XTYP_ADVDATA
	// XTYP_ADVSTART
	// XTYP_ADVSTOP
	// XTYP_EXECUTE
	// XTYP_POKE
	// XTYP_REGISTER
	// XTYP_REQUEST
	// XTYP_UNREGISTER

	return NewObject(env,
			"Lcom/google/code/jdde/ddeml/CallbackParameters;",
			"(IIIILjava/lang/String;Ljava/lang/String;[BLjava/lang/Object;Ljava/lang/Object;)V",
			idThread, uType, uFmt, hconv, jhsz1, jhsz2, jhdata, jdwData1, jdwData2);
}
