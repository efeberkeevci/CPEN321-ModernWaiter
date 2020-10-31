const express = require('express')
const mysql = require('mysql')

var con = mysql.createConnection({
    host: "localhost", 
    user: "admin", 
    password: "modernwaitercpen321!", 
    database: "MODERN_WAITER_DB", 
    port: 3306, 
    ssl:true
})

module.exports = function(app){
    app.use(express.json());
    
    /**
     * HTTP GET request to acquire details of a
     * restaurant using the restaurant id. It
     * returns the details with a status code of
     * 200 if successful.
     */
    app.get("/restaurant/:id", (req, res) => {
        console.log("/restaurant/{{id}}");
        let id = req.params.id;
        let sql_query = mysql.format("SELECT * FROM restaurant WHERE id = ?", [id]);
        con.query(sql_query, function(err, result){
            if (err) {
                res.send(err);
            }
            res.send(result);
        });
    });
}
