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
     * HTTP GET request to acquire items of a
     * restaurant representing its menu. It
     * returns a list of all the items belonging
     * to the restaurant with a status code of
     * 200 if successful.   
     */
    app.get("/items/:id", (req,res) => {
        console.log("/items/{{id}}")
        let id = req.params.id
        let sql_query = mysql.format("SELECT * FROM items WHERE restaurant_id = ?", [id])
        con.query(sql_query, function(err,result){
            if (err) {
                res.send(err)
            }
            res.send(result)
        })
    })
}
