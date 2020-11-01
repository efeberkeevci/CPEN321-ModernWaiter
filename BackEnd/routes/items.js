const mysql = require('mysql')
const sql = require("./../sql_connection.js")
const con = sql.getConnection()

/**
 * HTTP GET request to acquire items of a
 * restaurant representing its menu. It
 * returns a list of all the items belonging
 * to the restaurant with a status code of
 * 200 if successful.   
 */
function getMenu(req, res){
    console.log("/items/{{id}}")
    let id = req.params.id
    let sql_query = mysql.format("SELECT * FROM items WHERE restaurant_id = ?", [id])
    con.query(sql_query, function(err, result){
        if (err) {
            res.send(err)
        }
        res.send(result)
    })
}

module.exports = {getMenu}