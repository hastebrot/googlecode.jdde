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

package com.google.code.jdde.event;

import com.google.code.jdde.ddeml.CallbackParameters;
import com.google.code.jdde.ddeml.ConvContext;
import com.google.code.jdde.server.DdeServer;

/**
 * 
 * @author Vitor Costa
 */
public class ConnectEvent extends DdeEvent<DdeServer> {

	private final String topic;
	private final String service;

	private final ConvContext convContext;
	private final boolean sameInstance;
	
	public ConnectEvent(DdeServer server, CallbackParameters parameters) {
		super(server);

		topic = parameters.getHsz1();
		service = parameters.getHsz2();
		
		convContext = (ConvContext) parameters.getDwData1();
		sameInstance = (Boolean) parameters.getDwData2();
	}

	public String getTopic() {
		return topic;
	}

	public String getService() {
		return service;
	}

	public ConvContext getConvContext() {
		return convContext;
	}

	public boolean isSameInstance() {
		return sameInstance;
	}
	
}
