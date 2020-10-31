const express = require('express')
const mysql = require('mysql')
var recommendation = require("./../recommendation.js");

module.exports = function(app, con){
    app.use(express.json());

    /**
     * HTTP GET request to get a recommendation for
     * an item at a specific restaurant unique to the
     * user. Returns the recommended item Id with a
     * status code of 200.
     */
    app.get("/recommendation", (req, res) => {
        console.log("/recommendation");
        let users_id = req.body.userId;
        let restaurant_id = req.body.restaurantId;
        let user_query = mysql.format("SELECT preferences FROM users WHERE id = ?", [users_id]);
        let desc_query = mysql.format("SELECT id, description FROM items WHERE restaurant_id = ?", [restaurant_id]);

        con.query(user_query, function(err, prefResult){
            if (err) {
                res.send(err);
            };
            var preference = prefResult[0]["preferences"];

            con.query(desc_query, function(err, descResult) {
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
