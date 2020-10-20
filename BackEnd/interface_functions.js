const { randomInt } = require('crypto');
var mysql = require('mysql');

/*
Interfaces:
Backend interface for Modern Waiter:

Given, from QR code we get the restaurant id and table id

The following are User API.
*/

//RESTAURANT

/*
Response:
{
    "id": 4,
    "name" : "Jack's diner",
    "location" : "East Vancouver",
    "tax_percentage" : 12.0,
    "service_fee_percentage" : 6.5
} 
*/
function GetRestaurantById(id){
    let sql_query = mysql.format("SELECT * FROM restaurant WHERE id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) throw err;
        console.log(result);
        return(result);
    });
}


//ITEMS

/*
Response:
[{
    "id" : 2,
    "restaurant_id" : 4,
    "name" : "Salad",
    "description" : "blah blah",
    "type" : "Sides",
    "cost" : 5.50,
    "calories" : 200,
    "popularity_count" : 11,
    "image" : "some url or local file path of image in vm"
}, 
{
    "id" : 3,
    "restaurant_id" : 4,
    "name" : "Cheeseburger",
    "description" : "blah blah",
    "type" : "Burger",
    "cost" : 8.50,
    "calories" : 1100,
    "popularity_count" : 19,
    "image" : "some url or local file path of image in vm"
}]
*/

function GetItemsByRestaurantId(restaurantId){
    let sql_query = mysql.format("SELECT * FROM items WHERE restaurant_id = ?", [restaurantId]);
    con.query(sql_query, function(err,result,fields){
        if (err) throw err;
        console.log(result);
        return(result);
    });
}


//ITEMs_OPTIONS
/*
Response:

[{
    "id" : 1,
    "items_id" : 3,
    "options_id" : 1 
},
{
    "id" : 2,
    "items_id" : 3,
    "options_id" : 2 
},
{
    "id" : 3,
    "items_id" : 3,
    "options_id" : 3 
}]
*/
function GetOptionsIdsByItemsId(itemsId){
    let sql_query = mysql.format("SELECT * FROM items_options WHERE items_id = ?", [itemsId]);
    con.query(sql_query, function(err,result,fields){
        if (err) throw err;
        console.log(result);
        return(result);
    });
}



//OPTIONS 
/*
Response:
[{
    "id" : 1,
    "name" : "Tomatoes",
    "cost" : 0 
},
{
    "id" : 2,
    "name" : "Cherries",
    "cost" : 0 
},
{
    "id" : 3,
    "name" : "Avocado",
    "cost" : 1.50 
}]
*/
function GetOptionsById( id){
    let sql_query = mysql.format("SELECT * FROM options WHERE id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) throw err;
        console.log(result);
        return(result);
    });
}




//Tables:
/*
Response:

[{"id" : 1,
  "table_number" : 13
}]
*/
function GetTableById(id){
    let sql_query = mysql.format("SELECT * FROM tables WHERE id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) throw err;
        console.log(result);
        return(result);
    });
}



//USERS

/*
Response:
{
    "id" : 1,
    "username" : "tawsifh",
    "email" : "tawsif@h.com",
    "created_at" : "11Oct20 blah blah"
}
*/
function GetUserById(id){
    let sql_query = mysql.format("SELECT * FROM users WHERE id = ?", [id]);
    con.query(sql_query, function(err,result,fields){
        if (err) throw err;
        console.log(result);
        return(result);
    });
}




//ORDERS

/*
Request:

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
function PostOrder(requestBody){
    console.log("IN the postorder function");
    let request = requestBody[0];
    let sql_query = mysql.format("INSERT INTO orders (id, users_id, tables_id, restaurant_id, amount,ordered_at, has_paid, is_active_session) VALUES(?,?,?,?,?,?,?,?)"
    ,[request["id"], request["users_id"], request["tables_id"], request["restaurant_id"], request["amount"], request["ordered_at"], request["has_paid"], request["is_active_session"]]);
    
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


/*

bool UpdateOrderAmountById(int id, double amount);
bool UpdateOrderHasPaidFlag(bool hasPaid);
bool UpdateOrderIsActiveSessionFlag(bool isActiveSession);

JSON GetOrdersByUserId(int userId, bool isActive);

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

JSON GetOrdersByTableId(int tableId, bool isActive);

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

Ordered Items:

JSON GetOrderedItemsByOrderId(int orderId);

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

bool UpdateOrderItemsByOrderId(int orderId, int itemId);
bool UpdateOrderItemsHasPaidFlag(bool hasPaid);

*/

var con = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "12341234"
  });
  
  con.connect(function(err) {
    if (err) throw err;
    console.log("Connected!");
  });
  con.query("USE MODERN_WAITER_DB", function(err,result,fields){
    if (err) throw err;
    });
  main();


function main(){
    
    GetRestaurantById(1);
    GetItemsByRestaurantId(1);
    GetOptionsIdsByItemsId(1);
    GetOptionsById(1);
    GetTableById(1);
    GetUserById(1);
    PostOrder([{
        "id" : Math.ceil(Math.random() * 100),
        "users_id" : 1,
        "tables_id" : 1,
        "restaurant_id" : 1,
        "amount" : 25.0,
        "ordered_at" : "2020-10-20 14:15:11",
        "has_paid" : 0,
        "is_active_session" : 1
    }]);
    
}




