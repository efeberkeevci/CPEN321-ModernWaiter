const mysql = require('mysql')

var con = mysql.createConnection({
    host: "localhost", 
    user: "admin", 
    password: "modernwaitercpen321!", 
    database: "MODERN_WAITER_DB", 
    port: 3306, 
    ssl:true
})

export function getMenu(req, res){
    console.log("/items/{{id}}")
    let id = req.params.id
    let sql_query = mysql.format("SELECT * FROM items WHERE restaurant_id = ?", [id])
    con.query(sql_query, function(err, result){
        if (err) {
            res.send(err)
        }
        res.send(result)
    })
}