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
    console.log("/tables/{{id}}");
    let id = req.params.id;
    let sql_query = mysql.format("SELECT * FROM tables WHERE id = ?", [id]);
    con.query(sql_query, function(err, result){
        if (err) {
            res.status(400).send({code : err.code, errno : err.errno});
        }
        res.status(200).send(result);
    });
}

module.exports = {getTable}