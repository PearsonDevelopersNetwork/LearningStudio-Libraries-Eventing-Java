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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Client to handle all LearningStudio Eventing interactions
 */
public class EventingClient {
	private static final Logger logger = Logger.getLogger(EventingClient.class);
	private static final String DEFAULT_DELIMITER = "|";
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	static {
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	private Configuration config;
	private HttpClient httpClient;
	
	/**
	 * Constructs client for principal
	 * 
	 * @param config	Eventing credentials and server configuration
	 * @throws IllegalArgumentException	Thrown when parameters are invalid
	 */
	public EventingClient(Configuration config) throws IllegalArgumentException {
		if(config==null) {
			throw new IllegalArgumentException("Configuration object is required by client!");
		}
		if(config.getPrincipalId()==null || config.getPrincipalId().length()==0) {
			throw new IllegalArgumentException("Principal id is required by client!");
		}
		if(config.getPrincipalSecret()==null || config.getPrincipalSecret().length()==0) {
			throw new IllegalArgumentException("Principal secret is required by client!");
		}
		if(config.getEventingServer()==null || config.getEventingServer().length()==0) {
			throw new IllegalArgumentException("Eventing server URL is required by client!");
		}
		this.config = config;
		
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setDefaultMaxPerRoute(50);
		cm.setMaxTotal(100);
		httpClient = HttpClientBuilder.create().setConnectionManager(cm).build();
	}
	
	/**
	 * Gets subscriptions for principal
	 * 
	 * @return	List of subscriptions
	 * @throws EventingException	Thrown when operation fails
	 */
	public List<Subscription> getSubscriptions() throws EventingException {
		try {
			// use the principal id as the payload for auth token
			String authData = config.getPrincipalId();
	
			String responseBody = getSubscriptionResponse("/v1/subscriptions/principal/" + config.getPrincipalId(), authData);
					
			if(logger.isDebugEnabled()) {
				logger.debug("FOUND SUBS: " +responseBody);
			}
			
			// {"subscriptions":[
			//     {"subscription":{"id":"","principal_id":"","callback_url":"",
			//      "wsdl_uri":"","queue_name":"","date_created":"","date_cancelled":"",
			//      "tags":[{"tag":{"id":"","type":"","value":""}}]
			//     }
			//   ]
			// }
	
			return parseSubscriptions(responseBody);
		}
		catch (Exception e) {
			throw new EventingException(e);
		}
	}
	
	/**
	 * Get subscription by id
	 * 
	 * @param subscriptionId	ID of subscription
	 * @return	Subscription object
	 * @throws IllegalArgumentException	Thrown when parameters are invalid
	 * @throws EventingException	Thrown when operation fails
	 */
	public Subscription getSubscription(String subscriptionId) throws IllegalArgumentException, EventingException {
		if(subscriptionId==null || subscriptionId.length()==0) {
			throw new IllegalArgumentException("Subscription id is required to retrieve subscription!");
		}
		
		try {
			// use the principal id as the payload for auth token
			String authData = subscriptionId;
	
			String responseBody = getSubscriptionResponse("/v1/subscription/"+subscriptionId, authData);
					
			if(logger.isDebugEnabled()) {
				logger.debug("FOUND SUBS: " +responseBody);
			}
			
			// {"subscription":{"id":"","principal_id":"","callback_url":"",
			//   "wsdl_uri":"","queue_name":"","date_created":"","date_cancelled":"",
			//   "tags":[{"tag":{"id":"","type":"","value":""}}]
			// }
	
			return parseSubscription(responseBody);
		}
		catch (Exception e) {
			throw new EventingException(e);
		}
	}
	
	/**
	 * Handles shared code for GET operations on subscriptions
	 * 
	 * @param route	Route to call
	 * @param authData	Payload to sign
	 * @return	Response body from the operation
	 * @throws IOException
	 */
	private String getSubscriptionResponse(String route, String authData) throws IOException {
		// get current time in UTC for auth token
		String strTime = DATE_FORMAT.format(new Date());
		// generate the token using the default pipe delimiter
		String authToken = generateAuthToken(strTime, authData);

		// do a GET on /v1/subscriptions/principal/{principalId}
		HttpGet get = new HttpGet(config.getEventingServer() + route);
		// use the auth token in the Authorization header
		get.setHeader("Authorization", authToken);
		HttpResponse response = httpClient.execute(get);

		String responseBody = null;
		HttpEntity responseEntity = response.getEntity();
		if (responseEntity != null) {
			responseBody = new String(EntityUtils.toString(responseEntity));
		}

		// expect a 200 status if the call was executed successfully
		int responseCode = response.getStatusLine().getStatusCode();
		if(responseCode!=200) {
			throw new RuntimeException("Unexpected response code of "+responseCode+" and body: " + responseBody);
		}
		
		return responseBody;
	}
	
	/**
	 * Creates a subscription for this callback url
	 * 
	 * Success will auto populate the id, createDate, principalId properties.
	 * 
	 * @param	sub	The message attributes to subscribe
	 * @throws IllegalArgumentException	Thrown when parameters are invalid
	 * @throws EventingException	Thrown when operation fails
	 */
	public void subscribe(Subscription sub) throws IllegalArgumentException, EventingException {	
		if(sub==null) {
			throw new IllegalArgumentException("Subscription object is required to subscribe!");
		}
		
		// these are all required to function
		String callbackUrl=sub.getCallbackUrl();
		if(callbackUrl==null || callbackUrl.length()==0) {
			throw new IllegalArgumentException("Callback URL is required");
		}
		
		// these are all optional individually, but something should exist
		String tagString = getTagString(sub.getTags());
		String client = sub.getClient()==null?"":sub.getClient();
		String clientString = sub.getClientString()==null?"":sub.getClientString();
		String messageType = sub.getMessageType()==null?"":sub.getMessageType();
		String system = sub.getSystem()==null?"":sub.getSystem();
		String subSystem = sub.getSubSystem()==null?"":sub.getSubSystem();
		

		String authData = client + clientString + system +subSystem +
						  callbackUrl + tagString + messageType;
	
		// we're going to force at least one tag
		if(authData.equals(callbackUrl)) {
			throw new IllegalArgumentException("Subscriptions should include a standard or custom tag!");
		}

		try {
			// get current time in UTC for auth token
			String strTime = DATE_FORMAT.format(new Date());
			// generate the token using the default pipe delimiter
			String authToken = generateAuthToken(strTime, authData);
	
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("CALLBACK-URL", callbackUrl));
			nvps.add(new BasicNameValuePair("WSDL-URI", "")); // N/A
			nvps.add(new BasicNameValuePair("TAGS", tagString));
			nvps.add(new BasicNameValuePair("CLIENT", client));
			nvps.add(new BasicNameValuePair("CLIENT-STRING", clientString));
			nvps.add(new BasicNameValuePair("MESSAGE-TYPE", messageType));
			nvps.add(new BasicNameValuePair("SYSTEM", system));
			nvps.add(new BasicNameValuePair("SUB-SYSTEM", subSystem));
			
			// do a POST on /v1/subscription
			HttpPost post = new HttpPost(config.getEventingServer() + "/v1/subscription");
			// use the auth token in the Authorization header
			post.setHeader("Authorization", authToken);
			// include all parameters for subscription
			post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = httpClient.execute(post);
	
			String responseBody = null;
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				responseBody = new String(EntityUtils.toString(responseEntity));
			}
	
			// expect a 200 status if the call was executed successfully
			int responseCode = response.getStatusLine().getStatusCode();
			if(responseCode!=200) {
				throw new RuntimeException("Unexpected response code of "+responseCode+" and body: " + responseBody);
			}
	
			if(logger.isDebugEnabled()) {
				logger.debug("NEW SUB: " +responseBody);
			}
			// {"subscription":{
			//    "id":"","principal_id":"","callback_url":"","wsdl_uri":"","queue_name":"",
			//    "date_created":"","date_cancelled":"",
			//    "tags":[{"tag":{"id":"","type":"","value":"" }}]
			//   }
			// }
			
			// fill in the newly created props on this sub
			Subscription newSub = this.parseSubscription(responseBody);
			sub.setId(newSub.getId());
			sub.setCreateDate(newSub.getCreateDate());
			sub.setPrincipalId(newSub.getPrincipalId());
		}
		catch (Exception e) {
			throw new EventingException(e);
		}
	}
	
	/**
	 * Deletes the provided subscription
	 * 
	 * @param subscriptionId	ID of subscription to be deleted
	 * @throws IllegalArgumentException	Thrown when parameters are invalid
	 * @throws EventingException	Thrown when operation fails
	 */
	public void unsubscribe(String subscriptionId) throws IllegalArgumentException, EventingException {
		if(subscriptionId==null || subscriptionId.length()==0) {
			throw new IllegalArgumentException("Subscription Id is required to unsubcribe!");
		}
		
		try {
			// get current time in UTC for auth token
			String strTime = DATE_FORMAT.format(new Date());
			// use the principal id as the payload for auth token
			String authData = subscriptionId;
			// generate the token using the default pipe delimiter
			String authToken = generateAuthToken(strTime, authData);
	
			// do a DELETE on /v1/subscription/{subscriptionId}
			HttpDelete delete = new HttpDelete(config.getEventingServer() + "/v1/subscription/" + subscriptionId);
			delete.setHeader("Authorization", authToken);
			HttpResponse response = httpClient.execute(delete);
			
			String responseBody = null;
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				responseBody = new String(EntityUtils.toString(responseEntity));
			}
			
			// expect a 200 status if the call was executed successfully
			int responseCode = response.getStatusLine().getStatusCode();
			if(responseCode!=200) {
				throw new RuntimeException("Unexpected response code of "+responseCode+" and body: " + responseBody);
			}
		}
		catch (Exception e) {
			throw new EventingException(e);
		}
	}
		
	/**
	 * Verifies the received message was delivered by LearningStudio Eventing.
	 * Eventing signs each message delivery with the subscriber's secret, so
	 * re-sign the message and compare the authorization parameter.
	 *
	 * @param delivery		The contents of a delivered message
	 * @throws IllegalArgumentException	Thrown when parameters are invalid
	 * @throws EventingException	Thrown when verification fails
	 */
	public void verifyAuthenticity(Delivery delivery) throws IllegalArgumentException, EventingException
	{
		if(delivery==null) {
			throw new IllegalArgumentException("Delivery object is required for verification!");
		}
		
		String authorization = delivery.getAuthorization();
		if(authorization==null || authorization.length()==0) {
			throw new IllegalArgumentException("Authorization is required for verification!");
		}
		String messageId = delivery.getMessageId();
		if(messageId==null || messageId.length()==0) {
			throw new IllegalArgumentException("Message id is required for verification!");
		}
		String messageType = delivery.getMessageType();
		if(messageType==null || messageType.length()==0) {
			throw new IllegalArgumentException("Message type is required for verification!");
		}
		String payload = delivery.getPayload();
		if(payload==null || payload.length()==0) {
			throw new IllegalArgumentException("Payload is required for verification!");
		}
		String payloadContentType = delivery.getPayloadContentType();
		if(payloadContentType==null || payloadContentType.length()==0) {
			throw new IllegalArgumentException("Payload content type is required  for verification!");
		}
		
		try {		
			String messageData = messageId + messageType + payloadContentType + payload;
			
			String splitDelimiter = "\\" + DEFAULT_DELIMITER;
	
			String[] tokenParts = delivery.getAuthorization().split(splitDelimiter);
	
			if (tokenParts.length != 3) {
			    throw new RuntimeException("Authorization token not 3 parts: "+ authorization);
			}
	
			String tokenPrincipal = tokenParts[0];
			String tokenTimestamp = tokenParts[1];
			String tokenMac = tokenParts[2];
	
			if (!config.getPrincipalId().equals(tokenPrincipal)) {
			    throw new RuntimeException("Principal Mismatch: " + config.getPrincipalId() + " != " + tokenPrincipal);
			}
	
			String authToken = generateAuthToken(tokenTimestamp, messageData);
	
			if (!authToken.equals(authorization)) {
			    throw new RuntimeException("Generated MAC does not match input MAC " + tokenMac);
			}
		}
		catch (Exception e) {
			throw new EventingException(e);
		}
	}
	
	/**
	 * Creates an auth token signed with principal's credentials
	 * 
	 * @param dateTime	utc time in yyyy-MM-dd'T'HH:mm:ssZ format 
	 * @param delimiter	Delimiter to between token parts
	 * @param principalId	Id portion of credentials
	 * @param key	Secret portion of credentials
	 * @param payload	Data being signed
	 * @return	Token value
	 * @throws UnsupportedEncodingException
	 */
	String generateAuthToken(String dateTime, String payload) throws UnsupportedEncodingException
	{
		// prepare the data
		byte[] macInput = (dateTime+payload).getBytes("UTF-8");
		byte[] macOutput = new byte[16];
		// sign the data
		CMac macProvider = new CMac(new AESFastEngine(), 128);
		macProvider.init(new KeyParameter(config.getPrincipalSecret().getBytes()));
		macProvider.update(macInput, 0, macInput.length);
		macProvider.doFinal(macOutput, 0);
		// hex the signed data
		String macOutputHex = new String(Hex.encode(macOutput));
		// concatenate the token parts
		return config.getPrincipalId() + DEFAULT_DELIMITER + dateTime + DEFAULT_DELIMITER + macOutputHex;
	}
	
	/**
	 * Converts tags from map into the correct format
	 * 
	 * @return	Tags in the correct format
	 */
	private String getTagString(Map<String,String> tags) {
		if(tags==null) return "";
		
		// separate the key and value with colons and the pairs with commas
		String delimiter="";
		StringBuilder tagsBuilder = new StringBuilder();
		for(String tagKey : tags.keySet()) {
			tagsBuilder.append(delimiter).append(tagKey).append(":").append(tags.get(tagKey));
			delimiter = ",";
		}
		
		return tagsBuilder.toString();
	}
	
	/* Convert json string to subscription array
	 *  {"subscriptions":[...] }
	 */
	private List<Subscription> parseSubscriptions(String jsonString) throws ParseException {
		JsonParser jsonParser = new JsonParser();
		JsonElement json = jsonParser.parse(jsonString);
		JsonArray jsonSubs = json.getAsJsonObject().get("subscriptions").getAsJsonArray();

		List<Subscription> subs = new ArrayList<Subscription>();
		for(Iterator<JsonElement> iter= jsonSubs.iterator();iter.hasNext();) {
			JsonObject jsonSub = iter.next().getAsJsonObject().get("subscription").getAsJsonObject();
			subs.add(parseSubscriptionObject(jsonSub));
		}
		
		return subs;
	}
	
	/* Convert json string to subscription
	{"subscription":{
	  "id":"",
	  "principal_id":"",
	  "callback_url":"",
	  "wsdl_uri":"",
	  "queue_name":"",
	  "date_created":"2015-01-20T18:58:11Z",
	  "date_cancelled":"",
	  "tags":[
        {"tag":{"id":"Client:Value","type":"Client","value":"Value"}},
        {"tag":{"id":"ClientString:Value","type":"ClientString","value":"Value"}},
        {"tag":{"id":"MessageType:Value","type":"MessageType","value":"Value"}},
        {"tag":{"id":"SubSystem:Value","type":"SubSystem","value":"Value"}},
        {"tag":{"id":"System:Value","type":"System","value":"Value"}},
        {"tag":{"id":"SomeTag:Value","type":"SomeTag","value":"Value"}}
	  ]
	}
	*/
	private Subscription parseSubscription(String jsonString) throws ParseException {
				
		JsonParser jsonParser = new JsonParser();
		JsonElement json = jsonParser.parse(jsonString);
		JsonObject jsonSub = json.getAsJsonObject().get("subscription").getAsJsonObject();
		return parseSubscriptionObject(jsonSub);
	}
	
	// convert json object to subscription
	private Subscription parseSubscriptionObject(JsonObject jsonSub) throws ParseException {
		Subscription sub = new Subscription();
		sub.setId(jsonSub.get("id").getAsString());
		sub.setPrincipalId(jsonSub.get("principal_id").getAsString());
		sub.setCreateDate(DATE_FORMAT.parse(jsonSub.get("date_created").getAsString()));
		sub.setCallbackUrl(jsonSub.get("callback_url").getAsString());
		sub.setTags(new HashMap<String,String>());
		
		JsonArray jsonTags = jsonSub.get("tags").getAsJsonArray();
		for(Iterator<JsonElement> iter= jsonTags.iterator();iter.hasNext();) {
			JsonObject jsonTag = iter.next().getAsJsonObject().get("tag").getAsJsonObject();
			String tagName = jsonTag.get("type").getAsString();
			String tagValue = jsonTag.get("value").getAsString();
			
			// map the tags to sub as appropriate
			if("Client".equals(tagName)) sub.setClient(tagValue);
			else if("ClientString".equals(tagName)) sub.setClientString(tagValue);
			else if("MessageType".equals(tagName)) sub.setMessageType(tagValue);
			else if("System".equals(tagName)) sub.setSystem(tagValue);
			else if("SubSystem".equals(tagName)) sub.setSubSystem(tagValue);
			else sub.getTags().put(tagName, tagValue);
				
		}
		
		return sub;
	}
}
