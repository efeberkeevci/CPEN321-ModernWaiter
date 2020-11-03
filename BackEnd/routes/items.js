const mysql = require('mysql')
const sql = require("./../sql_connection.js")
const con = sql.getConnection()

/**
 * Acquires items of a restaurant representing its 
 * menu. It returns a list of all the items 
 * belonging to the restaurant
 * @param {*} req Param includes restaurantId
 * @param {*} res List of items with a status code of 200 if successful, else 400.
 */
function getMenu(req, res){
    console.log("/items/{{restaurantId}}")
    let restaurantId = req.params.restaurantId
    let sql_query = mysql.format("SELECT * FROM items WHERE restaurant_id = ?", [restaurantId])
    con.query(sql_query, function(err, result){
        if (err) {
            res.status(400).send({code : err.code, errno : err.errno})
        }
        res.status(200).send(result)
    })
}

function getItemName(itemId){
    let sql_query = mysql.format("SELECT name FROM items WHERE id = ?", [itemId]);
    con.query(sql_query, function(err, result){
        if (err) {
            console.log("Error in item name retrieval: ", err);
        }
        else{
            return result;
        }
    });
}

module.exports = {getMenu, getItemName}