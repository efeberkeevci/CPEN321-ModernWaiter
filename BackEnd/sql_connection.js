const mysql = require('mysql')

function getConnection(){
    return mysql.createConnection({
        host: "localhost", 
        user: "admin", 
        password: "modernwaitercpen321!", 
        database: "MODERN_WAITER_DB", 
        port: 3306, 
        ssl:true
    })
}

module.exports = {getConnection}