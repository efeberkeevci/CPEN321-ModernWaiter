const express = require('express')
const mysql = require('mysql')
const push_notification = require("./../push_notification.js")
const { subscribe, messageAccountisClosed } = require("./../push_notification.js")

var con = mysql.createConnection({
    host: "localhost", 
    user: "admin", 
    password: "modernwaitercpen321!", 
    database: "MODERN_WAITER_DB", 
    port: 3306, 
    ssl:true
})

module.exports = function(app){
    app.use(express.json());
    
    /**
     * HTTP POST request to create an order. It 
     * returns a status code of 200 if successful.
     */
    app.post("/orders", (req,res) =>{
        console.log("/orders")
        let sql_query = mysql.format("INSERT INTO orders ( users_id, tables_id, restaurant_id, amount, has_paid, is_active_session) VALUES(?,?,?,?,?,?)"
        ,[req.body.userId, req.body.tableId, req.body.restaurantId,
        req.body.amount, req.body.hasPaid, req.body.isActive])
        
        con.query(sql_query, function(err,result){
            if (err) {
                res.send(err)
            }
        res.send()
        //push_notification.push_notification_order_received("1")

    /*        let order_id_query = mysql.format("SELECT id FROM orders WHERE users_id = 1 && is_active_session = 1", [users_id])

            con.query(order_id_query, function(err,result2,fields){
                if (err) {
                    res.send(err)
                }
                result2=JSON.parse(JSON.stringify(result2))[0]
                let id = result2["id"]
                res.send({"orderId" : id})
            })*/
        })
    })

    /**
     * HTTP PUT request to update the amount spent
     * on ordered items. This is necessary to update
     * the amount if a user adds an item after 
     * they already created an order. It returns a 
     * status code of 200 if successful.
     */
    app.put("/orders/amount", (req,res) => {
        console.log("/orders/amount")
        let id = req.body.id
        let amount = req.body.amount
        let sql_query_get_oldamount = mysql. format("SELECT amount FROM orders WHERE id = ?", [id])
        con.query(sql_query_get_oldamount, function(err,result){
            if (err) {
                console.log(result)
                throw err
            }
            result=JSON.parse(JSON.stringify(result))[0]
            let old_amount = result["amount"]
            let new_amount = old_amount + amount
            let sql_query = mysql.format("UPDATE orders SET amount = ? WHERE id = ?", [new_amount, id])
            con.query(sql_query, function(err,result){
                if (err) {
                    console.log(err)
                    return false
                }
                return true
            })
        })

    })

    /**
     * HTTP GET request to retrieve order details
     * of a specific user by their user Id. It
     * returns the details with a status code of 
     * 200 if successful.
     */
    app.get("/orders/user/:users_id", (req,res) =>{
        console.log("/orders/user/{{userId}}")
        let users_id = req.params.users_id
        let isActive = req.query.isActive
        let sql_query = mysql.format("SELECT * FROM orders WHERE users_id = ? && is_active_session = ? ", [users_id, isActive])
        con.query(sql_query, function(err,result){
            if (err) {
                res.send(err)
            }
            res.send(result)
        })
    })

    /**
     * HTTP GET request to retrieve order details
     * of a specific table by its table Id. It
     * returns the details with a status code of 
     * 200 if successful.
     */
    app.get("/orders/table/:tables_id", (req,res) =>{
        console.log("/orders/table/{{tableId}}")
        let tables_id = req.params.tables_id
        let isActive = req.query.isActive
        let sql_query = mysql.format("SELECT * FROM orders WHERE tables_id = ? && is_active_session = ? ", [tables_id, isActive])
        con.query(sql_query, function(err,result){
            if (err) {
                res.send(err)
            }
            res.send(result)
        })
    })

    /**
     * HTTP PUT request to update the session of orders
     * at a specific table. This is used to keep track 
     * of active and inactive sessions. If a group of
     * users finish their meal, we will use this to 
     * mark the session as complete. It returns a 
     * status code of 200 if successful.
     */
    app.put("/orders/session", (req,res) => {
        console.log("/orders/session")
        let orderId = req.body.orderId
        let isActive = req.body.isActive
        let sql_query = mysql.format("UPDATE orders SET is_active_session = ? WHERE id = ?", [isActive, orderId])
        con.query(sql_query, function(err,result){
            if (err) {
                res.send(err)
            }
            res.send(result)
        })
    })

    /**
     * HTTP PUT request to mark whether an entire
     * order has been paid off. This is a layer of protection
     * to ensure a user does not create a double payment
     * for an order that has been paid for already. It returns a 
     * status code of 200 if successful.
     */
    app.put("/orders/paid", (req,res) => {
        console.log("/orders/paid")
        let orderId = req.body.orderId
        let hasPaid = req.body.hasPaid
        console.log(orderId,hasPaid)
        console.log("In the order closed part")
        let sql_query = mysql.format("UPDATE orders SET has_paid = ? WHERE id = ?", [hasPaid,orderId])
            con.query(sql_query, function(err,result){
                if (err) {
            console.log(err)
                    res.send(err)
                }
        console.log("Success")
                res.send(result)
                    console.log("Sending payment done notification")
            push_notification.push_notification_payment_done(orderId)
            })
    })
}