const mysql = require('mysql')
const sql = require("./../sql_connection.js")
const con = sql.getConnection()
const push_notification = require("./../push_notification.js")

/**
 * HTTP POST request to create an order. It 
 * returns a status code of 200 if successful.
 */
function createOrder(req, res){
    console.log("/orders")
    let sql_query = mysql.format("INSERT INTO orders ( users_id, tables_id, restaurant_id, amount, has_paid, is_active_session) VALUES(?,?,?,?,?,?)"
    ,[req.body.userId, req.body.tableId, req.body.restaurantId,
    req.body.amount, req.body.hasPaid, req.body.isActive])
    
    con.query(sql_query, function(err, result){
        if (err) {
            res.status(400).send({code : err.code, errno : err.errno})
        }
        res.send()
    })
}

/**
 * HTTP GET request to retrieve order details
 * of a specific user by their user Id. It
 * returns the details with a status code of 
 * 200 if successful.
 */
function getUserOrder(req, res){
    console.log("/orders/user/{{userId}}")
    let users_id = req.params.users_id
    let isActive = req.query.isActive
    let sql_query = mysql.format("SELECT * FROM orders WHERE users_id = ? && is_active_session = ? ", [users_id, isActive])
    con.query(sql_query, function(err, result){
        if (err) {
            res.send(err)
        }
        res.send(result)
    })
}

/**
 * HTTP GET request to retrieve order details
 * of a specific table by its table Id. It
 * returns the details with a status code of 
 * 200 if successful.
 */
function getTableOrder(req, res){
    console.log("/orders/table/{{tableId}}")
    let tables_id = req.params.tables_id
    let isActive = req.query.isActive
    let sql_query = mysql.format("SELECT * FROM orders WHERE tables_id = ? && is_active_session = ? ", [tables_id, isActive])
    con.query(sql_query, function(err, result){
        if (err) {
            res.send(err)
        }
        res.send(result)
    })
}

/**
 * HTTP PUT request to update the session of orders
 * at a specific table. This is used to keep track 
 * of active and inactive sessions. If a group of
 * users finish their meal, we will use this to 
 * mark the session as complete. It returns a 
 * status code of 200 if successful.
 */
function updateOrderSessionStatus(req, res){
    console.log("/orders/session")
    let orderId = req.body.orderId
    let isActive = req.body.isActive
    let sql_query = mysql.format("UPDATE orders SET is_active_session = ? WHERE id = ?", [isActive, orderId])
    con.query(sql_query, function(err, result){
        if (err) {
            res.send(err)
        }
        res.send()
    })
}

/**
 * HTTP PUT request to mark whether an entire
 * order has been paid off. This is a layer of protection
 * to ensure a user does not create a double payment
 * for an order that has been paid for already. It returns a 
 * status code of 200 if successful.
 */
function updateOrderPaidStatus(req, res){
    console.log("/orders/paid")
    let orderId = req.body.orderId
    let hasPaid = req.body.hasPaid
    console.log(orderId,hasPaid)
    console.log("In the order closed part")
    let sql_query = mysql.format("UPDATE orders SET has_paid = ? WHERE id = ?", [hasPaid,orderId])
    con.query(sql_query, function(err, result){
        if (err) {
            console.log(err)
            res.send(err)
        }

        res.send()
        console.log("Sending payment done notification")
        push_notification.push_notification_payment_done(orderId)
    })
}

module.exports = {createOrder, getUserOrder, getTableOrder, updateOrderSessionStatus, updateOrderPaidStatus}