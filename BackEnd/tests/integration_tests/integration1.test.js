const app = require('../../backend_server')
const supertest = require('supertest')
const { getUserOrder } = require('../../routes/orders')
const { getMenu } = require('../../routes/items')
const { getRecommendation } = require('../../recommendation_logic')
const { addOrderedItems, getOrderedItems } = require('../../routes/ordered_items')
const { getStripeKey, createStripePayment } = require('../../routes/payment')
const request = supertest(app)
const{testCreateOrder, testGetUserOrder, testGetMenu, 
    testGetRecommendation } = require("./test_functions")


describe("Integration test 1: ", () => {
    it("Setting up a customer table", async done => {
        await testCreateOrder()
        await testGetUserOrder()
        await testGetMenu()
        await testGetRecommendation()
        done()
    })
})



