const mysql = require('mysql')
const sql = require("./../sql_connection.js")
const con = sql.getConnection()

/**
 * HTTP GET request to acquire details of a
 * restaurant using the restaurant id. It
 * returns the details with a status code of
 * 200 if successful.
 */
function getRestaurant(req, res){
    console.log("/restaurants/{{id}}");
    let id = req.params.id;
    let sql_query = mysql.format("SELECT * FROM restaurant WHERE id = ?", [id]);
    con.query(sql_query, function(err, result){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
}

module.exports = {getRestaurant}
