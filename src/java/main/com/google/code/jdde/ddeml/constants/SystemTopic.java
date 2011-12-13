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
 * The System topic provides a context for information of general interest to
 * any DDE client. It is recommended that server applications support the System
 * topic at all times. The System topic is defined here as the {@code
 * SZDDESYS_TOPIC} constant.
 * <p>
 * To determine which servers are present and the kinds of information they can
 * provide, a client application can request a conversation on the System topic
 * upon starting, setting the device name to {@code NULL}. Such wildcard
 * conversations are costly in terms of system performance, so they should be
 * kept to a minimum.
 * 
 * @author Vitor Costa
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms648768(VS.85).aspx#_win32_System_Topic">System Topic</a>
 */
public interface SystemTopic {

	/**
	 * The System topic.
	 */
	String SZDDESYS_TOPIC = "System";

	/**
	 * A list of the items supported under a non-System topic. (This list can
	 * vary from moment to moment and from topic to topic.)
	 */
	String SZDDE_ITEM_ITEMLIST = "TopicItemList";

	/**
	 * A tab-delimited list of strings representing all clipboard formats
	 * potentially supported by the service application. Strings that represent
	 * predefined clipboard formats are equivalent to the {@code CF_} values
	 * with the "CF_" prefix removed. For example, the {@code CF_TEXT}
	 * format is represented by the string "TEXT". These strings must be
	 * uppercase to further identify them as predefined formats. The list of
	 * formats must appear in the order of most rich in content to least rich in
	 * content.
	 */
	String SZDDESYS_ITEM_FORMATS = "Formats";

	/**
	 * User-readable information of general interest. This item must contain, at
	 * a minimum, information on how to use the server application's DDE
	 * features. This information can include, but is not limited to, how to
	 * specify items within topics, what execute strings the server can perform,
	 * what poke transactions are allowed, and how to find help on other System
	 * topic items.
	 */
	String SZDDESYS_ITEM_HELP = "Help";

	/**
	 * Supporting detail for the most recently used {@code WM_DDE_ACK} message.
	 * This item is useful when more than 8 bits of application-specific return
	 * data are required.
	 */
	String SZDDESYS_ITEM_RTNMSG = "ReturnMessage";

	/**
	 * An indication of the current status of the server. Typically, this item
	 * supports only the {@code CF_TEXT} format and contains the Ready or Busy
	 * string.
	 */
	String SZDDESYS_ITEM_STATUS = "Status";

	/**
	 * A list of the items supported under the System topic by this server.
	 */
	String SZDDESYS_ITEM_SYSITEMS = "SysItems";

	/**
	 * A list of the topics supported by the server at the current time. (This
	 * list can vary from moment to moment.)
	 */
	String SZDDESYS_ITEM_TOPICS = "Topics";

}
