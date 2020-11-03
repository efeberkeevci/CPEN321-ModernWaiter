const mysql = require('mysql')
const sql = require("./../sql_connection.js")
const con = sql.getConnection()

/**
 * Acquires details of a
 * restaurant using the restaurant id
 * @param {*} req Params include id
 * @param {*} res Returns the details with a status code of
 * 200 if successful, otherwise 400
 */
function getRestaurant(req, res){
    console.log("/restaurants/{{id}}");
    let id = req.params.id;
    let sql_query = mysql.format("SELECT * FROM restaurant WHERE id = ?", [id]);
    con.query(sql_query, function(err, result){
        if (err) {
            res.status(400).send({code : err.code, errno : err.errno});
        }
        res.status(200).send(result);
    });
}

module.exports = {getRestaurant}
