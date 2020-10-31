const express = require('express')
const mysql = require('mysql')
var recommendation = require("./../recommendation.js");

var con = mysql.createConnection({
    host: "localhost", 
    user: "admin", 
    password: "modernwaitercpen321!", 
    database: "MODERN_WAITER_DB", 
    port: 3306, 
    ssl:true
})

module.exports = function(app){
    /**
     * HTTP GET request to item descriptions.
     */
    app.get("/item/descriptions/:restaurantId", (req,res) => {
        console.log("/item/descriptions/{{restaurantId}}");
        let restaurantId = req.params.restaurantId;
        let sql_query = mysql.format("SELECT id, description FROM items WHERE restaurant_id = ?", [restaurantId]);
        con.query(sql_query, function(err,result,fields){
            if (err) {
                res.send(err);
            };
            res.send(result);
        });
    })

    /**
     * HTTP GET request to get a recommendation for
     * an item at a specific restaurant unique to the
     * user. Returns the recommended item Id with a
     * status code of 200.
     */
    app.get("/item/recommend", (req,res) => {
        console.log("/item/recommend");
        let users_id = 1; //req.body.users_id;
        let restaurant_id = 1; //req.body.restaurant_id;
        let user_query = mysql.format("SELECT preferences FROM users WHERE id = ?", [users_id]);
        let desc_query = mysql.format("SELECT id, description FROM items WHERE restaurant_id = ?", [restaurant_id]);

        con.query(user_query, function(err,prefResult,fields){
            if (err) {
                res.send(err);
            };
            var preference = prefResult[0]["preferences"];

            con.query(desc_query, function(err,descResult,fields){
                if (err) {
                    res.send(err);
                };

                console.log(JSON.stringify(descResult));
                var descriptionJsonArray = JSON.parse(JSON.stringify(descResult));
                var itemDescriptionMap = new Map()

                descriptionJsonArray.forEach(item => {
                    itemDescriptionMap.set(item["id"], item["description"])
                });

                console.log(itemDescriptionMap);
            var itemId = recommendation.getRecommendation(preference, itemDescriptionMap);

                res.send({"itemId" : itemId});
            });
        });
    })
}
