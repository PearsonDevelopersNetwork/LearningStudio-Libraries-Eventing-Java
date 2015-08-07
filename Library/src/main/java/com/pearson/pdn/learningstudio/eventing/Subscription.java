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

import java.util.Date;

/**
 * Subscription definition
 */
public class Subscription extends MetaMessage {
	
	private String callbackUrl;
	// these are not part of the subscription creation
	// they are populated afterwards though
	private String id;
	private String principalId;
	private Date createDate;

	/**
	 * Get identifier of subscription
	 * 
	 * @return	Subscription identifier
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set identifier of subscription
	 * 
	 * @param id	Subscription identifier
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get identifier of principal for subscription
	 * 
	 * @return Principal identifier
	 */
	public String getPrincipalId() {
		return principalId;
	}

	/** 
	 * Set identifier of principal for subscription
	 * 
	 * @param principalId	Principal identifier
	 */
	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	/**
	 * Get creation date of subscription
	 * 
	 * @return	Subscription creation date
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * Set creation date of subscription
	 * 
	 * @param createDate	Subscription creation date
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * Get URL for message delivery
	 * 
	 * @return	URL for message delivery
	 */
	public String getCallbackUrl() {
		return callbackUrl;
	}

	/**
	 * Set URL for message delivery
	 * 
	 * @param callbackUrl	URL for message delivery
	 */
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

}
