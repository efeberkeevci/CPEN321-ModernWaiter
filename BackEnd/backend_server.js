var express = require("express");
var mysql = require('mysql');
const { subscribe, messageAccountisClosed } = require("./push_notification.js");
var app = express();
var push_notification = require("./push_notification.js");
var recommendation = require("./recommendation.js");

const env = require("dotenv").config({ path: "./.env" });
const stripe = require("stripe")(process.env.STRIPE_SECRET_KEY);

const items = require('./routes/items.js')(app)
const orders = require('./routes/orders.js')(app)
const options = require('./routes/options.js')(app)
const ordered_items = require('./routes/ordered_items.js')(app)

app.use(express.json());

var con = mysql.createConnection({
    host: "localhost", 
    user: "admin", 
    password: "modernwaitercpen321!", 
    database: "MODERN_WAITER_DB", 
    port: 3306, 
    ssl:true
});

con.connect(function(err) {
    if (err) throw err;
    console.log("Connected!");
    var server = app.listen(3000,function(){
        var port = server.address().port;
        console.log("Server started listening at %s", port);
    });
});
con.query("USE MODERN_WAITER_DB", function(err,result,fields){
    if (err) throw err;
});

function pushNotificationsDemo(){
    subscribe("dti7Svc4SC6utD7GPz9ZXy:APA91bEVZQYS-PJ1OYgYqbOElQkM_BTI7Si_S3eLXOpO-oIpM155VGAJzl-FJHYFUNMMYdfg3cOvWM6bX5X-6m6k7H6QQCdZA96qEZt3lwRpE68iOmb7uVx8hfbx5SZUuy8MnnTdGArg","1")
    messageAccountisClosed(1);
}

/**
 * HTTP GET request to acquire details of a
 * restaurant using the restaurant id. It
 * returns the details with a status code of
 * 200 if successful.
 */
app.get("/restaurant/:id", (req,res) => {
    console.log("/restaurant/{{id}}");
    let id = req.params.id;
    let sql_query = mysql.format("SELECT * FROM restaurant WHERE id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
});


/**
 * HTTP GET request to get details of the table
 * a user is seated at. It returns the 
 * information with a status code of 200 
 * if successful.
 */
app.get("/table/:id", (req,res) => {    
    console.log("/table/{{id}}");
    let id = req.params.id;
    let sql_query = mysql.format("SELECT * FROM tables WHERE id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
});

/**
 * HTTP GET request to get details of a 
 * user. It returns the 
 * information with a status code of 200 
 * if successful.
 */
app.get("/user/:id", (req, res)=>{
    console.log("/user/{{id}}");
    let id = req.params.id;
    let sql_query = mysql.format("SELECT * FROM users WHERE id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
})

/**
 * HTTP GET request to retrieve user preferences.
 */
app.get("/user/preferences/:id", (req,res) => {
    console.log("/user/preferences/{{id}}");
    let id = req.params.id;
    let sql_query = mysql.format("SELECT preferences FROM users WHERE id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        };
        res.send(result[0]);
    });
})

/**
 * HTTP GET request to item descriptions.
 */
app.get("/item/descriptions/:restaurantId", (req,res) => {
    console.log("/item/descriptions/{{restaurantId}}");
    let restaurantId = req.params.restaurantId;
    let sql_query = mysql.format("SELECT id, description FROM items WHERE restaurant_id = ?", [restaurantId]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        };
        res.send(result);
    });
})

/**
 * HTTP GET request to get a recommendation for
 * an item at a specific restaurant unique to the
 * user. Returns the recommended item Id with a
 * status code of 200.
 */
app.get("/item/recommend", (req,res) => {
    console.log("/item/recommend");
    let users_id = 1; //req.body.users_id;
    let restaurant_id = 1; //req.body.restaurant_id;
    let user_query = mysql.format("SELECT preferences FROM users WHERE id = ?", [users_id]);
    let desc_query = mysql.format("SELECT id, description FROM items WHERE restaurant_id = ?", [restaurant_id]);

    con.query(user_query, function(err,prefResult,fields){
        if (err) {
            res.send(err);
        };
        var preference = prefResult[0]["preferences"];

        con.query(desc_query, function(err,descResult,fields){
            if (err) {
                res.send(err);
            };

            console.log(JSON.stringify(descResult));
            var descriptionJsonArray = JSON.parse(JSON.stringify(descResult));
            var itemDescriptionMap = new Map()

            descriptionJsonArray.forEach(item => {
                itemDescriptionMap.set(item["id"], item["description"])
            });

            console.log(itemDescriptionMap);
	    var itemId = recommendation.getRecommendation(preference, itemDescriptionMap);

            res.send({"itemId" : itemId});
        });
    });
})

/**
 * HTTP POST request to register token for
 * push notification service.
 */
app.post("/registrationToken", (req,res) => {
    let registrationToken = req.body.registrationToken;
 console.log(registrationToken);
	let orderId = "1";
    res.send(push_notification.subscribe(registrationToken,orderId));
})

/* GET users listing. */
app.get('/key', (req, res) => {
  res.send({ publishableKey: process.env.STRIPE_PUBLISHABLE_KEY });
});

app.post('/pay', async(req, res) => {
  const { paymentMethodId, paymentIntentId, currency, useStripeSdk, orderAmount } = req.body;

  try {
    let intent;
    if (paymentMethodId) {
      // Create new PaymentIntent with a PaymentMethod ID from the client.
      intent = await stripe.paymentIntents.create({
        amount: orderAmount,
        currency: currency,
        payment_method: paymentMethodId,
        confirmation_method: "manual",
        confirm: true,
        // If a mobile client passes `useStripeSdk`, set `use_stripe_sdk=true`
        // to take advantage of new authentication features in mobile SDKs
        use_stripe_sdk: useStripeSdk,
      });
      // After create, if the PaymentIntent's status is succeeded, fulfill the order.
    } else if (paymentIntentId) {
      // Confirm the PaymentIntent to finalize payment after handling a required action
      // on the client.
      intent = await stripe.paymentIntents.confirm(paymentIntentId);
      // After confirm, if the PaymentIntent's status is succeeded, fulfill the order.
    }
    res.send(generateResponse(intent));
  } catch (e) {
    // Handle "hard declines" e.g. insufficient funds, expired card, etc
    // See https://stripe.com/docs/declines/codes for more
    res.send({ error: e.message });
  }
})

const generateResponse = intent => {
  // Generate a response based on the intent's status
  switch (intent.status) {
    case "requires_action":
    case "requires_source_action":
      // Card requires authentication
      return {
        requiresAction: true,
        clientSecret: intent.client_secret
      };
    case "requires_payment_method":
    case "requires_source":
      // Card was not properly authenticated, suggest a new payment method
      return {
        error: "Your card was denied, please provide a new payment method"
      };
    case "succeeded":
      // Payment is complete, authentication not required
      // To cancel the payment after capture you will need to issue a Refund (https://stripe.com/docs/api/refunds)
      return { clientSecret: intent.client_secret };
  }
};

//Informs server that the current cart is checked out
app.get("/checkout", (req,res) =>{
    console.log("Checkout received");
    push_notification.push_notification_order_received("1");
    res.send("Success");
});

/* HTTP GET request for health check */
app.get('/_health', (req, res) => {
  res.status(200).send('ok')
})
