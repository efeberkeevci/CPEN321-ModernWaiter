const express = require('express')
const mysql = require('mysql')

module.exports = function(app, con){
    app.use(express.json());
    
    /**
     * HTTP GET request to get details of a 
     * user. It returns the 
     * information with a status code of 200 
     * if successful.
     */
    app.get("/users/:id", (req, res) => {
        console.log("/users/{{id}}");
        let id = req.params.id;
        let sql_query = mysql.format("SELECT * FROM users WHERE id = ?", [id]);
        con.query(sql_query, function(err, result){
            if (err) {
                res.send(err);
            }
            res.send(result);
        });
    })

    /**
     * HTTP GET request to retrieve user preferences.
     */
    app.get("/users/preferences/:id", (req, res) => {
        console.log("/users/preferences/{{id}}");
        let id = req.params.id;
        let sql_query = mysql.format("SELECT preferences FROM users WHERE id = ?", [id]);
        con.query(sql_query, function(err, result){
            if (err) {
                res.send(err);
            };
            res.send(result[0]);
        });
    })
}
