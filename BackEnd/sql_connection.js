const mysql = require('mysql')
const env = require("dotenv").config({ path: "./.env" })

function getConnection(){
    return mysql.createConnection({
        host: process.env.HOST, 
        user: process.env.USER, 
        password: process.env.PASSWORD, 
        database: process.env.DATABASE, 
        port: process.env.PORT, 
        ssl:true
    })
}

module.exports = {getConnection}