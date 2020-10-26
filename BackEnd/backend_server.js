var express = require("express");
var mysql = require('mysql');
const { subscribe, messageAccountisClosed } = require("./push_notification.js");
var app = express();
var push_notification = require("./push_notification.js");

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
    let sql_query = mysql.format("INSERT INTO orders ( users_id, tables_id, restaurant_id, amount,ordered_at, has_paid, is_active_session) VALUES(?,?,?,?,?,?,?)"
    ,[req.body.users_id, req.body.tables_id, req.body.restaurant_id,
      req.body.amount, req.body.ordered_at, req.body.has_paid, req.body.is_active_session]);
    
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
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
		    push_notification.push_notification_func();
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
app.post("/ordered-items/:orderId", (req,res) => {
    let orderId = req.body.orderId;
    let itemId = req.body.itemId;
    let sql_query = mysql.format("INSERT INTO ordered_items (orders_id, items_id, has_paid, is_selected) VALUES(?,?, 0,0) ", [orderId,itemId]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        };
        res.send(result);
    });
})

//TODO: should we add userıd so we can send push notification saying x user paid y item

/**
 * HTTP PUT request to mark an item ordered
 * as already paid. This is to protect the user
 * from making double payments for the same item.
 * It returns a status code of 200 if successful.
 */
app.put("/ordered-items/paid", (req,res) => {
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

app.post("/registrationToken", (req,res) => {
    let registrationToken = req.body.registrationToken;
    let orderId = "1";
    res.send(push_notification.subscribe(registrationToken,orderId));
})
