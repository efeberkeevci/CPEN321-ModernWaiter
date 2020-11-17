const app = require('../../backend_server')
const supertest = require('supertest')
const { getUserOrder } = require('../../routes/orders')
const { getMenu } = require('../../routes/items')
const { getRecommendation } = require('../../recommendation_logic')
const { addOrderedItems, getOrderedItems } = require('../../routes/ordered_items')
const { getStripeKey, createStripePayment } = require('../../routes/payment')
const request = supertest(app)
const request = supertest(app)
const{testCreateOrder, testGetUserOrder, testGetMenu, 
    testGetRecommendation, testAddOrderedItems, testGetOrderedItems, 
    testGetStripeKey, testCreateStripePayment} = require("./test_functions")

describe("Integration test 2: ", () => {
    it("Adding items to cart", async done => {
        await testAddOrderedItems()
        done()
    })
})

