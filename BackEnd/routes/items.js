const mysql = require('mysql')
const sql = require("./../sql_connection.js")
const con = sql.getConnection()

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