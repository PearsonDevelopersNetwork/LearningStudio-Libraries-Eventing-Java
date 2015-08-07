/*
 * LearningStudio Eventing Library 
 * This library makes it easier to use the LearningStudio Eventing API.
 * Full Documentation is provided with the library. 
 * 
 * Need Help or Have Questions? 
 * Please use the PDN Developer Community at https://community.pdn.pearson.com
 *
 * @category   LearningStudio Course APIs
 * @author     Wes Williams <wes.williams@pearson.com>
 * @author     Pearson Developer Services Team <apisupport@pearson.com>
 * @copyright  2015 Pearson Education, Inc.
 * @license    http://www.apache.org/licenses/LICENSE-2.0  Apache 2.0
 * @version    1.0
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pearson.pdn.learningstudio.eventing;

import java.util.Map;

/**
 * Meta descriptors for a published message
 */
public class MetaMessage {
	private String client;
	private String clientString;
	private String messageType;
	private String system;
	private String subSystem;
	private Map<String, String> tags;

	/**
	 * Get client identifier
	 * 
	 * @return Client identifier
	 */
	public String getClient() {
		return client;
	}

	/**
	 * Set client identifier
	 * 
	 * @param client	Client identifier
	 */
	public void setClient(String client) {
		this.client = client;
	}

	/**
	 * Get string identifier for client
	 * 
	 * @return	Client string
	 */
	public String getClientString() {
		return clientString;
	}

	/**
	 * Set string identifier for client
	 * 
	 * @param clientString	Client string
	 */
	public void setClientString(String clientString) {
		this.clientString = clientString;
	}

	/**
	 * Get type of message
	 * 
	 * @return Message type
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * Set type of message
	 * 
	 * @param messageType	Message type
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	/**
	 * Get system that published message
	 * 
	 * @return	System of published message
	 */
	public String getSystem() {
		return system;
	}

	/**
	 * Set system that published message
	 * 
	 * @param system	System of published message
	 */
	public void setSystem(String system) {
		this.system = system;
	}

	/**
	 * Get subsystem of published message
	 * 
	 * @return	Subsystem of published message
	 */
	public String getSubSystem() {
		return subSystem;
	}

	/**
	 * Set subsystem of published message
	 * 
	 * @param subSystem	Subsystem of published message
	 */
	public void setSubSystem(String subSystem) {
		this.subSystem = subSystem;
	}

	/**
	 * Get extra tags on message
	 * 
	 * @return	Key value pairs of message tags
	 */
	public Map<String, String> getTags() {
		return tags;
	}

	/**
	 * Set extra tags on message
	 * 
	 * @param tags	Key value paris of message tags
	 */
	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}

}
