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

/**
 * The message delivered to a subscription
 */
public class Delivery {
	private String attemptId;
	private String messageId;
	private String messageType;
	private String authorization;
	private String payloadContentType;
	private String payload;

	/**
	 * Get delivery attempt identifier for message
	 * 
	 * @return	Delivery attempt identifier
	 */
	public String getAttemptId() {
		return attemptId;
	}

	/**
	 * Sets delivery attempt identifier for message
	 * 
	 * @param attemptId	Delivery attempt identifier
	 */
	public void setAttemptId(String attemptId) {
		this.attemptId = attemptId;
	}

	/**
	 * Get Identifier of delivered message
	 * 
	 * @return	Message Identifier
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * Sets Identifier of delivered message
	 * 
	 * @param messageId	Message identifier
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * Get type of message
	 * 
	 * @return	Message type
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * Sets type of message
	 * 
	 * @param messageType	Message type
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	/**
	 * Get authorization signature of delivery
	 * 
	 * @return	Authorization signature
	 */
	public String getAuthorization() {
		return authorization;
	}

	/**
	 * Sets authorization signature of delivery
	 * 
	 * @param authorization	Authorization signature
	 */
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	/**
	 * Get content type of payload
	 * 
	 * @return	Payload content type
	 */
	public String getPayloadContentType() {
		return payloadContentType;
	}

	/**
	 * Set content type of payload
	 * 
	 * @param payloadContentType	Payload content type
	 */
	public void setPayloadContentType(String payloadContentType) {
		this.payloadContentType = payloadContentType;
	}

	/**
	 * Get payload of message
	 * 
	 * @return	Message payload
	 */
	public String getPayload() {
		return payload;
	}

	/**
	 * Sets payload of message
	 * 
	 * @param payload	Message payload
	 */
	public void setPayload(String payload) {
		this.payload = payload;
	}
}
