const express = require("express")
const mysql = require('mysql')
const app = express()
const push_notification = require("./push_notification.js")
const sql = require("./sql_connection.js")
const con = sql.getConnection()

const items = require('./routes/items.js')
const options = require('./routes/options.js')
const ordered_items = require('./routes/ordered_items.js')
const orders = require('./routes/orders.js')
const payment = require('./routes/payment.js')
const recommendation = require('./routes/recommendation.js')
const restaurants = require('./routes/restaurants.js')
const tables = require('./routes/tables.js')
const users = require('./routes/users.js')

app.use(express.json())

con.connect(function(err) {
    if (err) throw err
    console.log("Connected!")
    var server = app.listen(3000,function(){
        var port = server.address().port
        console.log("Server started listening at %s", port)
    })
})

/*********************** REST API routes ****************************/

// Routes for items
app.get("/items/:id", items.getMenu)

// Routes for options
app.get("/item-options/:id", options.getItemOptions)
app.get("/options/:id", options.getOptions)

// Routes for ordered items
app.get("/ordered-items/:orderId", ordered_items.getOrderedItems)
app.post("/ordered-items", ordered_items.addOrderedItem)
app.put("/ordered-items/paid", ordered_items.updateOrderedItemPaidStatus)

// Routes for orders
app.post("/orders", orders.createOrder)
app.get("/orders/user/:users_id", orders.getUserOrder)
app.get("/orders/table/:tables_id", orders.getTableOrder)
app.put("/orders/session", orders.updateOrderSessionStatus)
app.put("/orders/paid", orders.updateOrderPaidStatus)

// Routes for Stripe (external API)
app.get('/key', payment.getStripeKey)
app.post('/pay', payment.createStripePayment)

// Routes for item recommendation
app.get("/recommendation/:userId/:restaurantId", recommendation.getItemRecommendation) 

// Routes for restaurant
app.get("/restaurants/:id", restaurants.getRestaurant)

// Routes for table
app.get("/tables/:id", tables.getTable)

// Routes for user
app.get("/users/:id", users.getUser)
app.get("/users/preferences/:id", users.getUserPreferences)

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