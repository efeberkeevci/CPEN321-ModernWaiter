const mysql = require('mysql')
const sql = require("./../sql_connection.js")
const con = sql.getConnection()
const push_notification = require("../push_notification")

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
 * @param {*} res Status code 201 if successful, else 400 for invalid itemId or orderId
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
        
        let item_cost_query = mysql.format("SELECT cost FROM items WHERE id = ?", [itemId])
        let old_amount_query = mysql. format("SELECT amount FROM orders WHERE id = ?", [orderId])

        con.query(item_cost_query, function(err, cost_result){
            if (err) {
                res.status(400).send({errno : err.errno, code : err.code})
                return
            }
            
            console.log(cost_result);

            if(cost_result.length == 0){
                res.status(400).send({message: "Failed to find item from provided item id"})
                return
            }

            let item_cost = 0

            try {
                cost_result = JSON.parse(JSON.stringify(cost_result))[0]
                item_cost = cost_result["cost"]
            } catch (error) {
                res.status(400).send({message: "Failed to find item from provided item id"})
                return
            }
            

            con.query(old_amount_query, function(err, old_amount_result){
                if (err) {
                    res.status(400).send({errno : err.errno, code : err.code})
                    return
                }

                if (old_amount_result.length == 0){
                    res.status(400).send({message: "Failed to find existing amount on order"})
                    return
                }

                let old_amount = 0

                try {
                    old_amount_result = JSON.parse(JSON.stringify(old_amount_result))[0]
                    old_amount = old_amount_result["amount"]
                } catch (error) {
                    res.status(400).send({message: "Failed to find existing amount on order"})
                    return
                }
                
                let new_amount = old_amount + item_cost
                let update_query = mysql.format("UPDATE orders SET amount = ? WHERE id = ?", [new_amount, orderId])

                con.query(update_query, function(err, result){
                    if (err) {
                        console.log(err)
                        res.status(400).send({errno : err.errno, code : err.code})
                        return
                    }
                })

                res.status(201).send()
            })
        })
    })
}

/**
 * HTTP PUT request to mark an item as selected
 */
function updateSelectedStatus(req, res){
    console.log("/ordered-items/selected")
    let orderId = req.body.orderId
    let itemId = req.body.itemId
    let userId = req.body.userId
    let isSelected = req.body.isSelected
    let notIsSelected = isSelected == 1 ? 0 : 1
    let user_name="";
    let item_name="";
    let sql_query = mysql.format("UPDATE ordered_items SET is_selected = ?, users_id = ? WHERE orders_id = ? && items_id = ? && is_selected = ? LIMIT 1", [isSelected, userId, orderId, itemId, notIsSelected])
    con.query(sql_query, function(err, result){
        if (err) {
            res.status(400).send({code : err.code, errno : err.errno})
        }
        res.status(200).send()
        push_notification.push_notification_item_claimed(orderId, itemId, userId);
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
    let notHasPaid = hasPaid == 1 ? 0 : 1
    let sql_query = mysql.format("UPDATE ordered_items SET has_paid = ? WHERE orders_id = ? && items_id = ? && has_paid = ? LIMIT 1", [hasPaid, orderId, itemId, notHasPaid])
    con.query(sql_query, function(err, result){
        if (err) {
            res.status(400).send({code : err.code, errno : err.errno})
        }
        res.status(200).send()
    })
}

// /**
//  * Function to update the order amount when 
//  * an item is added to the order.
//  * @param {*} orderId Id of the order.
//  * @param {*} itemId Id of the item.
//  */
// function updateOrderAmount(orderId, itemId) {
//     let item_cost_query = mysql.format("SELECT cost FROM items WHERE id = ?", [itemId])
//     let old_amount_query = mysql. format("SELECT amount FROM orders WHERE id = ?", [orderId])
//     let updateResponse = null;

//     con.query(item_cost_query, function(err, cost_result){
//         if (err) {
//             updateResponse = { status : false, body : {errno : err.errno, code : err.code} }
//             return
//         }
        
//         if(cost_result < 0){
//             updateResponse = { status : false, body : "Failed to find item from provided item id" }
//             return
//         }
        
//         cost_result = JSON.parse(JSON.stringify(cost_result))[0]
//         let item_cost = cost_result["cost"]

//         con.query(old_amount_query, function(err, old_amount_result){
//             if (err) {
//                 updateResponse = { status : false, body : {errno : err.errno, code : err.code} }
//                 return
//             }

//             if(old_amount_result < 0){
//                 updateResponse = { status : false, body : "Failed to find existing amount on order" }
//                 return
//             }

//             old_amount_result = JSON.parse(JSON.stringify(old_amount_result))[0]
//             let old_amount = old_amount_result["amount"]
//             let new_amount = old_amount + item_cost
//             let update_query = mysql.format("UPDATE orders SET amount = ? WHERE id = ?", [new_amount, orderId])

//             con.query(update_query, function(err, result){
//                 if (err) {
//                     console.log(err)
//                     updateResponse = { status : false, body : {errno : err.errno, code : err.code} }
//                     return
//                 }
//             })

//             updateResponse = {status : true, body : "" }
//         })
//     })

//     return updateResponse
// }


module.exports = {getOrderedItems, addOrderedItem, updateSelectedStatus, updateOrderedItemPaidStatus}
