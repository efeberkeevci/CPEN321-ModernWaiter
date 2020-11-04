//This document handles server duties for the Firebase Cloud Messaging

//When customers open up their account they should subscribe to a new topic
//Topic value should be the table's orderID
// These registration tokens come from the client FCM SDKs.

var admin = require('firebase-admin');
var serviceAccount = require("./modern-waiter-47e96-firebase-adminsdk-exb5m-82aceb8e76.json");
var items = require("./routes/items");
var users = require("./routes/users");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://modern-waiter-47e96.firebaseio.com"
});


function subscribe(registrationToken, orderId){
    console.log("SUBSCRIBED for " + orderId);
    // Subscribe the devices corresponding to the registration tokens to the
    // topic.
    let topic = orderId;
    admin.messaging().subscribeToTopic(registrationToken, topic)
        .then(function(response) {
        // See the MessagingTopicManagementResponse reference documentation
        // for the contents of response.
        console.log('Successfully subscribed to topic:', response);
        return "Succesfully added user to push notifications list";
        })
        .catch(function(error) {
        console.log('Error subscribing to topic:', error);
        return "Error adding user to push notifications list";
        });
    
}
//When payment is done, the customers are being unsubscribed from the abovementioned created subscription
// These registration tokens come from the client FCM SDKs.

function unsubscribe(registrationToken, orderId){
    let topic = orderId;
    // Unsubscribe the devices corresponding to the registration tokens from
    // the topic.
    admin.messaging().unsubscribeFromTopic(registrationTokens, topic)
        .then(function(response) {
        // See the MessagingTopicManagementResponse reference documentation
        // for the contents of response.
        console.log('Successfully unsubscribed from topic:', response);
        })
        .catch(function(error) {
        console.log('Error unsubscribing from topic:', error);
        });
    
}

function push_notification_payment_done(orderId){
  console.log("Sending payment done push notification");

  var message = {
    notification: {
      title: 'Payment Completed!',
      body: 'All items are paid.'
    },
    topic:orderId
  };

  // Send a message to devices subscribed to the provided topic.
  admin.messaging().send(message)
    .then((response) => {
      // Response is a message ID string.
      console.log('Successfully sent message:', response);
    })
    .catch((error) => {
      console.log('Error sending message:', error);
    });
}

function push_notification_order_received(orderId){
  console.log("Sending order received push notification for topic " + orderId);

  var message = {
    notification: {
      title: 'Order Received!',
      body: 'Your order is being prepared now'
    },
    topic:orderId
  };

  // Send a message to devices subscribed to the provided topic.
  admin.messaging().send(message)
    .then((response) => {
      // Response is a message ID string.
      console.log('Successfully sent message:', response);
    })
    .catch((error) => {
      console.log('Error sending message:', error);
    });
}


function push_notification_item_claimed(orderId) {
  console.log("Sending order received push notification");

  var message = {
    notification: {
      title: 'Item Claimed!',
      body: 'Your order is being prepared now'
    },
    topic:orderId
  };

  // Send a message to devices subscribed to the provided topic.
  admin.messaging().send(message)
    .then((response) => {
      // Response is a message ID string.
      console.log('Successfully sent message:', response);
    })
    .catch((error) => {
      console.log('Error sending message:', error);
    });
}
module.exports = {push_notification_payment_done, push_notification_order_received, push_notification_item_claimed, subscribe, unsubscribe};
