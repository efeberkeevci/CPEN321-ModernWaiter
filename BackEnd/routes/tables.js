const mysql = require('mysql')
const sql = require("./../sql_connection.js")
const con = sql.getConnection()

/**
 * HTTP GET request to get details of the table
 * a user is seated at. It returns the 
 * information with a status code of 200 
 * if successful.
 */
function getTable(req, res) {    
    console.log("/tables/{{id}}");
    let id = req.params.id;
    let sql_query = mysql.format("SELECT * FROM tables WHERE id = ?", [id]);
    con.query(sql_query, function(err, result){
        if (err) {
            res.send(err);
        }
        res.send(result);
    });
}

module.exports = {getTable}