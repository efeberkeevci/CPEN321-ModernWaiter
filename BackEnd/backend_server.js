const express = require("express")
const mysql = require('mysql')
const app = express()
const push_notification = require("./push_notification.js")
const sql = require("./sql_connection.js")
const con = sql.getConnection()

// Reference to the API routes
const items = require('./routes/items.js')
// const options = require('./routes/options.js')
// const orders = require('./routes/orders.js')
// const ordered_items = require('./routes/ordered_items.js')
// const payment = require('./routes/payment.js')
// const recommendation = require('./routes/recommendation.js')
// const restaurants = require('./routes/restaurants.js')
// const tables = require('./routes/tables.js')
// const users = require('./routes/users.js')

app.use(express.json())

con.connect(function(err) {
    if (err) throw err
    console.log("Connected!")
    var server = app.listen(3000,function(){
        var port = server.address().port
        console.log("Server started listening at %s", port)
    })
})

/**
 * HTTP GET request to acquire items of a
 * restaurant representing its menu. It
 * returns a list of all the items belonging
 * to the restaurant with a status code of
 * 200 if successful.   
 */
app.get("/items/:id", items.getMenu)

// function pushNotificationsDemo(){
//     subscribe("dti7Svc4SC6utD7GPz9ZXy:APA91bEVZQYS-PJ1OYgYqbOElQkM_BTI7Si_S3eLXOpO-oIpM155VGAJzl-FJHYFUNMMYdfg3cOvWM6bX5X-6m6k7H6QQCdZA96qEZt3lwRpE68iOmb7uVx8hfbx5SZUuy8MnnTdGArg","1")
//     messageAccountisClosed(1)
// }

/**
 * HTTP POST request to register token for
 * push notification service.
 */
app.post("/registrationToken", (req,res) => {
    console.log("/registrationToken")
    let registrationToken = req.body.registrationToken
    console.log(registrationToken)
	let orderId = "1"
    res.send(push_notification.subscribe(registrationToken, orderId))
})

//Informs server that the current cart is checked out
app.get("/checkout", (req,res) =>{
    console.log("Checkout received")
    push_notification.push_notification_order_received("1")
    res.send("Success")
})

/* HTTP GET request for health check */
app.get('/_health', (req, res) => {
  res.status(200).send('ok')
})

module.exports = app