# LearningStudio Eventing Library

LearningStudio's Eventing capabilities reduce the need to “poll” for status changes by pushing them directly to your application. This library handles the details of subscribing to messages, managing subscriptions, and verifying authenticity of received messages.

## Getting Help 

Please use the PDN Developer Community at https://community.pdn.pearson.com


## Pre-Requisites 

You should be sure to read the API documentation and background information for LearningStudio Eventing while using this library. Here are some key places to start:

 * [Eventing Introduction](http://developer.pearson.com/apis/eventing)
 * [Eventing Setup](http://developer.pearson.com/learningstudio/set-1)
 * [Event Contracts](http://developer.pearson.com/product/event-contracts)


## Library Usage


### Create a client

~~~~~~~~~~~~~~~~~~~~~~~
import com.pearson.pdn.learningstudio.eventing.*;

Configuration config = new Configuration();
config.setPrincipalId("{principalId}");		// application id
config.setPrincipalSecret("{principalSecret}");	// application secret
config.setEventingServer("{eventingServer}");	// url of eventing server

// client to use in following examples
EventingClient eventing = new EventingClient(config);	
~~~~~~~~~~~~~~~~~~~~~~~

### Create a subscription
 
~~~~~~~~~~~~~~~~~~~~~~~
Subscription sub = new Subscription();
sub.setCallbackUrl("{callbackUrl}");	// url to receive message
// each of these filters are optional, but one of them must exist
sub.setMessageType("{messageType}");	// message type (most useful)
sub.setSystem("{system}");		// publishing system id (rarely used)
sub.setSubSystem("{subSystem}");	// publishing subsystem id (rarely used)
sub.setClient("{client}");		// campus id (useful for vendors)
sub.setClientString("{clientString}");  // campus string (useful for vendors)

// Include additional filters (optional)
Map<String, String> tags = new HashMap<String, String>();
tags.put("{key}","{value}");	// Applicable {key} values: UserId, CourseId
sub.setTags(tags);

try {
  eventing.subscribe(sub);
  // these properties were populated
  sub.getId();			// unique identifier for subscription
  sub.getCreateDate();		// creation date of subscription
  sub.getPrincipalId();		// principal that created subscription
}
catch(EventingException e) {
  // subscribe failed
}
~~~~~~~~~~~~~~~~~~~~~~~

### Delete a subscription

~~~~~~~~~~~~~~~~~~~~~~~
try {  
  eventing.unsubscribe("{subscriptionId}");
}
catch(EventingException e) {
  // unsubscribe failed
}
~~~~~~~~~~~~~~~~~~~~~~~

### Lookup all subscriptions

~~~~~~~~~~~~~~~~~~~~~~~
try {
  List<Subscription> subs = eventing.getSubscriptions();
  // might be empty
}
catch(EventingException e) {
  // subscriptions lookup failed
}
~~~~~~~~~~~~~~~~~~~~~~~

### Lookup a specific subscription 

~~~~~~~~~~~~~~~~~~~~~~~
try {
  Subscription sub = eventing.getSubscription("{subscriptionId}");
  // subscription found
}
catch(EventingException e) {
  // subscription lookup failed
}
~~~~~~~~~~~~~~~~~~~~~~~

### Verify authenticity of a delivered message

~~~~~~~~~~~~~~~~~~~~~~~
Delivery delivery = new Delivery();
// populate the following with received parameters of same name
delivery.setAttemptId("{attemptId}");			// unique delivery id (optional)
delivery.setAuthorization("{authorization}");		// auth signature
delivery.setMessageId("{messageId}");			// message id
delivery.setMessageType("{messageType}");		// event type
delivery.setPayloadContentType("{payloadContentType}");	// application/json
delivery.setPayload("{payload}");			// json payload
		
try {
  eventing.verifyAuthenticity(delivery);
  // authorized
}
catch(EventingException e) {
  // not authorized
}
~~~~~~~~~~~~~~~~~~~~~~~

## License

Copyright (c) 2015 Pearson Education, Inc.
Created by Pearson Developer Services

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
