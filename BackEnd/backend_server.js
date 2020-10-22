var express = require("express");
var mysql = require('mysql');
var app = express();


app.use(express.json());
var con = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "12341234"
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



//ORDERS

/*
Request:

[{
    “users_id” : 2
    "tables_id" : 3,
    "restaurant_id" : 1,
    "amount" : 25.0,
    "ordered_at" : "some timestamp",
    "has_paid" : false,
    "is_active_session" : true
}]
*/
function PostOrder(requestBody){
    console.log("IN the postorder function");
    let request = requestBody[0];
    let sql_query = mysql.format("INSERT INTO orders ( users_id, tables_id, restaurant_id, amount,ordered_at, has_paid, is_active_session) VALUES(?,?,?,?,?,?,?)"
    ,[request["users_id"], request["tables_id"], request["restaurant_id"], request["amount"], request["ordered_at"], request["has_paid"], request["is_active_session"]]);
    
    //let sql_query = ("INSERT INTO orders (id, users_id, tables_id, restaurant_id, amount,ordered_at, has_paid, is_active_session) VALUES(" + request["id"] + "," + request["user_id"] + "," +  request["tables_id"] + "," +  request["restaurant_id"] + "," +  request["amount"]  + "," + "\'" + String(request["ordered_at"])+ "\'" + "," +  request["has_paid"]  + "," +  request["is_active_session"] +")");
    con.query(sql_query, function(err,result,fields){
        if (err) {
            console.log(err);
            return false;
        }
        console.log(result);
        return(true);
    });
}


// if you add an item to your order, update the total amount in the order
function UpdateOrderAmountById(orderId, amount){
    let sql_query_get_oldamount = mysql. format("SELECT amount FROM orders WHERE id = ?", [orderId]);
    con.query(sql_query_get_oldamount, function(err,result,fields){
        if (err) {
            console.log(result);
            throw err;
        };
        result=JSON.parse(JSON.stringify(result))[0];
        let old_amount = result["amount"];
        let new_amount = old_amount + amount;
        let sql_query = mysql.format("UPDATE orders SET amount = ? WHERE id = ?", [new_amount, orderId]);
        con.query(sql_query, function(err,result,fields){
            if (err) {
                console.log(err);
                return false;
            };
            return true;
        });
    });
}

// if all the ordered items have been paid for, mark this as has paid 
function UpdateOrderHasPaidFlag(hasPaid, orderId){
    /*
    let sql_query_get_ordered_items_paid_info = mysql. format("SELECT hasPaid FROM ordered_items WHERE id = ?", [orderId]);
    con.query(sql_query_get_ordered_items_paid_info, function(err,result,fields){
        if (err) {
            console.log(result);
            throw err;
        };
        result=JSON.parse(JSON.stringify(result))[0];
       
        console.log(result);
        */
        let sql_query = mysql.format("UPDATE orders SET has_paid = ? WHERE id = ?", [hasPaid,orderId]);
        con.query(sql_query, function(err,result,fields){
            if (err) {
                console.log(err);
                return false;
            };
            console.log(result);
            return true;
        });
    //});
}

// if the session is complete, set the active session flag to false
function UpdateOrderIsActiveSessionFlag(isActive, orderId ){
    let sql_query = mysql.format("UPDATE orders SET is_active_session = ? WHERE id = ?", [isActive, orderId]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            console.log(err);
            return false;
        };
        console.log(result);
        return true;
    });
}

/*
Response:

[{
    "id" : 1,
    “users_id” : 2
    "tables_id" : 3,
    "restaurant_id" : 1,
    "amount" : 25.0,
    "ordered_at" : "some timestamp",
    "has_paid" : false,
    "is_active_session" : true
}]
*/
function GetOrdersByUserId(users_id, isActive){
    let sql_query = mysql.format("SELECT * FROM orders WHERE users_id = ? && is_active_session = ? ", [users_id, isActive]);
    con.query(sql_query, function(err,result,fields){
        if (err) throw err;
        console.log(result);
        return(result);
    });
}

/*
Response:
[{
    "id" : 1,
    "users_id" : 2
    "tables_id" : 3,
    "restaurant_id" : 1,
    "amount" : 25.0,
    "ordered_at" : "some timestamp",
    "has_paid" : false,
    "is_active_session" : true
},
{
    "id" : 5,
    "users_id" : 4
    "tables_id" : 3,
    "restaurant_id" : 1,
    "amount" : 35.0,
    "ordered_at" : "some timestamp",
    "has_paid" : false,
    "is_active_session" : true
},
{
    "id" : 11,
    "users_id" : 5
    "tables_id" : 3,
    "restaurant_id" : 1,
    "amount" : 10.0,
    "ordered_at" : "some timestamp",
    "has_paid" : false,
    "is_active_session" : true
}]
*/
function GetOrdersByTableId( tables_id, isActive){
    let sql_query = mysql.format("SELECT * FROM orders WHERE tables_id = ? && is_active_session = ? ", [tables_id, isActive]);
    con.query(sql_query, function(err,result,fields){
        if (err) throw err;
        console.log(result);
        return(result);
    });
}

//ORDERED ITEMS

/*
Response:

[{
    "id" : 5,
    "orders_id" : 11
    "items_id" : 2,
    "has_paid" : true,
    “Is_selected” : false
},
{
    "id" : 6,
    "orders_id" : 11
    "items_id" : 3,
    "has_paid" : false,
    “Is_selected: : false
}]

*/
function GetOrderedItemsByOrderId(orderId){
    let sql_query = mysql.format("SELECT * FROM ordered_items WHERE orders_id = ?", [ orderId]);
    con.query(sql_query, function(err,result,fields){
        if (err) throw err;
        console.log(result);
        return(result);
    });
}

// adds a new item to the ordered items table, used when you add an item to your meal later on during your time at the restaurant
function UpdateOrderItemsByOrderId( orderId,  itemId){
    let sql_query = mysql.format("INSERT INTO ordered_items (orders_id, items_id, has_paid, is_selected) VALUES(?,?, 0,0) ", [orderId,itemId]);
    con.query(sql_query, function(err,result,fields){
        if (err) return false;
        console.log(result);
        return true;
    });
}

// if you pay for the specific item, mark it as has paid
function UpdateOrderItemsHasPaidFlag(items_id, orders_id, hasPaid){
    let sql_query = mysql.format("UPDATE ordered_items SET has_paid = ? WHERE orders_id = ? && items_id = ?", [hasPaid, orders_id, items_id]);
    con.query(sql_query, function(err,result,fields){
        if (err) return false;
        console.log(result);
        return true;
    });
}

//Get restaurant info by restaurant id
app.get("/restaurant", (req,res) => {
    let id = req.query.id;
    let sql_query = mysql.format("SELECT * FROM restaurant WHERE id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
});

//Get items with restaurant id
app.get("/items", (req,res) =>{
    let id = req.query.id;
    let sql_query = mysql.format("SELECT * FROM items WHERE restaurant_id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
});

//Get item options by item id
app.get("/item_options", (req,res) => {
    let id = req.query.id;
    let sql_query = mysql.format("SELECT * FROM items_options WHERE items_id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
});

//Get options by id
app.get("/options", (req,res) =>{
    let id = req.query.id;
    let sql_query = mysql.format("SELECT * FROM options WHERE id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
});

//Get table by id
app.get("/table", (req,res) => {    
    let id = req.query.id;
    let sql_query = mysql.format("SELECT * FROM tables WHERE id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
});

//Get user by id
app.get("/user", (req, res)=>{
    let id = req.query.id;
    let sql_query = mysql.format("SELECT * FROM users WHERE id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
})

//TODO:CHECK IF THE DATA PASSED CORRECTLY
//Post order with given info
app.post("/order", (req,res) =>{
    let request = req.query;
    let sql_query = mysql.format("INSERT INTO orders ( users_id, tables_id, restaurant_id, amount,ordered_at, has_paid, is_active_session) VALUES(?,?,?,?,?,?,?)"
    ,[request["users_id"], request["tables_id"], request["restaurant_id"], request["amount"], request["ordered_at"], request["has_paid"], request["is_active_session"]]);
    
    con.query(sql_query, function(err,result,fields){
        if (err) {
            console.log(err);
            return false;
        }
        console.log(result);
        return(true);
    });
})