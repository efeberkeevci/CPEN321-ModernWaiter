const mysql = require('mysql')
const sql = require("./../sql_connection.js")
const con = sql.getConnection()
const push_notification = require("../push_notification.js")

/**
 * Retrieves a list of all the 
 * items associated with a specific order by the
 * order Id.
 * @param {*} req Param includes order id
 * @param {*} res Status code 200 if successful, returns list of ordered items
 */
function getOrderedItems(req, res){
    console.log("GET /ordered-items/{{orderId}}")

    let orderId = parseInt(req.params.orderId,10)
    if (isNaN(orderId)){
        res.status(400).send("Invalid order id type, must be an integer")
    }

    let sql_query = mysql.format("SELECT * FROM ordered_items WHERE orders_id = ?", [orderId])
    con.query(sql_query, function(err, result){
        if (err) {
            res.status(400).send({code : err.code, errno : err.errno})
        }
        res.status(200).send(result)
    })
}

/**
 * Adds an multiple items to your ordered
 * items. This will be used when a user adds an item
 * to their meal during their time at the restaurant.
 * @param {*} req Body includes array of orderId and itemId
 * @param {*} res Status code 201 if successful, else 400 for any invalid itemId or orderId
 */
function addOrderedItems(req, res){
    console.log("POST /ordered-items/")
    let ordered_items = req.body
    var count = 0;

    for(var i = 0; i < ordered_items.length; i++){
        let orderId = parseInt(ordered_items[i].orderId,10)
        let itemId = parseInt(ordered_items[i].itemId,10)

        if (isNaN(orderId) || isNaN(itemId)){
            res.status(400).send("Invalid request body - order and item ids must be integers");
            return;
        }
        
        let sql_query = mysql.format("INSERT INTO ordered_items (orders_id, items_id, has_paid, is_selected) VALUES(?, ?, 0, 0) ", [orderId,itemId])
        con.query(sql_query, function(err, result){
            if (err) {
                res.status(400).send({code : err.code, errno : err.errno})
                return
            }
            count++;
            if (count == ordered_items.length - 1)
                res.status(201).send();
                return;
        })
    }
}

/**
 * Request to mark an item as selected by a specific user
 * @param {*} req Body includes orderId, itemId, userId, isSelected
 * @param {*} res Status code 200 if successful, else 400
 */
function updateSelectedStatus(req, res){
    console.log("PUT /ordered-items/selected")
    let orderId = parseInt(req.body.orderId,10)
    let itemId = parseInt(req.body.itemId,10)
    let userId = parseInt(req.body.userId,10)

    if (isNaN(orderId) || isNaN(itemId) || isNaN(userId)){
        res.status(400).send("Invalid request body - order, item and user ids must be integers");
        return;
    }

    let isSelected = req.body.isSelected
    let notIsSelected = isSelected === 1 ? 0 : 1
    let sql_query = mysql.format("UPDATE ordered_items SET is_selected = ?, users_id = ? WHERE orders_id = ? && items_id = ? && is_selected = ? LIMIT 1", [isSelected, userId, orderId, itemId, notIsSelected])
    con.query(sql_query, function(err, result){
        if (err) {
            res.status(400).send({code : err.code, errno : err.errno});
            return;
        }
        res.status(200).send()
        push_notification.push_notification_item_claimed(orderId)
        console.log(orderId + ":" + isSelected + " by " + userId + " for " + itemId);
        return;
    })
}

/**
 * Request to mark an item as paid.
 * @param {*} req Body includes orderId, itemId, hasPaid
 * @param {*} res Status code 200 if successful, else 400
 */
function updateOrderedItemPaidStatus(req, res){
    console.log("PUT /ordered-items/paid")
    let orderId = parseInt(req.body.orderId,10)
    let itemId = parseInt(req.body.itemId,10)

    if (isNaN(orderId) || isNaN(itemId)){
        res.status(400).send("Invalid request body - order and item ids must be integers");
        return;
    }

    let hasPaid = req.body.hasPaid
    let notHasPaid = hasPaid === 1 ? 0 : 1
    let sql_query = mysql.format("UPDATE ordered_items SET has_paid = ? WHERE orders_id = ? && items_id = ? && has_paid = ? LIMIT 1", [hasPaid, orderId, itemId, notHasPaid])
    con.query(sql_query, function(err, result){
        if (err) {
            res.status(400).send({code : err.code, errno : err.errno});
            return;
        }
        res.status(200).send();
        return;
    })
}

module.exports = {getOrderedItems, addOrderedItems, updateSelectedStatus, updateOrderedItemPaidStatus}
