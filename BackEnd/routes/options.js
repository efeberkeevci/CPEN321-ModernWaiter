const mysql = require('mysql')
const sql = require("./../sql_connection.js")
const con = sql.getConnection()

/**
 * HTTP GET request to list option ids for
 * every item. This can be used to 
 * identify every additional option per item,
 * such as extra condiments or ingredients.
 * It returns the list with a status code of
 * 200 if successful.
 */
function getItemOptions(req, res){
    console.log("/item-options/{{id}}")
    let id = req.params.id
    let sql_query = mysql.format("SELECT * FROM items_options WHERE items_id = ?", [id])
    con.query(sql_query, function(err, result) {
        if (err) {
            res.send(err)
        }
        res.send(result)
    })
}

/**
 * HTTP GET request to acquire all options
 * matching an option id. It returns the list 
 * with a status code of 200 if successful.
 */
function getOptions(req, res){
    console.log("/options/{{id}}")
    let id = req.params.id
    let sql_query = mysql.format("SELECT * FROM options WHERE id = ?", [id])
    con.query(sql_query, function(err, result) {
        if (err) {
            res.send(err)
        }
        res.send(result)
    })
}

module.exports = {getItemOptions, getOptions}