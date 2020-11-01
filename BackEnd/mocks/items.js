const menu = 
    [{
        "id": 1,
        "restaurant_id": 1,
        "name": "Spicy Ahi Roll",
        "type": "Sushi",
        "cost": 16.5,
        "description": "ocean wise ahi tuna, mango, avocado, asparagus, cucumber, sesame soy paper, wasabi mayo, cripy yam curls",
        "calories": 500,
        "popularity_count": 3,
        "image": "gs://modern-waiter-47e96.appspot.com/dummy-spicy-ahi.jpg"
    }, {
        "id": 2,
        "restaurant_id": 1,
        "name": "Prawn Crunch Roll",
        "type": "Sushi",
        "cost": 16,
        "description": "crispy prawn, mango, avocado, asparagus, cucumber, sesame soy paper, sriracha mayo, soy glaze",
        "calories": 500,
        "popularity_count": 4,
        "image": "gs://modern-waiter-47e96.appspot.com/dummy-prawn-crunch.jpg"
    }]

module.exports = function(app, con){
    function getMenu(req, res){
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
}
