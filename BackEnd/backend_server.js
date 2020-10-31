var express = require("express");
var mysql = require('mysql');
const { subscribe, messageAccountisClosed } = require("./push_notification.js");
var app = express();
var push_notification = require("./push_notification.js");

const items = require('./routes/items.js')(app)
const options = require('./routes/options.js')(app)
const orders = require('./routes/orders.js')(app)
const ordered_items = require('./routes/ordered_items.js')(app)
const payment = require('./routes/payment.js')(app)
const recommendation = require('./routes/recommendation.js')(app)
const restaurants = require('./routes/restaurants.js')(app)
const tables = require('./routes/tables.js')(app)
const users = require('./routes/users.js')(app)

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

// /**
//  * HTTP POST request to create an order. It 
//  * returns a status code of 200 if successful.
//  */
// app.post("/order", (req,res) =>{
//     console.log("/order")
//     let sql_query = mysql.format("INSERT INTO orders ( users_id, tables_id, restaurant_id, amount, has_paid, is_active_session) VALUES(?,?,?,?,?,?)"
//     ,[req.body.users_id, req.body.tables_id, req.body.restaurant_id,
//     req.body.amount, req.body.has_paid, req.body.is_active_session])
    
//     con.query(sql_query, function(err,result,fields){
//         if (err) {
//             res.send(err)
//         }
//     res.send(result)
//     //push_notification.push_notification_order_received("1")

// /*        let order_id_query = mysql.format("SELECT id FROM orders WHERE users_id = 1 && is_active_session = 1", [users_id])

//         con.query(order_id_query, function(err,result2,fields){
//             if (err) {
//                 res.send(err)
//             }
//             result2=JSON.parse(JSON.stringify(result2))[0]
//             let id = result2["id"]
//             res.send({"orderId" : id})
//         })*/
//     })
// })

// con.query("USE MODERN_WAITER_DB", function(err,result,fields){
//     if (err) throw err;
// });

function pushNotificationsDemo(){
    subscribe("dti7Svc4SC6utD7GPz9ZXy:APA91bEVZQYS-PJ1OYgYqbOElQkM_BTI7Si_S3eLXOpO-oIpM155VGAJzl-FJHYFUNMMYdfg3cOvWM6bX5X-6m6k7H6QQCdZA96qEZt3lwRpE68iOmb7uVx8hfbx5SZUuy8MnnTdGArg","1")
    messageAccountisClosed(1);
}

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
