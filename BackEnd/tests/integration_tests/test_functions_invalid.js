const app = require('../../backend_server')
const supertest = require('supertest')
const request = supertest(app)

// Test variables
var userId = 2
var restaurantId = 2
var tableId = 4
var amount = 51
var orderId
var menu
let name = "testItem" 
let type = "sushi"
let cost = "12.5"
let description = "abc" 
let calories = "123"
let popularityCount = "0"
let image = "" 
var userId

var timestamp = new Date().getTime()
var username = "Integration_test" + timestamp
var googleId = "dummy_google" + timestamp
var email = "integration_test_user" + timestamp + "@gmail.com"
var preferences = "chicken"

var dummyString = "dummy"

// async function testCreateOrder() {
//     // Arrange
//     const url = `/orders`
//     const req = 
//         {
//             "userId" : userId,
//             "tableId" : tableId,
//             "restaurantId" : restaurantId,
//             "amount" : 0,
//             "hasPaid" : 0,
//             "isActive" : 1
//         }

//     // Act
//     const response = await request.post(url).send(req)

//     // Assert
//     expect(response.status).toBe(201)
// }

async function testGetUserOrderInvalid() {
    // Arrange
    const isActive = 1
    const url = `/orders/user/${dummyString}?isActive=${dummyString}`

    // Act
    const response = await request.get(url)

    // Assert
    expect(response.status).toBe(400)
}

async function testGetTableOrderInvalid() {
    // Arrange
    const isActive = 1
    const url = `/orders/table/${dummyString}?isActive=${dummyString}`

    // Act
    const response = await request.get(url)

    // Assert
    expect(response.status).toBe(400)
}

async function testGetMenuInvalid() {
    // Arrange
    const url = `/items/${dummyString}`

    // Act
    const response = await request.get(url)

    // Assert
    expect(response.status).toBe(400)
}

async function testAddOrderedItemsInvalid() {
    //Arrange
    let req_body =
        [
            {
                "orderId"  : dummyString,
                "itemId"  : dummyString
            },
            {
                "orderId"  : dummyString,
                "itemId"  : dummyString
            },
            {
                "orderId"  : dummyString,
                "itemId"  : dummyString
            }
        ]
 
    let url = "/ordered-items/"

    //Act
    const res = await request.post(url).send(req_body)

    //Assert
    expect(res.status).toBe(400)
}

async function testGetOrderedItemsInvalid() {
    //Arrange
    let url = "/ordered-items/"
    
    //Act
    const res = await request.get(url + dummyString)

    //Assert
    expect(res.status).toBe(400)
}

// async function testGetStripeKey() {
//     //Arrange
//     let url = "/key"
    
//     //Act
//     const res = await request.get(url)

//     //Assert
//     expect(res.status).toEqual(200)
//     expect(res.body.publishableKey).toStrictEqual(expect.anything())
// }

// async function testCreateStripePayment() {
//     //Arrange
//     let req_body =
//         {
//             "paymentMethodId" : "pm_card_visa",
//             "paymentIntendId" : null,
//             "currency" : "cad",
//             "useStripeSdk" : true,
//             "orderAmount" : (amount * 100)
//         }
 
//     let url = "/pay"

//     //Act
//     const res = await request.post(url).send(req_body)

//     //Assert
//     expect(res.status).toBe(200)
//     expect(res.body.clientSecret).toStrictEqual(expect.anything())
// }

async function testTableSessionDoneInvalid(){
    //Arrange
    let req_body =
        {
            "orderId" : dummyString,
            "isActive" : dummyString
        }
 
    let url = "/orders/session"

    //Act
    const res = await request.put(url).send(req_body)

    //Assert
    expect(res.status).toBe(400)
}

async function testPaidStatusDoneInvalid(){
    //Arrange
    let req_body =
        {
            "orderId" : dummyString,
            "hasPaid" : dummyString
        }
 
    let url = "/orders/paid"

    //Act
    const res = await request.put(url).send(req_body)

    //Assert
    expect(res.status).toBe(400)
}

async function testOrderedItemSelectedInvalid(){
    //Arrange
    let req_body =
        {
            "orderId" : dummyString,
            "itemId" : dummyString,
            "isSelected" : dummyString,
            "userId" : dummyString
        }
    
    let url = "/ordered-items/selected"

    //Act
    const res = await request.put(url).send(req_body)

    //Assert
    expect(res.status).toBe(400)
}

async function testOrderedItemPaidInvalid(){
    //Arrange
    let req_body =
        {
            "orderId" : dummyString,
            "itemId" : dummyString,
            "hasPaid" : dummyString
        }
    
    let url = "/ordered-items/paid"

    //Act
    const res = await request.put(url).send(req_body)

    //Assert
    expect(res.status).toBe(400)
}

async function testAddToMenuInvalid() {
    //Arrange
    let req_body =
        {
            "restaurantId" : dummyString ,
            "name" : name ,
            "type" : type,
            "cost" : dummyString,
            "description" : description ,
            "calories" : dummyString,
            "popularityCount" : dummyString,
            "image" : image 
        }
 
    let url = "/items"

    //Act
    const res = await request.post(url).send(req_body)

    //Assert
    expect(res.status).toBe(400)
}

async function testAddToMenuInvalidAlt() {
    //Arrange
    let req_body =
        {
            "restaurantId" : restaurantId ,
            "cost" : cost,
            "description" : description ,
            "calories" : calories,
            "popularityCount" : popularityCount,
        }
 
    let url = "/items"

    //Act
    const res = await request.post(url).send(req_body)

    //Assert
    expect(res.status).toBe(400)
}

// async function testGetMenuLatestItem() {
//     // Arrange
//     const url = `/items/${restaurantId}`

//     // Act
//     const response = await request.get(url)

//     // Assert
//     expect(response.status).toBe(200)

//     expect(response.body[response.body.length-1].id).toStrictEqual(expect.anything())
//     expect(response.body[response.body.length-1].restaurant_id).toStrictEqual(restaurantId)
//     expect(response.body[response.body.length-1].name).toStrictEqual(name)
//     expect(response.body[response.body.length-1].type).toStrictEqual(type)
//     expect(String(response.body[response.body.length-1].cost)).toStrictEqual(String(cost))
//     expect(response.body[response.body.length-1].description).toStrictEqual(description)
//     expect(String(response.body[response.body.length-1].calories)).toStrictEqual(String(calories))
//     expect(String(response.body[response.body.length-1].popularity_count)).toStrictEqual(String(popularityCount))
//     expect(response.body[response.body.length-1].image).toStrictEqual(image)
// }

// async function testCreateUser() {
//     // Arrange
//     const url = `/users`
//     const req_body = 
//         {
//             "username" : username,
//             "email" : email,
//             "googleId" : googleId,
//             "preferences" : preferences
//         }

//     // Act
//     const response = await request.post(url).send(req_body)

//     // Assert
//     expect(response.status).toBe(200)
// }

// async function testGetUserByGoogleId() {
//     // Arrange
//     const url = `/users/google/${googleId}`

//     // Act
//     const response = await request.get(url)

//     // Assert
//     expect(response.status).toBe(200)
//     expect(response.body.username).toStrictEqual(username)
//     expect(response.body.email).toStrictEqual(email)
//     expect(response.body.google_id).toStrictEqual(googleId)
//     expect(response.body.preferences).toStrictEqual(preferences)
//     expect(response.body.username).toStrictEqual(username)

//     userId = response.body.id
// }

// async function testGetUserByUserId() {
//     // Arrange
//     const url = `/users/${userId}`

//     // Act
//     const response = await request.get(url)

//     // Assert
//     expect(response.status).toBe(200)
//     expect(response.body.username).toStrictEqual(username)
//     expect(response.body.email).toStrictEqual(email)
//     expect(response.body.google_id).toStrictEqual(googleId)
//     expect(response.body.preferences).toStrictEqual(preferences)
//     expect(response.body.username).toStrictEqual(username)
// }

// async function testGetUserPreferences() {
//     // Arrange
//     const url = `/users/preferences/${userId}`

//     // Act
//     const response = await request.get(url)

//     // Assert
//     expect(response.status).toBe(200)
//     expect(response.body.preferences).toStrictEqual(preferences)
// }

// async function testUpdateUserPreferences() {
//     // Arrange
//     const url = `/users/preferences/`
//     const req_body = 
//         {
//             "userId" : userId,
//             "preferences" : "mango cucumber tuna spicy"
//         }

//     // Act
//     const response = await request.put(url).send(req_body)

//     // Assert
//     expect(response.status).toBe(200)
//     preferences = "mango cucumber tuna spicy"
// }

/* Invalid cases functions */
async function testCreateOrderInvalid() {
    // Arrange
    const url = `/orders`
    const req = 
        {
            "userId" : dummyString,
            "tableId" : dummyString,
            "restaurantId" : dummyString,
            "amount" : 0,
            "hasPaid" : 0,
            "isActive" : 1
        }

    // Act
    const response = await request.post(url).send(req)

    // Assert
    expect(response.status).toBe(400)
}

async function testGetRecommendationInvalid() {
    // Arrange
    const url = `/recommendation/${dummyString}/${dummyString}`

    // Act
    const response = await request.get(url)

    // Assert
    expect(response.status).toBe(400)
}


async function testGetRestaurantInvalid() {
    // Arrange
    const url = `/restaurants/${dummyString}`

    // Act
    const response = await request.get(url)

    // Assert
    expect(response.status).toBe(400)
}

async function testAddRestaurantInvalid() {
    // Arrange
    const url = `/restaurants`
    const req_body = {
        "taxPercentage" : dummyString,
        "serviceFeePercentage": dummyString,
        "name" : dummyString,
        "location" : dummyString
    }

    // Act
    const response = await request.post(url).send(req_body)

    // Assert
    expect(response.status).toBe(400)
}

async function testAddRestaurantInvalidAlt() {
    // Arrange
    const url = `/restaurants`
    const req_body = {
        "taxPercentage" : 5,
        "serviceFeePercentage": 5,
    }

    // Act
    const response = await request.post(url).send(req_body)

    // Assert
    expect(response.status).toBe(400)
}

module.exports = {
    // testCreateOrder, testPaidStatusDone, testGetUserOrder, testGetMenu, 
    // testGetRecommendation, testAddOrderedItems, testGetOrderedItems, testOrderedItemPaid,
    // testGetStripeKey, testCreateStripePayment, testGetTableOrder, testTableSessionDone, 
    // testOrderedItemSelected, testCreateUser, testGetUserByGoogleId, testGetUserByUserId, 
    // testGetUserPreferences, testUpdateUserPreferences, testAddToMenu, testGetMenuLatestItem, 
    testCreateOrderInvalid, testGetRecommendationInvalid, testGetUserOrderInvalid, testGetTableOrderInvalid,
    testTableSessionDoneInvalid, testPaidStatusDoneInvalid, testAddOrderedItemsInvalid, testGetOrderedItemsInvalid, 
    testOrderedItemSelectedInvalid, testOrderedItemPaidInvalid, testGetRestaurantInvalid, testAddRestaurantInvalid,
    testAddRestaurantInvalidAlt, testGetMenuInvalid, testAddToMenuInvalid, testAddToMenuInvalidAlt
}