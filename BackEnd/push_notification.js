//This document handles server duties for the Firebase Cloud Messaging

//When customers open up their account they should subscribe to a new topic
//Topic value should be the table's orderID
// These registration tokens come from the client FCM SDKs.

function subscribe(registrationToken, orderId){

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

//Initial use case => Send push notification when order is closed aka the whole amount has been paid
function messageAccountisClosed(orderId){

    var message = {
    title : "Payment Completed!",
    topic: orderId
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
//Later use case => Send push notification upon succsfull payment of an item
    //To do this, in the app.post("/mark/item/has/paid" route make callback to this function

module.exports = {messageAccountisClosed, subscribe};