const mysql = require('mysql')
const sql = require("./../sql_connection.js")
const con = sql.getConnection()

/**
 * Lists option ids for
 * every item. This can be used to 
 * identify every additional option per item,
 * such as extra condiments or ingredients.
 * @param {*} req Param includes id
 * @param {*} res List of ids with status code 200 if successful, otherwise 400
 */
function getItemOptions(req, res){
    console.log("/item-options/{{id}}")

    let id = parseInt(req.params.id)
    if (isNaN(id)){
        res.status(400).send("Invalid id type, must be an integer")
    }

    let sql_query = mysql.format("SELECT * FROM items_options WHERE items_id = ?", [id])
    con.query(sql_query, function(err, result) {
        if (err) {
            res.status(400).send({code : err.code, errno : err.errno})
        }
        res.status(200).send(result)
    })
}

/**
 * Acquires all options
 * matching an option id.
 * @param {*} req Param includes id
 * @param {*} res List of options with status code 200, otherwise 400
 */
function getOptions(req, res){
    console.log("/options/{{id}}")
    
    let id = parseInt(req.params.id)
    if (isNaN(id)){
        res.status(400).send("Invalid id type, must be an integer")
    }
    
    let sql_query = mysql.format("SELECT * FROM options WHERE id = ?", [id])
    con.query(sql_query, function(err, result) {
        if (err) {
            res.status(400).send({code : err.code, errno : err.errno})
        }
        res.status(200).send(result)
    })
}

module.exports = {getItemOptions, getOptions}