const app = require('../backend_server')
const supertest = require('supertest')
const request = supertest(app)

var userId = 3
var restaurantId = 1
var tableId = 4
var amount = 51
var orderId
var menu

describe("Integration test 1: ", () => {
    it("Setting up a customer table", async done => {
        await testCreateOrder()
        await testGetUserOrder()
        await testGetMenu()
        await testGetRecommendation()
        done()
    })
})

describe("Integration test 2: ", () => {
    it("Adding items to cart", async done => {
        await testAddOrderedItems()
        done()
    })
})

describe("Integration test 3: ", () => {
    it("Viewing bill", async done => {
        await testGetOrderedItems()
        done()
    })
})

describe("Integration test 4: ", () => {
    it("Paying for all the items", async done => {
        await testGetStripeKey()
        await testCreateStripePayment()
        done()
    })
})

async function testCreateOrder() {
    // Arrange
    const url = `/orders`
    const req = 
        {
        "userId" : userId,
        "tableId" : tableId,
        "restaurantId" : restaurantId,
        "amount" : 0,
        "hasPaid" : 0,
        "isActive" : 1
        }

    // Act
    const response = await request.post(url).send(req)

    // Assert
    expect(response.status).toBe(201)
}

async function testGetUserOrder() {
    // Arrange
    const isActive = 1
    const url = `/orders/user/${userId}?isActive=${isActive}`

    // Act
    const response = await request.get(url)

    // Assert
    expect(response.status).toBe(200)
    expect(response.body[0].id).toStrictEqual(expect.anything())
    expect(response.body[0].tables_id).toStrictEqual(tableId)
    expect(response.body[0].users_id).toStrictEqual(userId)
    expect(response.body[0].restaurant_id).toStrictEqual(restaurantId)
    expect(response.body[0].amount).toStrictEqual(expect.anything())
    expect(response.body[0].has_paid).toStrictEqual(expect.anything())
    expect(response.body[0].is_active_session).toStrictEqual(expect.anything())

    orderId = response.body[0].id
}

async function testGetMenu() {
    // Arrange
    const url = `/items/${restaurantId}`

    // Act
    const response = await request.get(url)

    // Assert
    expect(response.status).toBe(200)
    expect(response.body[0].id).toStrictEqual(expect.anything())
    expect(response.body[0].restaurant_id).toStrictEqual(restaurantId)
    expect(response.body[0].name).toStrictEqual(expect.anything())
    expect(response.body[0].type).toStrictEqual(expect.anything())
    expect(response.body[0].cost).toStrictEqual(expect.anything())
    expect(response.body[0].description).toStrictEqual(expect.anything())
    expect(response.body[0].calories).toStrictEqual(expect.anything())
    expect(response.body[0].popularity_count).toStrictEqual(expect.anything())
    expect(response.body[0].image).toStrictEqual(expect.anything())

    menu = response.body
}

async function testGetRecommendation() {
    // Arrange
    const url = `/items/${restaurantId}`

    // Act
    const response = await request.get(url)

    // Assert
    expect(response.status).toBe(200)
    expect(response.body[0].id).toStrictEqual(expect.anything())
    expect(response.body[0].restaurant_id).toStrictEqual(restaurantId)
    expect(response.body[0].name).toStrictEqual(expect.anything())
    expect(response.body[0].type).toStrictEqual(expect.anything())
    expect(response.body[0].cost).toStrictEqual(expect.anything())
    expect(response.body[0].description).toStrictEqual(expect.anything())
    expect(response.body[0].calories).toStrictEqual(expect.anything())
    expect(response.body[0].popularity_count).toStrictEqual(expect.anything())
    expect(response.body[0].image).toStrictEqual(expect.anything())

    menu = response.body
}

async function testAddOrderedItems() {
    //Arrange
    let req_body =
        [
            {
                "orderId"  : orderId,
                "itemId"  : 1
            },
            {
                "orderId"  : orderId,
                "itemId"  : 2
            },
            {
                "orderId"  : orderId,
                "itemId"  : 3
            }
        ]
 
    let url = "/ordered-items/"

    //Act
    const res = await request.post(url).send(req_body)

    //Assert
    expect(res.status).toBe(201)
    expect(res.body).toStrictEqual({})
}

async function testGetOrderedItems() {
    //Arrange
    let url = "/ordered-items/"
    
    //Act
    const res = await request.get(url + orderId)

    //Assert
    expect(res.status).toEqual(200)
    expect(typeof res.body[0].id).toBe('number')
    expect(typeof res.body[0].orders_id).toBe('number')
    expect(typeof res.body[0].items_id).toBe('number')
    expect(typeof res.body[0].has_paid).toBe('number')
    expect(typeof res.body[0].is_selected).toBe('number')
    expect(typeof res.body[0].users_id).toBe('number')
}

async function testGetStripeKey() {
    //Arrange
    let url = "/key"
    
    //Act
    const res = await request.get(url)

    //Assert
    expect(res.status).toEqual(200)
    expect(res.body.publishableKey).toStrictEqual(expect.anything())
}

async function testCreateStripePayment() {
    //Arrange
    let req_body =
        {
            "paymentMethodId" : "pm_card_visa",
            "paymentIntendId" : null,
            "currency" : "cad",
            "useStripeSdk" : true,
            "orderAmount" : (amount * 100)
        }
 
    let url = "/pay"

    //Act
    const res = await request.post(url).send(req_body)

    //Assert
    expect(res.status).toBe(200)
    expect(res.body.clientSecret).toStrictEqual(expect.anything())
}