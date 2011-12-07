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

package com.google.code.jdde.server.event;

/**
 * 
 * @author Vitor Costa
 */
import com.google.code.jdde.ddeml.constants.FlagCallbackResult;
import com.google.code.jdde.event.AdviseRequestEvent;
import com.google.code.jdde.event.AdviseStartEvent;
import com.google.code.jdde.event.AdviseStopEvent;
import com.google.code.jdde.event.ExecuteEvent;
import com.google.code.jdde.event.PokeEvent;
import com.google.code.jdde.event.RequestEvent;

public class TransactionAdapter implements TransactionListener {

	@Override
	public byte[] onAdviseRequest(AdviseRequestEvent e) {
		return null;
	}

	@Override
	public boolean onAdviseStart(AdviseStartEvent e) {
		return false;
	}

	@Override
	public void onAdviseStop(AdviseStopEvent e) {
		
	}

	@Override
	public FlagCallbackResult onExecute(ExecuteEvent e) {
		return FlagCallbackResult.DDE_FNOTPROCESSED;
	}

	@Override
	public FlagCallbackResult onPoke(PokeEvent e) {
		return FlagCallbackResult.DDE_FNOTPROCESSED;
	}

	@Override
	public byte[] onRequest(RequestEvent e) {
		return null;
	}

}
