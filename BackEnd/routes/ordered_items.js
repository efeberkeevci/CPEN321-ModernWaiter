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
    /**
     * HTTP GET request to retrieve a list of all the 
     * items associated with a specific order by the
     * order Id. It returns the list with a status 
     * code of 200 if successful.
     */
    app.get("/ordered-items/:orderId", (req,res) => {
        console.log("/ordered-items/{{orderId}}")
        let orderId = req.params.orderId
        let sql_query = mysql.format("SELECT * FROM ordered_items WHERE orders_id = ?", [ orderId])
        con.query(sql_query, function(err,result,fields){
            if (err) {
                res.send(err)
            }
            res.send(result)
        })
    })

    /**
     * HTTP POST request to add an item to your ordered
     * items. This will be used when a user adds an item
     * to their meal during their time at the restaurant.
     * It returns a status code of 200 if successful.
     */
    app.post("/ordered-items", (req,res) => {
        console.log("/ordered-items")
        let orderId = req.body.orderId
        let itemId = req.body.itemId
        let sql_query = mysql.format("INSERT INTO ordered_items (orders_id, items_id, has_paid, is_selected) VALUES(?,?, 0,0) ", [orderId,itemId])
        con.query(sql_query, function(err,result,fields){
            if (err) {
                res.send(err)
            }
            updateOrderAmount(orderId, itemId)
            res.send(result)
        })
    })

    /**
     * HTTP PUT request to mark an item ordered
     * as already paid. This is to protect the user
     * from making double payments for the same item.
     * It returns a status code of 200 if successful.
     */
    app.put("/ordered-items/paid", (req,res) => {
        console.log("/ordered-items/paid")
        let orderId = req.body.orderId
        let itemId = req.body.itemId
        let hasPaid = req.body.hasPaid
        let sql_query = mysql.format("UPDATE ordered_items SET has_paid = ? WHERE orders_id = ? && items_id = ?", [hasPaid, orderId, itemId])
        con.query(sql_query, function(err,result,fields){
            if (err) {
                res.send(err)
            }
            res.send(result)
        })
    })

    function updateOrderAmount(orderId, itemId) {
        let item_cost_query = mysql.format("SELECT cost FROM items WHERE id = ?", [itemId])
        let old_amount_query = mysql. format("SELECT amount FROM orders WHERE id = ?", [orderId])

        con.query(item_cost_query, function(err,result,fields){
            if (err) {
                console.log(result)
                throw err
            }
            result=JSON.parse(JSON.stringify(result))[0]
            let item_cost = result["cost"]

            con.query(old_amount_query, function(err,result2,fields){
                if (err) {
                    console.log(err)
                    throw err
                }
                result2=JSON.parse(JSON.stringify(result2))[0]
                let old_amount = result2["amount"]
                let new_amount = old_amount + item_cost
                let update_query = mysql.format("UPDATE orders SET amount = ? WHERE id = ?", [new_amount, orderId])

                con.query(update_query, function(err,result3,fields){
                    if (err) {
                        console.log(err)
                        throw err
                    }
                })
            })
        })
        return true
    }
}
