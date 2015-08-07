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
 * Configuration for the Eventing Client
 */
public class Configuration {

	private String eventingServer;
	private String principalId;
	private String principalSecret;
	
	/**
	 * Get the URL of the Eventing Server
	 * 
	 * @return	URL of the Eventing Server
	 */
	public String getEventingServer() {
		return eventingServer;
	}
	
	/**
	 * Sets the URL of the Eventing Server
	 * 
	 * @param eventingServer	URL of the Eventing Server
	 */
	public void setEventingServer(String eventingServer) {
		this.eventingServer = eventingServer;
	}
	
	/**
	 * Get the id portion of the principal
	 * 
	 * @return	Principal identifier
	 */
	public String getPrincipalId() {
		return principalId;
	}
	
	/**
	 * Sets the id portion of the principal
	 * 
	 * @param principalId	Principal identifier
	 */
	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}
	
	/**
	 * Get the secret portion of the principal
	 * 
	 * @return	Principal secret
	 */
	public String getPrincipalSecret() {
		return principalSecret;
	}
	
	/**
	 * Sets the secret portion of the principal
	 *  
	 * @param principalSecret	Principal secret
	 */
	public void setPrincipalSecret(String principalSecret) {
		this.principalSecret = principalSecret;
	}
	
}
