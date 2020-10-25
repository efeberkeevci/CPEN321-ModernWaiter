//This document handles server duties for the Firebase Cloud Messaging

//When customers open up their account they should subscribe to a new topic
//Topic value should be the table's orderID
function subscribe(){
    // These registration tokens come from the client FCM SDKs.
    var registrationTokens = [
        'YOUR_REGISTRATION_TOKEN_1',
        // ...
        'YOUR_REGISTRATION_TOKEN_n'
    ];

    // Subscribe the devices corresponding to the registration tokens to the
    // topic.
    admin.messaging().subscribeToTopic(registrationTokens, topic)
        .then(function(response) {
        // See the MessagingTopicManagementResponse reference documentation
        // for the contents of response.
        console.log('Successfully subscribed to topic:', response);
        })
        .catch(function(error) {
        console.log('Error subscribing to topic:', error);
        });
    
}
//When payment is done, the customers are being unsubscribed from the abovementioned created subscription
function unsubscribe(){
    // These registration tokens come from the client FCM SDKs.
    var registrationTokens = [
        'YOUR_REGISTRATION_TOKEN_1',
        // ...
        'YOUR_REGISTRATION_TOKEN_n'
    ];
    
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
//Later use case => Send push notification upon succsfull payment of an item
    //To do this, in the app.post("/mark/item/has/paid" route make callback to this function