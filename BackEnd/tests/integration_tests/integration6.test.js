const app = require('../../backend_server')
const supertest = require('supertest')
const { getUserOrder } = require('../../routes/orders')
const { get, addToMenu } = require('../../routes/restaurant')
const { getRecommendation } = require('../../recommendation_logic')
const { addOrderedItems, getOrderedItems } = require('../../routes/ordered_items')
const { getStripeKey, createStripePayment } = require('../../routes/payment')
const { testGetMenu } = require('./test_functions')
const request = supertest(app)

//Test to get restaurant, get restaurant menu, 
let restaurantId = 1

describe("Integration test 6: ", () => {
    it("Test to get restaurant, get restaurant menu", async done => {
        await testGetRestaurant()
        await testGetMenu()
        done()
    })
})

async function testGetRestaurant() {
    // Arrange
    const url = `/items/${restaurantId}`

    // Act
    const response = await request.get(url)

    // Assert
    expect(response.status).toBe(200)

}