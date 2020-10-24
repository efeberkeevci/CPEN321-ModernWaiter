
var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
const Http = new XMLHttpRequest();
const base_url='http://localhost:3000';

//Get restaurant info by restaurant id
url = base_url +"/restaurant?id=1";
Http.open("GET", url);
Http.send();

Http.onreadystatechange = (e) => {
  console.log(Http.responseText);
}

//Get items with restaurant id
url = base_url +"/items?id=1";
Http.open("GET", url);
Http.send();

Http.onreadystatechange = (e) => {
  console.log(Http.responseText);
}

//Get items with restaurant id
url = base_url +"/item_options?id=1";
Http.open("GET", url);
Http.send();

Http.onreadystatechange = (e) => {
  console.log(Http.responseText);
}
    
//Get options by id
url = base_url +"/options?id=1";
Http.open("GET", url);
Http.send();

Http.onreadystatechange = (e) => {
  console.log(Http.responseText);
}

//Get table by id
url = base_url +"/table?id=1";
Http.open("GET", url);
Http.send();

Http.onreadystatechange = (e) => {
  console.log(Http.responseText);
}

//Get user by id
url = base_url +"/user?id=1";
Http.open("GET", url);
Http.send();

Http.onreadystatechange = (e) => {
  console.log(Http.responseText);
}

//Post order with given info
const data = {
    "users_id": "2",
    "tables_id" : "3",
    "restaurant_id" : "1",
    "amount" : "25.0",
    "ordered_at" : "2020-10-24 10:22:20",
    "has_paid" : "0",
    "is_active_session" : "1"
};
url = base_url +"/order";
fetch(url, {
  method: 'POST', 
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify(data),
})
.then(response => response.json())
.then(data => {
  console.log('Success:', data);
})
.catch((error) => {
  console.error('Error:', error);
});


//Update order amount with a given amount
app.post("/add/amount/to/order", (req,res) => {
const data = {
    "amount" : "25.0"
};
url = base_url +"/add/amount/to/order";
fetch(url, {
  method: 'POST', 
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify(data),
})
.then(response => response.json())
.then(data => {
  console.log('Success:', data);
})
.catch((error) => {
  console.error('Error:', error);
});

//Get order by userID
url = base_url +"/order/user/id?id=1";
Http.open("GET", url);
Http.send();

Http.onreadystatechange = (e) => {
  console.log(Http.responseText);
}


//Get order by tablesID
url = base_url +"/order/table/id?id=1";
Http.open("GET", url);
Http.send();

Http.onreadystatechange = (e) => {
  console.log(Http.responseText);
}

//If the session is complete, set the active session flag to false
const data = {
    "amount" : "25.0"
};
url = base_url +"/add/amount/to/order";
fetch(url, {
    method: 'POST', 
    headers: {
    'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
})
.then(response => response.json())
.then(data => {
    console.log('Success:', data);
})
.catch((error) => {
    console.error('Error:', error);
});

//If all the ordered items have been paid for, mark this as has paid 
app.post("order/has/paid", (req,res) => {
url = base_url +"/order/has/paid?id=1";
Http.open("POST", url);
Http.send();

Http.onreadystatechange = (e) => {
  console.log(Http.responseText);
}


//Gets ordered items via orderid
app.get("/ordered/items", (req,res) => {


// adds a new item to the ordered items table, used when you add an item to your meal later on during your time at the restaurant
app.post("/add/item/to/order", (req,res) => {


// if you pay for the specific item, mark it as has paid
app.post("/mark/item/has/paid", (req,res) => {
const data = {
    "amount" : "25.0"
};
url = base_url +"/mark/item/has/paid";
fetch(url, {
    method: 'POST', 
    headers: {
    'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
})
.then(response => response.json())
.then(data => {
    console.log('Success:', data);
})
.catch((error) => {
    console.error('Error:', error);
});