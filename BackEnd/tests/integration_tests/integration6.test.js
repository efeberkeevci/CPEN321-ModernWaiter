const app = require('../../backend_server')
const supertest = require('supertest')
const { getUserOrder } = require('../../routes/orders')
const { getRestaurant } = require('../../routes/restaurants')
const { getRecommendation } = require('../../recommendation_logic')
const { addOrderedItems, getOrderedItems } = require('../../routes/ordered_items')
const { getStripeKey, createStripePayment } = require('../../routes/payment')
const { testGetMenu } = require('./test_functions')
const request = supertest(app)

//Test to get restaurant, get restaurant menu, 
let restaurantId

describe("Integration test 6: ", () => {
    it("Test to add a restaurant, verify added restaurant, get restaurant menu", async done => {
        await testAddRestaurant()
        await testGetRestaurant()
        await testGetMenu()
        done()
    })
})

async function testGetRestaurant() {
    // Arrange
    const url = `/restaurants/${restaurantId}`

    // Act
    const response = await request.get(url)

    // Assert
    expect(response.status).toBe(200)

}

async function testAddRestaurant() {
    // Arrange
    const url = `/restaurants`
    const req_body = {
        "taxPercantage" : 12,
        "serviceFeePercentage": 0,
        "name" : "Best Restaurant",
        "location" : "Calgary"
    }

    // Act
    const response = await (await request.post(url)).setEncoding(req_body)

    // Assert
    expect(response.status).toBe(200)
    restaurantId = response.body.id
}


