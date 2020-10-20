var express = require("express");
var mysql = require('mysql');

var backend_functions = require("./interface_functions");

var app = express();

app.use(express.json());

var con = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "12341234"
  });
  
  con.connect(function(err) {
    if (err) throw err;
    console.log("Connected!");
    var server = app.listen(3000,function(){
        var port = server.address().port;
        console.log("Server started listening at %s", port);
    });
  });
  con.query("USE MODERN_WAITER_DB", function(err,result,fields){
    if (err) throw err;
});