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
     * HTTP GET request to get details of a 
     * user. It returns the 
     * information with a status code of 200 
     * if successful.
     */
    app.get("/user/:id", (req, res)=>{
        console.log("/user/{{id}}");
        let id = req.params.id;
        let sql_query = mysql.format("SELECT * FROM users WHERE id = ?", [id]);
        con.query(sql_query, function(err,result,fields){
            if (err) {
                res.send(err);
            }
            res.send(result);
        });
    })

    /**
     * HTTP GET request to retrieve user preferences.
     */
    app.get("/user/preferences/:id", (req,res) => {
        console.log("/user/preferences/{{id}}");
        let id = req.params.id;
        let sql_query = mysql.format("SELECT preferences FROM users WHERE id = ?", [id]);
        con.query(sql_query, function(err,result,fields){
            if (err) {
                res.send(err);
            };
            res.send(result[0]);
        });
    })
}
