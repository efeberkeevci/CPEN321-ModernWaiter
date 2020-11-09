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

    if (typeof(restaurantId) != 'number'){
        res.status(400).send("Invalid restaurant id type, must be an integer")
    }

    let sql_query = mysql.format("SELECT * FROM items WHERE restaurant_id = ?", [restaurantId])
    con.query(sql_query, function(err, result){
        if (err) {
            res.status(400).send({code : err.code, errno : err.errno})
        }
        res.status(200).send(result)
    })
}

/**
 * Adds an item to a restaurant's menu records
 * @param {*} req Body with details of an item
 * @param {*} res List of items with a status code of 200 if successful, else 400
 */
function addToMenu(req, res){
    console.log("/items")
    let restaurantId = req.body.restaurantId
    let name = req.body.name
    let type = req.body.type
    let cost = req.body.cost
    let description = req.body.description
    let calories = req.body.calories
    let popularityCount = req.body.popularityCount
    let image = req.body.image

    let sql_query = mysql.format("INSERT INTO items (restaurantId, name, type, cost, description, calories, popularity_count, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", [restaurantId, name, type, cost, description, calories, popularityCount, image])
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