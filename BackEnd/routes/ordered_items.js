const mysql = require('mysql')
const sql = require("./../sql_connection.js")
const con = sql.getConnection()

/**
 * Retrieves a list of all the 
 * items associated with a specific order by the
 * order Id.
 * @param {*} req Param includes order id
 * @param {*} res Status code 200 if successful, returns list of ordered items
 */
function getOrderedItems(req, res){
    console.log("/ordered-items/{{orderId}}")
    let orderId = req.params.orderId
    let sql_query = mysql.format("SELECT * FROM ordered_items WHERE orders_id = ?", [orderId])
    con.query(sql_query, function(err, result){
        if (err) {
            res.status(400).send({code : err.code, errno : err.errno})
        }
        res.status(200).send(result)
    })
}

/**
 * Adds an item to your ordered
 * items. This will be used when a user adds an item
 * to their meal during their time at the restaurant.
 * @param {*} req Body includes orderId and itemId
 * @param {*} res 
 */
function addOrderedItem(req, res){
    console.log("/ordered-items")
    let orderId = req.body.orderId
    let itemId = req.body.itemId
    let sql_query = mysql.format("INSERT INTO ordered_items (orders_id, items_id, has_paid, is_selected) VALUES(?,?, 0, 0) ", [orderId,itemId])
    con.query(sql_query, function(err, result){
        if (err) {
            res.status(400).send({code : err.code, errno : err.errno})
            return
        }
        updateOrderAmount(orderId, itemId)
        res.status(201).send()
    })
}

/**
 * HTTP PUT request to mark an item ordered
 * as already paid. This is to protect the user
 * from making double payments for the same item.
 * It returns a status code of 200 if successful.
 */
function updateOrderedItemPaidStatus(req, res){
    console.log("/ordered-items/paid")
    let orderId = req.body.orderId
    let itemId = req.body.itemId
    let hasPaid = req.body.hasPaid
    let sql_query = mysql.format("UPDATE ordered_items SET has_paid = ? WHERE orders_id = ? && items_id = ?", [hasPaid, orderId, itemId])
    con.query(sql_query, function(err, result){
        if (err) {
            res.send({code : err.code, errno : err.errno})
        }
        res.send()
    })
}

/**
 * Function to update the order amount when 
 * an item is added to the order.
 * @param {*} orderId Id of the order.
 * @param {*} itemId Id of the item.
 */
function updateOrderAmount(orderId, itemId) {
    let item_cost_query = mysql.format("SELECT cost FROM items WHERE id = ?", [itemId])
    let old_amount_query = mysql. format("SELECT amount FROM orders WHERE id = ?", [orderId])

    con.query(item_cost_query, function(err, cost_result){
        if (err) {
            console.log(cost_result)
            throw err
        }
        cost_result = JSON.parse(JSON.stringify(cost_result))[0]
        let item_cost = cost_result["cost"]

        con.query(old_amount_query, function(err, old_amount_result){
            if (err) {
                console.log(err)
                throw err
            }
            old_amount_result = JSON.parse(JSON.stringify(old_amount_result))[0]
            let old_amount = old_amount_result["amount"]
            let new_amount = old_amount + item_cost
            let update_query = mysql.format("UPDATE orders SET amount = ? WHERE id = ?", [new_amount, orderId])

            con.query(update_query, function(err, result){
                if (err) {
                    console.log(err)
                    throw err
                }
            })
        })
    })
    return true
}

module.exports = {getOrderedItems, addOrderedItem, updateOrderedItemPaidStatus}
