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

import com.google.code.jdde.ddeml.constants.FlagCallbackResult;
import com.google.code.jdde.misc.SupportedServiceTopic;

/**
 * 
 * @author Vitor Costa
 */
public interface DdeCallback {

	boolean DdeBooleanCallback(CallbackParameters parameters);
	
	byte[] DdeDataCallback(CallbackParameters parameters);
	
	FlagCallbackResult DdeFlagCallback(CallbackParameters parameters);
	
	void DdeNotificationCallback(CallbackParameters parameters);
	
	/* specific callbacks **/
	
	SupportedServiceTopic[] DdeWildConnectCallback(CallbackParameters parameters);
	
}
