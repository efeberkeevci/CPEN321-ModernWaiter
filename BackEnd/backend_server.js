const express = require("express");
const mysql = require('mysql');
// const { subscribe, messageAccountisClosed } = require("./push_notification.js");
const app = express();
const push_notification = require("./push_notification.js");

var con = mysql.createConnection({
    host: "localhost", 
    user: "admin", 
    password: "modernwaitercpen321!", 
    database: "MODERN_WAITER_DB", 
    port: 3306, 
    ssl:true
});

// Reference to the API routes
const items = require('./routes/items.js')(app, con)
const options = require('./routes/options.js')(app, con)
const orders = require('./routes/orders.js')(app, con)
const ordered_items = require('./routes/ordered_items.js')(app, con)
const payment = require('./routes/payment.js')(app)
const recommendation = require('./routes/recommendation.js')(app, con)
const restaurants = require('./routes/restaurants.js')(app, con)
const tables = require('./routes/tables.js')(app, con)
const users = require('./routes/users.js')(app, con)

app.use(express.json());

con.connect(function(err) {
    if (err) throw err;
    console.log("Connected!");
    var server = app.listen(3000,function(){
        var port = server.address().port;
        console.log("Server started listening at %s", port);
    });
});

// function pushNotificationsDemo(){
//     subscribe("dti7Svc4SC6utD7GPz9ZXy:APA91bEVZQYS-PJ1OYgYqbOElQkM_BTI7Si_S3eLXOpO-oIpM155VGAJzl-FJHYFUNMMYdfg3cOvWM6bX5X-6m6k7H6QQCdZA96qEZt3lwRpE68iOmb7uVx8hfbx5SZUuy8MnnTdGArg","1")
//     messageAccountisClosed(1);
// }

/**
 * HTTP POST request to register token for
 * push notification service.
 */
app.post("/registrationToken", (req,res) => {
    let registrationToken = req.body.registrationToken;
 console.log(registrationToken);
	let orderId = "1";
    res.send(push_notification.subscribe(registrationToken, orderId));
})

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
