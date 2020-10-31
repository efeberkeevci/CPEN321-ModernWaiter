const express = require('express')
const mysql = require('mysql')

module.exports = function(app, con){
    app.use(express.json());
    
    /**
     * HTTP GET request to acquire details of a
     * restaurant using the restaurant id. It
     * returns the details with a status code of
     * 200 if successful.
     */
    app.get("/restaurants/:id", (req, res) => {
        console.log("/restaurants/{{id}}");
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
