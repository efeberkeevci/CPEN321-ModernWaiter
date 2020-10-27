var express = require("express");
var mysql = require('mysql');
const { subscribe, messageAccountisClosed } = require("./push_notification.js");
var app = express();
var push_notification = require("./push_notification.js");
var recommendation = require("./recommendation.js");

const env = require("dotenv").config({ path: "./.env" });
const stripe = require("stripe")(process.env.STRIPE_SECRET_KEY);

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
 * HTTP GET request to acquire items of a
 * restaurant representing its menu. It
 * returns a list of all the items belonging
 * to the restaurant with a status code of
 * 200 if successful.
 */
app.get("/items/:id", (req,res) =>{
    console.log("/items/{{id}}");
    let id = req.params.id;
    let sql_query = mysql.format("SELECT * FROM items WHERE restaurant_id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
});

/**
 * HTTP GET request to list option ids for
 * every item. This can be used to 
 * identify every additional option per item,
 * such as extra condiments or ingredients.
 * It returns the list with a status code of
 * 200 if successful.
 */
app.get("/item-options/:id", (req,res) => {
    console.log("/item-options/{{id}}");
    let id = req.params.id;
    let sql_query = mysql.format("SELECT * FROM items_options WHERE items_id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
});

/**
 * HTTP GET request to acquire all options
 * matching an option id. It returns the list 
 * with a status code of 200 if successful.
 */
app.get("/options/:id", (req,res) =>{
    console.log("/options/{{id}}");
    let id = req.params.id;
    let sql_query = mysql.format("SELECT * FROM options WHERE id = ?", [id]);
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
 * HTTP POST request to create an order. It 
 * returns a status code of 200 if successful.
 */
app.post("/order", (req,res) =>{
    console.log("/order");
    let sql_query = mysql.format("INSERT INTO orders ( users_id, tables_id, restaurant_id, amount, has_paid, is_active_session) VALUES(?,?,?,?,?,?)"
    ,[req.body.users_id, req.body.tables_id, req.body.restaurant_id,
      req.body.amount, req.body.has_paid, req.body.is_active_session]);
    
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
	res.send(result);

/*        let order_id_query = mysql.format("SELECT id FROM orders WHERE users_id = 1 && is_active_session = 1", [users_id]);

        con.query(order_id_query, function(err,result2,fields){
            if (err) {
                res.send(err);
            }
            result2=JSON.parse(JSON.stringify(result2))[0];
            let id = result2["id"];
            res.send({"orderId" : id});
        });*/
    });
})

/**
 * HTTP PUT request to update the amount spent
 * on ordered items. This is necessary to update
 * the amount if a user adds an item after 
 * they already created an order. It returns a 
 * status code of 200 if successful.
 */
app.put("/order/amount", (req,res) => {
    console.log("/order/amount");
    let id = req.body.id;
    let amount = req.body.amount;
    let sql_query_get_oldamount = mysql. format("SELECT amount FROM orders WHERE id = ?", [id]);
    con.query(sql_query_get_oldamount, function(err,result,fields){
        if (err) {
            console.log(result);
            throw err;
        };
        result=JSON.parse(JSON.stringify(result))[0];
        let old_amount = result["amount"];
        let new_amount = old_amount + amount;
        let sql_query = mysql.format("UPDATE orders SET amount = ? WHERE id = ?", [new_amount, id]);
        con.query(sql_query, function(err,result,fields){
            if (err) {
                console.log(err);
                return false;
            };
            return true;
        });
    });

})

/**
 * HTTP GET request to retrieve order details
 * of a specific user by their user Id. It
 * returns the details with a status code of 
 * 200 if successful.
 */
app.get("/order/user/:users_id", (req,res) =>{
    console.log("/order/user/{{userId}}");
    let users_id = req.params.users_id;
    let isActive = req.query.isActive;
    let sql_query = mysql.format("SELECT * FROM orders WHERE users_id = ? && is_active_session = ? ", [users_id, isActive]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
})

/**
 * HTTP GET request to retrieve order details
 * of a specific table by its table Id. It
 * returns the details with a status code of 
 * 200 if successful.
 */
app.get("/order/table/:tables_id", (req,res) =>{
    console.log("/order/table/{{tableId}}");
    let tables_id = req.params.tables_id;
    let isActive = req.query.isActive;
    let sql_query = mysql.format("SELECT * FROM orders WHERE tables_id = ? && is_active_session = ? ", [tables_id, isActive]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
})

/**
 * HTTP PUT request to update the session of orders
 * at a specific table. This is used to keep track 
 * of active and inactive sessions. If a group of
 * users finish their meal, we will use this to 
 * mark the session as complete. It returns a 
 * status code of 200 if successful.
 */
app.put("/order/session", (req,res) => {
    console.log("/order/session");
    let orderId = req.body.orderId;
    let isActive = req.body.isActive;
    let sql_query = mysql.format("UPDATE orders SET is_active_session = ? WHERE id = ?", [isActive, orderId]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        };
        res.send(result);
    });
})


/**
 * HTTP PUT request to mark whether an entire
 * order has been paid off. This is a layer of protection
 * to ensure a user does not create a double payment
 * for an order that has been paid for already. It returns a 
 * status code of 200 if successful.
 */
app.put("/order/paid", (req,res) => {
    console.log("/order/paid");
    let orderId = req.body.orderId;
    let hasPaid = req.body.hasPaid;
    console.log(orderId,hasPaid);
    console.log("In the order closed part");
    let sql_query = mysql.format("UPDATE orders SET has_paid = ? WHERE id = ?", [hasPaid,orderId]);
        con.query(sql_query, function(err,result,fields){
            if (err) {
		console.log(err);
                res.send(err);
            };
	console.log("Success");
            res.send(result);
            if(hasPaid){
               	console.log("in the push notification call");
		push_notification.push_notification_func(orderId);
	    }

        });
})

/**
 * HTTP GET request to retrieve a list of all the 
 * items associated with a specific order by the
 * order Id. It returns the list with a status 
 * code of 200 if successful.
 */
app.get("/ordered-items/:orderId", (req,res) => {
    console.log("/ordered-items/{{orderId}}");
    let orderId = req.params.orderId;
    let sql_query = mysql.format("SELECT * FROM ordered_items WHERE orders_id = ?", [ orderId]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        };
        res.send(result);
    });
})

/**
 * HTTP POST request to add an item to your ordered
 * items. This will be used when a user adds an item
 * to their meal during their time at the restaurant.
 * It returns a status code of 200 if successful.
 */
app.post("/ordered-items", (req,res) => {
    console.log("/ordered-items");
    let orderId = req.body.orderId;
    let itemId = req.body.itemId;
    let sql_query = mysql.format("INSERT INTO ordered_items (orders_id, items_id, has_paid, is_selected) VALUES(?,?, 0,0) ", [orderId,itemId]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        };
        updateOrderAmount(orderId, itemId);
        res.send(result);
    });
})

function updateOrderAmount(orderId, itemId) {
    let item_cost_query = mysql.format("SELECT cost FROM items WHERE id = ?", [itemId]);
    let old_amount_query = mysql. format("SELECT amount FROM orders WHERE id = ?", [orderId]);

    con.query(item_cost_query, function(err,result,fields){
        if (err) {
            console.log(result);
            throw err;
        };
        result=JSON.parse(JSON.stringify(result))[0];
        let item_cost = result["cost"];

        con.query(old_amount_query, function(err,result2,fields){
            if (err) {
                console.log(err);
                throw err;
            };
            result2=JSON.parse(JSON.stringify(result2))[0];
            let old_amount = result2["amount"];
            let new_amount = old_amount + item_cost;
            let update_query = mysql.format("UPDATE orders SET amount = ? WHERE id = ?", [new_amount, orderId]);

            con.query(update_query, function(err,result3,fields){
                if (err) {
                    console.log(err);
                    throw err;
                };
            });
        });
    });
    return true;
}

/**
 * HTTP PUT request to mark an item ordered
 * as already paid. This is to protect the user
 * from making double payments for the same item.
 * It returns a status code of 200 if successful.
 */
app.put("/ordered-items/paid", (req,res) => {
    console.log("/ordered-items/paid");
    let orderId = req.body.orderId;
    let itemId = req.body.itemId;
    let hasPaid = req.body.hasPaid;
    let sql_query = mysql.format("UPDATE ordered_items SET has_paid = ? WHERE orders_id = ? && items_id = ?", [hasPaid, orderId, itemId]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        };
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


