var express = require("express");
var mysql = require('mysql');
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

//Get restaurant info by restaurant id
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

//Get items with restaurant id
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

//Get item options by item id
app.get("/item_options/:id", (req,res) => {
    let id = req.params.id;
    let sql_query = mysql.format("SELECT * FROM items_options WHERE items_id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
});

//Get options by id
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

//Get table by id
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

//Get user by id
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

//Post order with given info
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

//Update order amount with a given amount
app.post("/add/amount/to/order", (req,res) => {
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

//Get order by userID
app.get("/order/user/id/:users_id/:isActive", (req,res) =>{
    let users_id = req.params.users_id;
    let isActive = req.params.isActive;
    let sql_query = mysql.format("SELECT * FROM orders WHERE users_id = ? && is_active_session = ? ", [users_id, isActive]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
})

//Get order by tablesID
app.get("/order/table/id/:tables_id/:isActive", (req,res) =>{
    let tables_id = req.params.tables_id;
    let isActive = req.params.isActive;
    let sql_query = mysql.format("SELECT * FROM orders WHERE tables_id = ? && is_active_session = ? ", [tables_id, isActive]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
})


// If the session is complete, set the active session flag to false
app.post("/order/is/active/session", (req,res) => {
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



//If all the ordered items have been paid for, mark this as has paid 
app.post("/order/has/paid", (req,res) => {
    let orderId = req.body.orderId;
    let hasPaid = req.body.hasPaid;
    let sql_query = mysql.format("UPDATE orders SET has_paid = ? WHERE id = ?", [hasPaid,orderId]);
        con.query(sql_query, function(err,result,fields){
            if (err) {
                res.send(err);
            };
            res.send(result);
            if(hasPaid){
                push_notification.messageAccountisClosed(orderId);
            }
        });
})

//Gets ordered items via orderid
app.get("/ordered/items/:orderId", (req,res) => {
    let orderId = req.params.orderId;
    let sql_query = mysql.format("SELECT * FROM ordered_items WHERE orders_id = ?", [ orderId]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        };
        res.send(result);
    });
})

// adds a new item to the ordered items table, used when you add an item to your meal later on during your time at the restaurant
app.post("/add/item/to/order", (req,res) => {
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

// if you pay for the specific item, mark it as has paid
//TODO: should we add userıd so we can send push notification saying x user paid y item
app.post("/mark/item/has/paid", (req,res) => {
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
    let orderId = req.body.userId;
    res.send(push_notification.subscribe(registrationToken,orderId));
})