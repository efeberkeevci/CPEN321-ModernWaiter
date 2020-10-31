const express = require('express')
const mysql = require('mysql')

module.exports = function(app, con){
    app.use(express.json());
    
    /**
     * HTTP GET request to list option ids for
     * every item. This can be used to 
     * identify every additional option per item,
     * such as extra condiments or ingredients.
     * It returns the list with a status code of
     * 200 if successful.
     */
    app.get("/item-options/:id", (req, res) => {
        console.log("/item-options/{{id}}")
        let id = req.params.id
        let sql_query = mysql.format("SELECT * FROM items_options WHERE items_id = ?", [id])
        con.query(sql_query, function(err, result) {
            if (err) {
                res.send(err)
            }
            res.send(result)
        })
    })

    /**
     * HTTP GET request to acquire all options
     * matching an option id. It returns the list 
     * with a status code of 200 if successful.
     */
    app.get("/options/:id", (req, res) => {
        console.log("/options/{{id}}")
        let id = req.params.id
        let sql_query = mysql.format("SELECT * FROM options WHERE id = ?", [id])
        con.query(sql_query, function(err, result) {
            if (err) {
                res.send(err)
            }
            res.send(result)
        })
    })
}
