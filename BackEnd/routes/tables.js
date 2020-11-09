const mysql = require('mysql')
const sql = require("./../sql_connection.js")
const con = sql.getConnection()

/**
 * Gets details of the table
 * a user is seated at
 * @param {*} req Params include id
 * @param {*} res It returns the 
 * information with a status code of 200 
 * if successful, otherwise 400
 */
function getTable(req, res) {    
    console.log("GET /tables/{{id}}");

    let id = parseInt(req.params.id)
    if (isNaN(id)){
        res.status(400).send("Invalid id type, must be an integer")
    }

    let sql_query = mysql.format("SELECT * FROM tables WHERE id = ?", [id])
    con.query(sql_query, function(err, result){
        if (err) {
            res.status(400).send({code : err.code, errno : err.errno})
        }
        res.status(200).send(result)
    })
}

/**
 * Adds details of a table
 * @param {*} req Body includes tableNumber
 * @param {*} res It returns a status code of 200 
 * if successful, otherwise 400
 */
function addTable(req, res) {    
    console.log("POST /tables");

    let tableNumber = parseInt(req.body.tableNumber)
    if(isNaN(tableNumber)){
        res.status(400).send("Invalid table number type, must be an integer")
    }

    let sql_query = mysql.format("INSERT INTO tables (table_number) VALUES (?)", [tableNumber])
    con.query(sql_query, function(err, result){
        if (err) {
            res.status(400).send({code : err.code, errno : err.errno})
        }
        res.status(200).send(result)
    });
}
module.exports = {getTable}